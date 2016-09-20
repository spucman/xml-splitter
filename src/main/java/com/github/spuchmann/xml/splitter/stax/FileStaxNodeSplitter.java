package com.github.spuchmann.xml.splitter.stax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * file implementation of the node splitter. Every xml fragment will be stored inside a file.
 *
 * @since 0.1.0
 */
public class FileStaxNodeSplitter extends StaxNodeSplitter {

    private static final Logger log = LoggerFactory.getLogger(FileStaxNodeSplitter.class);

    private String outputFolder;

    private ThreadLocal<FileOutputStream> currentStream = new ThreadLocal<>();

    public FileStaxNodeSplitter() {
    }

    public FileStaxNodeSplitter(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    /**
     * initializes the file splitter (e.g. creating the outputFolder)
     */
    public void init() {
        new File(this.outputFolder).mkdirs();
    }

    @Override
    protected XMLStreamWriter createNewStreamWriter(SplitContext context) throws XMLStreamException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(context));
            currentStream.set(fileOutputStream);
            return getOutputFactory().createXMLStreamWriter(fileOutputStream, context.getEncoding());
        } catch (IOException e) {
            tryToCloseStream(fileOutputStream);
            throw new IllegalStateException("Unable to create xml stream writer", e);
        }
    }

    @Override
    protected void closeInternalStream() {
        tryToCloseStream(currentStream.get());
        currentStream.set(null);
    }

    private void tryToCloseStream(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("Unable to close file stream", e);
            }
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