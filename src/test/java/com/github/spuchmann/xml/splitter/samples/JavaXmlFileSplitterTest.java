package com.github.spuchmann.xml.splitter.samples;


import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter;
import com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

public class JavaXmlFileSplitterTest extends AbstractXmlFileSplitterTest {

    @Test
    public void testSimpleJavaSplittingWithFiles() throws IOException, XmlSplitException {
        File outputFolder = Files.createTempDir();

        FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();
        fileStaxNodeSplitter.setOutputFolder(outputFolder.getAbsolutePath());
        fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));

        fileStaxNodeSplitter.init();

        splitXml(fileStaxNodeSplitter, outputFolder);
    }

    @Test
    public void testSurroundingJavaSplitting() throws IOException, XmlSplitException {
        File outputFolder = Files.createTempDir();

        FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();
        fileStaxNodeSplitter.setOutputFolder(outputFolder.getAbsolutePath());
        fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));
        fileStaxNodeSplitter
                .setDocumentEventHandler(new XmlSurroundingNodeDocumentEventHandler(new QName("parentElement")));
        fileStaxNodeSplitter.init();

        System.out.println("\n\n\n******************************\n");
        System.out.println("Splitting Xml With surrounding node");

        splitXml(fileStaxNodeSplitter, outputFolder);
    }

    @Test
    public void testSurroundingJavaSplitterWithGlobalValues() throws IOException, XmlSplitException {
        File outputFolder = Files.createTempDir();

        FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();
        fileStaxNodeSplitter.setOutputFolder(outputFolder.getAbsolutePath());
        fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));
        fileStaxNodeSplitter
                .setGlobalDataCollectorNameList(Lists.newArrayList(new QName("global"), new QName("global1")));

        XmlSurroundingNodeDocumentEventHandler eventHandler = new XmlSurroundingNodeDocumentEventHandler();
        eventHandler.setNode(new QName("root"));
        eventHandler.setGlobalValueList(Lists.<QName>newArrayList(new QName("global1")));

        fileStaxNodeSplitter.setDocumentEventHandler(eventHandler);
        fileStaxNodeSplitter.init();

        System.out.println("\n\n\n******************************\n");
        System.out.println("Splitting Xml With Global Values");
        splitXml(fileStaxNodeSplitter, outputFolder);
    }
}
