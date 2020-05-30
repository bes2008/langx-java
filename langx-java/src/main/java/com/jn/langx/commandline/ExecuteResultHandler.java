package com.jn.langx.commandline;

/**
 * The callback handlers for the result of asynchronous process execution. When a
 * process is started asynchronously the callback provides you with the result of
 * the executed process, i.e. the exit value or an exception. 
 *
 * @see CommandLineExecutor#execute(CommandLine, java.util.Map, ExecuteResultHandler)
 *
 * @version $Id: ExecuteResultHandler.java 1636056 2014-11-01 21:12:52Z ggregory $
 */
public interface ExecuteResultHandler {

  /**
   * The asynchronous execution completed.
   *
   * @param exitValue the exit value of the sub-process
   */
    void onProcessComplete(int exitValue);

  /**
   * The asynchronous execution failed.
   *
   * @param e the {@code ExecuteException} containing the root cause
   */
    void onProcessFailed(ExecuteException e);
}
