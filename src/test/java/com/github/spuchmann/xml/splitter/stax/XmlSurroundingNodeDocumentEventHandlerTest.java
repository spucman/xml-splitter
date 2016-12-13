package com.github.spuchmann.xml.splitter.stax;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static com.github.spuchmann.xml.splitter.stax.StaxXmlTestHelper.closeDocument;
import static com.github.spuchmann.xml.splitter.stax.StaxXmlTestHelper.createStreamWriter;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlSurroundingNodeDocumentEventHandlerTest {

    private static final String RESULT_WITHOUT_NAMESPACE = "<?xml version='1.0' encoding='UTF-8'?><root><node/></root>";

    private static final String RESULT_WITH_DEFAULT_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><node xmlns=\"http://test.namespace\"/></root>";

    private static final String RESULT_WITH_NAMESPACE =
            "<?xml version='1.0' encoding='UTF-8'?><root><nr:node xmlns:nr=\"http://test.namespace\"/></root>";

    private XmlSurroundingNodeDocumentEventHandler eventHandler;

    @Before
    public void before() {
        eventHandler = new XmlSurroundingNodeDocumentEventHandler();
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
}

