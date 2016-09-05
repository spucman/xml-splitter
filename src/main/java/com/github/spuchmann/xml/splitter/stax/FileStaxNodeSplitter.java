package com.github.spuchmann.xml.splitter.stax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class FileStaxNodeSplitter extends StaxNodeSplitter {

    private String outputFolder;

    public FileStaxNodeSplitter() {
    }

    public FileStaxNodeSplitter(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @Override
    protected XMLStreamWriter createNewStreamWriter(SplitContext context) throws XMLStreamException {
        try {
            return getOutputFactory().createXMLStreamWriter(new FileOutputStream(createFile(context)));

        } catch (IOException e) {
            throw new IllegalStateException("Unable to create xml stream writer", e);
        }
    }

    protected File createFile(SplitContext context) {
        return new File(outputFolder,
                createFilename(context.getBasename(), Integer.toString(context.getCurrentCount())));
    }

    protected String createFilename(String baseFilename, String addition) {
        return String.format("%s_%s.xml", baseFilename, addition);
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }
}