package com.github.spuchmann.xml.splitter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public interface XmlDocumentEventHandler {

    void afterStartDocument(XMLStreamWriter streamWriter) throws XMLStreamException;

    void beforeEndDocument(XMLStreamWriter streamWriter) throws XMLStreamException;

    void finishedDocument();
}
