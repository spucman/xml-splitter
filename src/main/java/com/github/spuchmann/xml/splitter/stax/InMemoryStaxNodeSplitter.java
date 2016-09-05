package com.github.spuchmann.xml.splitter.stax;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class InMemoryStaxNodeSplitter extends StaxNodeSplitter {

    private List<ByteArrayOutputStream> baosList = new ArrayList<>();

    @Override
    protected XMLStreamWriter createNewStreamWriter(SplitContext context) throws XMLStreamException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baosList.add(baos);
        return getOutputFactory().createXMLStreamWriter(baos);
    }

    public List<ByteArrayOutputStream> getResultList() {
        return baosList;
    }

    public void resetResultList() {
        baosList = new ArrayList<>();
    }
}
