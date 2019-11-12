package com.jn.langx.util.jar.multiplelevel;

/**
 * Callback visitor triggered by {@link CentralDirectoryParser}.
 *
 */
interface CentralDirectoryVisitor {

    void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData);

    void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset);

    void visitEnd();

}
