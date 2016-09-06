package com.github.spuchmann.xml.splitter.stax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.SPACE;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * stax mapper which maps xml elements, content, attributes from a reader to a writer
 *
 * @since 1.0.0
 */
public class XmlReadWriterMapper {

    private static final Logger log = LoggerFactory.getLogger(XmlReadWriterMapper.class);

    public void map(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {

        int event = reader.getEventType();

        switch (event) {
            case START_ELEMENT:
                writeStartElement(reader, writer);
                break;
            case END_ELEMENT:
                writer.writeEndElement();
                break;

            case CHARACTERS:
                writer.writeCharacters(reader.getText());
                break;

            case COMMENT:
                writer.writeComment(reader.getText());
                break;

            case START_DOCUMENT:
                writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
                break;

            case END_DOCUMENT:
                writer.writeEndDocument();
                break;

            case CDATA:
                writer.writeCData(reader.getText());
                break;

            case SPACE:
                writer.writeCharacters(reader.getText());
                break;

            default:
                log.warn("Event {} is currently not supported", event);
        }
    }

    private void writeStartElement(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        QName qName = reader.getName();
        writer.writeStartElement(qName.getPrefix(),
                qName.getLocalPart(),
                qName.getNamespaceURI());
        writeNamespaces(reader, writer);
        writeAttributes(reader, writer);
    }

    private void writeNamespaces(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < reader.getNamespaceCount(); i++) {
            writer.writeNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
        }
    }

    private void writeAttributes(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            QName qAttrName = reader.getAttributeName(i);
            writer.writeAttribute(qAttrName.getPrefix(), qAttrName.getNamespaceURI(), qAttrName.getLocalPart(),
                    reader.getAttributeValue(i));
        }
    }
}
