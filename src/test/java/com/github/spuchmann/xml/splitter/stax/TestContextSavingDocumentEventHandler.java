package com.github.spuchmann.xml.splitter.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class TestContextSavingDocumentEventHandler implements XmlDocumentEventHandler {

    private SplitContext splitContext;

    @Override
    public void afterStartDocument(XMLStreamWriter streamWriter, SplitContext context) throws XMLStreamException {
        this.splitContext = context;
    }

    @Override
    public void beforeEndDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
    }

    @Override
    public void finishedDocument() {
    }

    public SplitContext getSplitContext() {
        return splitContext;
    }
}
