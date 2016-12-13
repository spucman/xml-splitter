package com.github.spuchmann.xml.splitter.stax;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static com.github.spuchmann.xml.splitter.stax.StaxXmlTestHelper.closeDocument;
import static com.github.spuchmann.xml.splitter.stax.StaxXmlTestHelper.createStreamWriter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

public class StaxXmlHelperTest {

    private static final String RESULT_WITHOUT_VALUE = "<?xml version='1.0' encoding='UTF-8'?><root/>";

    private static final String RESULT_WITHOUT_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><tag>value</tag></root>";

    private static final String RESULT_WITH_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><ns:tag xmlns:ns=\"http://test.uri\">value</ns:tag></root>";

    private static final String RESULT_WITH_TAG_ONLY = "<?xml version='1.0' encoding='UTF-8'?><root><tag/></root>";

    private static final String RESULT_WITH_TAG_INCL_NS =
            "<?xml version='1.0' encoding='UTF-8'?><root><ns:tag xmlns:ns=\"http://test.uri\"/></root>";

    @Test
    public void testWriteXmlElementWithNamespace() throws XMLStreamException {
        testWriteXmlElementWithNamespace(new QName("tag"), null, RESULT_WITHOUT_VALUE);
        testWriteXmlElementWithNamespace(new QName("tag"), "", RESULT_WITHOUT_VALUE);
        testWriteXmlElementWithNamespace(new QName("tag"), "value", RESULT_WITHOUT_NAMESPACE);
        testWriteXmlElementWithNamespace(new QName("http://test.uri", "tag", "ns"), "value", RESULT_WITH_NAMESPACE);
    }

    @Test
    public void testWriteXmlStartElementWithNamespace() throws XMLStreamException {
        testWriteXmlStartElementWithNamespace(new QName("tag"), RESULT_WITH_TAG_ONLY);
        testWriteXmlStartElementWithNamespace(new QName("http://test.uri", "tag", "ns"), RESULT_WITH_TAG_INCL_NS);
        testWriteXmlStartElementWithNamespace(new QName("http://test.uri", "tag"), RESULT_WITH_TAG_INCL_NS);
    }

    @Test
    public void testWriteXmlNamespace() throws XMLStreamException{
        String xml = testWriteXmlNamespace(new QName("http://test.uri", "tag"), "<?xml version='1.0' encoding='UTF-8'?><root xmlns=\"http://test.uri\"/>");
        //TODO: verify
        assertThat(xml, hasXPath("namespace-uri(/*)", equalTo("http://test.uri")));
    }

    private void testWriteXmlElementWithNamespace(QName node, String value, String result) throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLStreamWriter writer = createStreamWriter(os);
        StaxXmlHelper.writeXmlElementWithNamespace(writer, node, value);
        closeDocument(writer);
        assertThat(new String(os.toByteArray()), isSimilarTo(result));
    }

    private void testWriteXmlStartElementWithNamespace(QName node, String result) throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLStreamWriter writer = createStreamWriter(os);
        StaxXmlHelper.writeXmlStartElementWithNamespace(writer, node);
        writer.writeEndElement();
        closeDocument(writer);
        assertThat(new String(os.toByteArray()), isSimilarTo(result));
    }

    private String testWriteXmlNamespace(QName node, String result) throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLStreamWriter writer = createStreamWriter(os);
        StaxXmlHelper.writeXmlNamespace(writer, node);
        closeDocument(writer);
        String xml = new String(os.toByteArray());
        System.out.println(xml);
        assertThat(xml, isSimilarTo(result));
        return xml;
    }
}