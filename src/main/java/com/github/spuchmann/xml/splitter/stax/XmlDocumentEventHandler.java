package com.github.spuchmann.xml.splitter.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * event handler which provides hooks inside the splitting process
 *
 * @since 0.1.0
 */
public interface XmlDocumentEventHandler {

    /**
     * will be called after the document and the xml header is written
     */
    void afterStartDocument(XMLStreamWriter streamWriter) throws XMLStreamException;

    /**
     * will be called before the document ends and will be closed
     */
    void beforeEndDocument(XMLStreamWriter streamWriter) throws XMLStreamException;

    /**
     * will be called if the document is closed
     */
    void finishedDocument();
}
