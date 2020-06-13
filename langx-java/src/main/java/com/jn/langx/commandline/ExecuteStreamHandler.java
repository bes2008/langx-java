package com.jn.langx.commandline;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used by {@code Execute} to handle input and output stream of
 * subprocesses.
 *
 * @version $Id: ExecuteStreamHandler.java 1636064 2014-11-01 22:01:19Z ggregory $
 */
public interface ExecuteStreamHandler {

    /**
     * Install a handler for the input stream of the subprocess.
     * 
     * @param os
     *            output stream to write to the standard input stream of the subprocess
     * @throws IOException
     *             thrown when an I/O exception occurs.
     */
    void setProcessInputStream(OutputStream os) throws IOException;

    /**
     * Install a handler for the error stream of the subprocess.
     * 
     * @param is
     *            input stream to read from the error stream from the subprocess
     * @throws IOException
     *             thrown when an I/O exception occurs.
     */
    void setProcessErrorStream(InputStream is) throws IOException;

    /**
     * Install a handler for the output stream of the subprocess.
     * 
     * @param is
     *            input stream to read from the error stream from the subprocess
     * @throws IOException
     *             thrown when an I/O exception occurs.
     */
    void setProcessOutputStream(InputStream is) throws IOException;

    /**
     * Start handling of the streams.
     * 
     * @throws IOException
     *             thrown when an I/O exception occurs.
     */
    void start() throws IOException;

    /**
     * Stop handling of the streams - will not be restarted. Will wait for pump threads to complete.
     * 
     * @throws IOException
     *             thrown when an I/O exception occurs.
     */
    void stop() throws IOException;
}
