package com.github.spuchmann.xml.splitter.samples;


import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import com.github.spuchmann.xml.splitter.XmlSplitter;
import com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter;
import com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JavaXmlFileSplitterTest {

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

    private void splitXml(XmlSplitter splitter, File outputDir)
            throws XmlSplitException, IOException {
        XmlSplitStatistic statistic;

        try (InputStream is = JavaXmlFileSplitterTest.class.getResourceAsStream("/xml/generatedTestOutput.xml")) {
            statistic = splitter.split("simpleElementSplit", is);
        }

        assertThat(statistic.getCount(), is(7));

        assertThat(outputDir.list().length, is(7));

        for (File file : outputDir.listFiles()) {
            System.out.println("\nFilename: " + file.getName());
            System.out.println(Files.toString(file, Charsets.UTF_8));
        }
    }
}
