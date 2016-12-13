package com.github.spuchmann.xml.splitter.samples;

import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.stax.InMemoryStaxNodeSplitter;
import com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

public class JavaXmlSplitter {

    @Test
    public void testSimpleJavaSplitting() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));

        List<ByteArrayOutputStream> resultList = splitXml(splitter);

        assertThat(resultList.size(), is(7));
        verifyRootElementName(resultList, "element");
    }

    @Test
    public void testSurroundingJavaSplitting() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));
        splitter.setDocumentEventHandler(new XmlSurroundingNodeDocumentEventHandler(new QName("parentElement")));

        List<ByteArrayOutputStream> resultList = splitXml(splitter);

        assertThat(resultList.size(), is(7));
        verifyRootElementName(resultList, "parentElement");
    }

    @Test
    public void testSurroundingJavaSplitterWithGlobalValues() throws IOException, XmlSplitException {
        InMemoryStaxNodeSplitter splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName("element"));
        splitter.setDocumentEventHandler(new XmlSurroundingNodeDocumentEventHandler(new QName("parentElement")));

        List<ByteArrayOutputStream> resultList = splitXml(splitter);

        assertThat(resultList.size(), is(7));
        System.out.println(new String(resultList.get(0).toByteArray()));

        verifyRootElementName(resultList, "parentElement");
        //TODO: define test cases

    }

    private List<ByteArrayOutputStream> splitXml(InMemoryStaxNodeSplitter splitter)
            throws XmlSplitException, IOException {
        try (InputStream is = JavaXmlSplitter.class.getResourceAsStream("/xml/generatedTestOutput.xml")) {
            splitter.split("simpleElementSplit", is);
        }

        return splitter.getResultList();
    }

    private void verifyRootElementName(List<ByteArrayOutputStream> resultList, String rootElementName) {
        for (ByteArrayOutputStream baos : resultList) {
            String xml = new String(baos.toByteArray());
            assertThat(xml, hasXPath("name(/*)", equalTo(rootElementName)));
        }
    }
}
