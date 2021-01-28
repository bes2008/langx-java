package com.jn.langx.commandline;

import com.jn.langx.commandline.launcher.CommandLauncher;
import com.jn.langx.commandline.launcher.CommandLauncherFactory;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.concurrent.completion.CompletableFuture;
import com.jn.langx.util.function.Supplier0;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * The default class to start a subprocess. The implementation
 * allows to
 * <ul>
 *  <li>set a current working directory for the subprocess</li>
 *  <li>provide a set of environment variables passed to the subprocess</li>
 *  <li>capture the subprocess output of stdout and stderr using an ExecuteStreamHandler</li>
 *  <li>kill long-running processes using an ExecuteWatchdog</li>
 *  <li>define a set of expected exit values</li>
 *  <li>terminate any started processes when the main process is terminating using a ProcessDestroyer</li>
 * </ul>
 * <p>
 * The following example shows the basic usage:
 *
 * <pre>
 * Executor exec = new DefaultExecutor();
 * CommandLine cl = new CommandLine("ls -l");
 * int exitvalue = exec.execute(cl);
 * </pre>
 */
public class DefaultCommandLineExecutor implements CommandLineExecutor {
    private ExecutorService executorService;
    /**
     * taking care of output and error stream
     */
    private ExecuteStreamHandler streamHandler;

    /**
     * the working directory of the process
     */
    private File workingDirectory;

    /**
     * monitoring of long running processes
     */
    private ExecuteWatchdog watchdog;

    /**
     * the exit values considered to be successful
     */
    private int[] exitValues;

    /**
     * launches the command in a new process
     */
    private final CommandLauncher launcher;

    /**
     * optional cleanup of started processes
     */
    private InstructionSequenceDestroyer processDestroyer;


    /**
     * the first exception being caught to be thrown to the caller
     */
    private Throwable exceptionCaught;

    public ExecuteStreamHandler getStreamHandler() {
        return streamHandler;
    }

    public void setStreamHandler(ExecuteStreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Default constructor creating a default {@code PumpStreamHandler}
     * and sets the working directory of the subprocess to the current
     * working directory.
     * <p>
     * The {@code PumpStreamHandler} pumps the output of the subprocess
     * into our {@code System.out} and {@code System.err} to avoid
     * into our {@code System.out} and {@code System.err} to avoid
     * a blocked or deadlocked subprocess (see{@link java.lang.Process Process}).
     */
    public DefaultCommandLineExecutor() {
        this.streamHandler = new PumpStreamHandler();
        this.launcher = CommandLauncherFactory.createVMLauncher();
        this.exitValues = new int[0];
        this.workingDirectory = new File(".");
        this.exceptionCaught = null;
    }

    /**
     * @see CommandLineExecutor#getWatchdog()
     */
    public ExecuteWatchdog getWatchdog() {
        return watchdog;
    }

    /**
     * @see CommandLineExecutor#setWatchdog(com.jn.langx.commandline.ExecuteWatchdog)
     */
    public void setWatchdog(final ExecuteWatchdog watchDog) {
        this.watchdog = watchDog;
    }

    /**
     * @see CommandLineExecutor#getProcessDestroyer()
     */
    public InstructionSequenceDestroyer getProcessDestroyer() {
        return this.processDestroyer;
    }

    /**
     * @see CommandLineExecutor#setProcessDestroyer(InstructionSequenceDestroyer)
     */
    public void setProcessDestroyer(final InstructionSequenceDestroyer processDestroyer) {
        this.processDestroyer = processDestroyer;
    }

    /**
     * @see CommandLineExecutor#getWorkingDirectory()
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @see CommandLineExecutor#setWorkingDirectory(java.io.File)
     */
    public void setWorkingDirectory(final File dir) {
        this.workingDirectory = dir;
    }

    @Override
    public int execute(CommandLine command) throws ExecuteException, IOException {
        return execute(false, command);
    }

    public int execute(boolean async, final CommandLine command) throws IOException {
        return execute(async, command, (Map<String, String>) null);
    }

    @Override
    public int execute(CommandLine command, Map<String, String> environment) throws ExecuteException, IOException {
        return execute(false, command, environment);
    }

    public int execute(boolean async, final CommandLine command, final Map<String, String> environment) throws IOException {
        return execute(async, command, environment, null, null, null);
    }

    @Override
    public int execute(CommandLine command, ExecuteResultHandler handler) throws ExecuteException, IOException {
        return execute(false, command, handler);
    }

    public int execute(boolean async, final CommandLine command, final ExecuteResultHandler handler) throws IOException {
        return execute(async, command, null, null, null, handler);
    }

    @Override
    public int execute(CommandLine command, Map<String, String> environment, File workingDirectory, ExecuteStreamHandler streamHandler, ExecuteResultHandler handler) throws ExecuteException, IOException {
        return execute(false, command, environment, workingDirectory, streamHandler, handler);
    }

    public int execute(boolean async, final CommandLine command, final Map<String, String> environment, File workingDir, ExecuteStreamHandler executeStreamHandler, final ExecuteResultHandler resultHandler) throws IOException {
        if (watchdog != null) {
            watchdog.setProcessNotStarted();
        }
        final ExecuteStreamHandler streamHandler = Objs.useValueIfNull(executeStreamHandler, this.streamHandler);
        final File workingDirectory = Objs.useValueIfNull(workingDir, this.getWorkingDirectory());
        Supplier0<Integer> task = new Supplier0<Integer>() {
            @Override
            public Integer get() {
                return doExecute(command, environment, workingDirectory, streamHandler, resultHandler);
            }
        };
        CompletableFuture<Integer> future = executorService == null ? CompletableFuture.supplyAsync(task) : CompletableFuture.supplyAsync(task, executorService);

        if (!async) {
            try {
                return future.get();
            } catch (Throwable ex) {
                throw Throwables.wrapAsRuntimeException(ex);
            }
        }
        return -1;
    }

    private int doExecute(final CommandLine command, final Map<String, String> environment, File workingDir, ExecuteStreamHandler executeStreamHandler, final ExecuteResultHandler resultHandler) {
        int exitValue = CommandLineExecutor.INVALID_EXITVALUE;
        try {
            exitValue = executeInternal(command, environment, workingDirectory, streamHandler);
            resultHandler.onProcessComplete(exitValue);
        } catch (final ExecuteException e) {
            resultHandler.onProcessFailed(e);
        } catch (final Exception e) {
            resultHandler.onProcessFailed(new ExecuteException("Execution failed", exitValue, e));
        }

        return exitValue;
    }

    /**
     * @see CommandLineExecutor#setExitValue(int)
     */
    public void setExitValue(final int value) {
        this.setExitValues(new int[]{value});
    }


    /**
     * @see CommandLineExecutor#setExitValues(int[])
     */
    public void setExitValues(final int[] values) {
        this.exitValues = values == null ? null : (int[]) values.clone();
    }

    /**
     * @see CommandLineExecutor#isFailure(int)
     */
    public boolean isFailure(final int exitValue) {

        if (this.exitValues == null) {
            return false;
        } else if (this.exitValues.length == 0) {
            return this.launcher.isFailure(exitValue);
        } else {
            for (final int exitValue2 : this.exitValues) {
                if (exitValue2 == exitValue) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a process that runs a command.
     *
     * @param command    the command to run
     * @param env        the environment for the command
     * @param workingDir the working directory for the command
     * @return the process started
     * @throws IOException forwarded from the particular launcher used
     */
    protected InstructionSequence launch(final CommandLine command, final Map<String, String> env,
                                         final File workingDir) throws IOException {

        if (this.launcher == null) {
            throw new IllegalStateException("CommandLauncher can not be null");
        }

        if (workingDir != null && !workingDir.exists()) {
            throw new IOException(workingDir + " doesn't exist.");
        }
        return this.launcher.exec(command, env, workingDir);
    }


    /**
     * Close the streams belonging to the given Process.
     *
     * @param process the <CODE>Process</CODE>.
     */
    private void closeProcessStreams(final InstructionSequence process) {

        try {
            process.getInputStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }

        try {
            process.getOutputStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }

        try {
            process.getErrorStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }
    }

    /**
     * Execute an internal process. If the executing thread is interrupted while waiting for the
     * child process to return the child process will be killed.
     *
     * @param command       the command to execute
     * @param environment   the execution environment
     * @param dir           the working directory
     * @param streamHandler process the streams (in, out, err) of the process
     * @return the exit code of the process
     * @throws IOException executing the process failed
     */
    protected int executeInternal(final CommandLine command, final Map<String, String> environment, final File dir, final ExecuteStreamHandler streamHandler) throws IOException {

        setExceptionCaught(null);

        final InstructionSequence process = this.launch(command, environment, dir);

        try {
            streamHandler.setSubProcessInputStream(process.getOutputStream());
            streamHandler.setSubProcessOutputStream(process.getInputStream());
            streamHandler.setSubProcessErrorStream(process.getErrorStream());
        } catch (final IOException e) {
            process.destroy();
            throw e;
        }

        streamHandler.start();

        try {

            // add the process to the list of those to destroy if the VM exits
            if (this.getProcessDestroyer() != null) {
                this.getProcessDestroyer().add(process);
            }

            // associate the watchdog with the newly created process
            if (watchdog != null) {
                watchdog.start(process);
            }

            int exitValue = CommandLineExecutor.INVALID_EXITVALUE;

            try {
                exitValue = process.waitFor();
            } catch (final InterruptedException e) {
                process.destroy();
            } finally {
                // see http://bugs.sun.com/view_bug.do?bug_id=6420270
                // see https://issues.apache.org/jira/browse/EXEC-46
                // Process.waitFor should clear interrupt status when throwing InterruptedException
                // but we have to do that manually
                Thread.interrupted();
            }

            if (watchdog != null) {
                watchdog.stop();
            }

            try {
                streamHandler.stop();
            } catch (final IOException e) {
                setExceptionCaught(e);
            }

            closeProcessStreams(process);

            if (getExceptionCaught() != null) {
                throw Throwables.wrapAsRuntimeException(getExceptionCaught());
            }

            if (watchdog != null) {
                try {
                    watchdog.checkException();
                } catch (final IOException e) {
                    throw e;
                } catch (final Exception e) {
                    throw new IOException(e.getMessage());
                }
            }

            if (this.isFailure(exitValue)) {
                throw new ExecuteException("Process exited with an error: " + exitValue, exitValue);
            }

            return exitValue;
        } finally {
            // remove the process to the list of those to destroy if the VM exits
            if (this.getProcessDestroyer() != null) {
                this.getProcessDestroyer().remove(process);
            }
        }
    }

    /**
     * Keep track of the first IOException being thrown.
     *
     * @param e the IOException
     */
    private void setExceptionCaught(final Throwable e) {
        if (this.exceptionCaught == null) {
            this.exceptionCaught = e;
        }
    }

    /**
     * Get the first IOException being thrown.
     *
     * @return the first IOException being caught
     */
    private Throwable getExceptionCaught() {
        return this.exceptionCaught;
    }

}
