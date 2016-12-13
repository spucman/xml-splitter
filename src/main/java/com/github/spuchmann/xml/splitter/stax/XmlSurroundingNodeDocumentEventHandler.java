package com.github.spuchmann.xml.splitter.stax;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static com.github.spuchmann.xml.splitter.stax.StaxXmlHelper.writeXmlElementWithNamespace;
import static com.github.spuchmann.xml.splitter.stax.StaxXmlHelper.writeXmlStartElementWithNamespace;
import static com.github.spuchmann.xml.splitter.utils.CommonUtils.isNotNullOrEmpty;

/**
 * Surrounds the splitted xml fragment with the given node
 *
 * @since 0.1.0
 */
public class XmlSurroundingNodeDocumentEventHandler implements XmlDocumentEventHandler {

    private QName node;

    private List<QName> globalValueList;

    public XmlSurroundingNodeDocumentEventHandler() {
    }

    public XmlSurroundingNodeDocumentEventHandler(QName node) {
        this.node = node;
    }

    @Override
    public void afterStartDocument(XMLStreamWriter streamWriter, SplitContext context) throws XMLStreamException {
        writeGlobalValuesIfNecessary(streamWriter, context);
        writeXmlStartElementWithNamespace(streamWriter, node);
    }

    private void writeGlobalValuesIfNecessary(XMLStreamWriter streamWriter, SplitContext context)
            throws XMLStreamException {
        Map<QName, String> collectedData = context.getCollectedData();
        if (isNotNullOrEmpty(globalValueList) && isNotNullOrEmpty(collectedData)) {
            for (QName qName : globalValueList) {
                String value = collectedData.get(qName);
                writeXmlElementWithNamespace(streamWriter, qName, value);
            }
        }
    }

    @Override
    public void beforeEndDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
    }

    @Override
    public void finishedDocument() {

    }

    /**
     * @since 0.2.0
     */
    public void setGlobalValueList(List<QName> globalValueList) {
        this.globalValueList = globalValueList;
    }

    public QName getNode() {
        return node;
    }

    public void setNode(QName node) {
        this.node = node;
    }
}
