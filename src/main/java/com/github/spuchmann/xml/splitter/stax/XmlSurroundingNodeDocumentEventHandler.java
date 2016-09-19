package com.github.spuchmann.xml.splitter.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Surrounds the splitted xml fragment with the given node
 *
 * @since 1.0.0
 */
public class XmlSurroundingNodeDocumentEventHandler implements XmlDocumentEventHandler {

    private QName node;

    public XmlSurroundingNodeDocumentEventHandler() {
    }

    public XmlSurroundingNodeDocumentEventHandler(QName node) {
        this.node = node;
    }

    @Override
    public void afterStartDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartElement(node.getPrefix(), node.getLocalPart(), node.getNamespaceURI());

        if (isNotNullOrEmpty(node.getNamespaceURI())) {
            writeNamespace(streamWriter, node);
        }
    }

    private void writeNamespace(XMLStreamWriter streamWriter, QName node) throws XMLStreamException {
        if (isNotNullOrEmpty(node.getPrefix())) {
            streamWriter.writeNamespace(node.getPrefix(), node.getNamespaceURI());
        } else {
            streamWriter.writeDefaultNamespace(node.getNamespaceURI());
        }
    }

    @Override
    public void beforeEndDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
    }

    @Override
    public void finishedDocument() {

    }

    protected boolean isNotNullOrEmpty(String value) {
        return value != null ? !value.isEmpty() : false;
    }

    public QName getNode() {
        return node;
    }

    public void setNode(QName node) {
        this.node = node;
    }
}
