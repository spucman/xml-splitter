package com.github.spuchmann.xml.splitter.stax;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlSurroundingNodeDocumentEventHandlerTest {

    private static final String RESULT_WITHOUT_NAMESPACE = "<?xml version='1.0' encoding='UTF-8'?><root><node/></root>";

    private static final String RESULT_WITH_DEFAULT_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><node xmlns=\"http://test.namespace\"/></root>";

    private static final String RESULT_WITH_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><nr:node xmlns:nr=\"http://test.namespace\"/></root>";

    private XmlSurroundingNodeDocumentEventHandler eventHandler;

    private XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    @Before
    public void before() {
        eventHandler = new XmlSurroundingNodeDocumentEventHandler();
    }

    @Test
    public void testIsNotNullOrEmpty() {
        assertThat(eventHandler.isNotNullOrEmpty(null), is(false));
        assertThat(eventHandler.isNotNullOrEmpty(""), is(false));
        assertThat(eventHandler.isNotNullOrEmpty("test"), is(true));
    }

    @Test
    public void testAfterStartBeforeEndDocument() throws XMLStreamException {
        testAfterStartBeforeEndDocument(new QName("node"), RESULT_WITHOUT_NAMESPACE);
        testAfterStartBeforeEndDocument(new QName("http://test.namespace", "node"), RESULT_WITH_DEFAULT_NAMESPACE);
        testAfterStartBeforeEndDocument(new QName("http://test.namespace", "node", "nr"), RESULT_WITH_NAMESPACE);
    }

    private void testAfterStartBeforeEndDocument(QName node, String result) throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLStreamWriter writer = createStreamWriter(os);
        eventHandler.setNode(node);
        eventHandler.afterStartDocument(writer, new SplitContext());
        eventHandler.beforeEndDocument(writer);
        closeDocument(writer);

        assertThat(new String(os.toByteArray()), isSimilarTo(result));
    }

    private XMLStreamWriter createStreamWriter(OutputStream os) throws XMLStreamException {
        XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(os);

        streamWriter.writeStartDocument("UTF-8", "1.0");
        streamWriter.writeStartElement("root");
        return streamWriter;
    }

    private void closeDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
        streamWriter.writeEndDocument();
    }
}

