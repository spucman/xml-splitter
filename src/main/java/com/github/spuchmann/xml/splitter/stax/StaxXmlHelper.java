package com.github.spuchmann.xml.splitter.stax;


import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static com.github.spuchmann.xml.splitter.utils.CommonUtils.isNotNullOrEmpty;

/**
 * Helper class which provides simple snippets to make the life with stax easier
 *
 * @since 0.2.0
 */
public final class StaxXmlHelper {

    private StaxXmlHelper() {
    }

    /**
     * writes a complete xmlelement if the QName and the value is present
     */
    public static void writeXmlElementWithNamespace(XMLStreamWriter streamWriter, QName node, String value)
            throws XMLStreamException {
        if (node != null && isNotNullOrEmpty(value)) {
            writeXmlStartElementWithNamespace(streamWriter, node);
            streamWriter.writeCharacters(value);
            streamWriter.writeEndElement();
        }
    }

    /**
     * writes a start tag with namespaces (if this exists)
     */
    public static void writeXmlStartElementWithNamespace(XMLStreamWriter streamWriter, QName node) throws
            XMLStreamException {
        streamWriter.writeStartElement(node.getPrefix(), node.getLocalPart(), node.getNamespaceURI());

        if (isNotNullOrEmpty(node.getNamespaceURI())) {
            writeXmlNamespace(streamWriter, node);
        }
    }

    /**
     * writes a xml namespace to a written xml start element tag
     */
    public static void writeXmlNamespace(XMLStreamWriter streamWriter, QName node) throws XMLStreamException {
        if (isNotNullOrEmpty(node.getPrefix())) {
            streamWriter.writeNamespace(node.getPrefix(), node.getNamespaceURI());
        } else {
            streamWriter.writeDefaultNamespace(node.getNamespaceURI());
        }
    }
}
