package com.github.spuchmann.xml.splitter.stax;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlTestFileGenerationHelper {

    protected static final String ROOT_ELEMENT = "listElement";

    protected static final String LIST_ELEMENT = "element";

    private static final String[] SUB_ELEMENTS = {"name", "address", "to", "from", "email", "other", "stuff"};

    private XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    public void generateTestXml(OutputStream os, int amountOfElements) throws XMLStreamException {
        XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(os);

        streamWriter.writeStartDocument("UTF-8", "1.0");
        streamWriter.writeStartElement(ROOT_ELEMENT);

        writeElement(streamWriter, "global", "globalValue");
        writeElement(streamWriter, "global1", "globalValue1");

        for (int i = 0; i < amountOfElements; i++) {
            writeElement(streamWriter, i);
        }

        streamWriter.writeEndElement();
        streamWriter.writeEndDocument();
        streamWriter.flush();
        streamWriter.close();
    }

    public String generateSimpleElementAsDocument(int elementId) throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(os);

        streamWriter.writeStartDocument("UTF-8", "1.0");
        writeElement(streamWriter, elementId);
        streamWriter.writeEndDocument();
        streamWriter.flush();
        streamWriter.close();
        return new String(os.toByteArray());
    }

    private void writeElement(XMLStreamWriter streamWriter, int elementCount) throws XMLStreamException {
        streamWriter.writeStartElement(LIST_ELEMENT);
        streamWriter.writeAttribute("id", Integer.toString(elementCount));
        for (String elementName : SUB_ELEMENTS) {
            writeElement(streamWriter, elementName, elementName + elementCount);
        }
        streamWriter.writeEndElement();
    }

    private void writeElement(XMLStreamWriter streamWriter, String tagName, String value) throws XMLStreamException {
        streamWriter.writeStartElement(tagName);
        streamWriter.writeCharacters(value);
        streamWriter.writeEndElement();
    }
}
