package com.github.spuchmann.xml.splitter.samples;


import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import com.github.spuchmann.xml.splitter.stax.InMemoryStaxNodeSplitter;
import com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JavaXmlInMemorySplitter {

    @Test
    public void testSimpleJavaSplitting() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));

        System.out.println("\n\n\n******************************\n");
        System.out.println("Splitting Xml");
        splitXml(splitter);
    }

    @Test
    public void testSurroundingJavaSplitting() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));
        splitter.setDocumentEventHandler(new XmlSurroundingNodeDocumentEventHandler(new QName("parentElement")));

        System.out.println("\n\n\n******************************\n");
        System.out.println("Splitting Xml With surrounding node");
        splitXml(splitter);
    }

    @Test
    public void testSurroundingJavaSplitterWithGlobalValues() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));
        splitter.setGlobalDataCollectorNameList(Lists.newArrayList(new QName("global"), new QName("global1")));

        XmlSurroundingNodeDocumentEventHandler eventHandler = new XmlSurroundingNodeDocumentEventHandler();
        eventHandler.setNode(new QName("root"));
        eventHandler.setGlobalValueList(Lists.<QName>newArrayList(new QName("global1")));

        splitter.setDocumentEventHandler(eventHandler);

        System.out.println("\n\n\n******************************\n");
        System.out.println("Splitting Xml With Global Values");
        splitXml(splitter);
    }

    private List<ByteArrayOutputStream> splitXml(InMemoryStaxNodeSplitter splitter)
            throws XmlSplitException, IOException {
        XmlSplitStatistic statistic;

        try (InputStream is = JavaXmlInMemorySplitter.class.getResourceAsStream("/xml/generatedTestOutput.xml")) {
            statistic = splitter.split("simpleElementSplit", is);
        }

        assertThat(statistic.getCount(), is(7));

        List<ByteArrayOutputStream> resultList = splitter.getResultList();
        assertThat(resultList.size(), is(7));

        for (ByteArrayOutputStream xml : resultList) {
            System.out.println(xml + "\n");
        }

        return splitter.getResultList();
    }
}
