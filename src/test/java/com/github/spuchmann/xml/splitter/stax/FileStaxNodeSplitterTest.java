package com.github.spuchmann.xml.splitter.stax;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FileStaxNodeSplitterTest {

    private FileStaxNodeSplitter splitter = new FileStaxNodeSplitter("target/testOutput");

    @Test
    public void testCreateFilename() {
        assertThat(splitter.createFilename("basename", "x"), is("basename_x.xml"));
        assertThat(splitter.createFilename("basename", "1"), is("basename_1.xml"));
    }

    @Test
    public void testCreateFile() {
        File outFile = splitter.createFile(new SplitContext("basename", 3));
        assertThat(outFile.getName(), is("basename_3.xml"));
        assertThat(outFile.getPath(), is("target/testOutput/basename_3.xml"));
    }

    public void testSplit() {

    }
}