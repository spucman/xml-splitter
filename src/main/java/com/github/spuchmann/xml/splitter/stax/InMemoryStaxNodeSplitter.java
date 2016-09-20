package com.github.spuchmann.xml.splitter.stax;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * in memory split storage. every fragment will be stored inside the memory.
 * <b>Hint: </b> Make sure you have configured enough memory or just use the FileStaxNodeSplitter
 *
 * @since 0.1.0
 */
public class InMemoryStaxNodeSplitter extends StaxNodeSplitter {

    private List<ByteArrayOutputStream> baosList = new ArrayList<>();

    @Override
    protected XMLStreamWriter createNewStreamWriter(SplitContext context) throws XMLStreamException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baosList.add(baos);
        return getOutputFactory().createXMLStreamWriter(baos);
    }

    @Override
    protected void closeInternalStream() {
        //baos don't needed to be closed
    }

    public List<ByteArrayOutputStream> getResultList() {
        return baosList;
    }

    /**
     * resets the whole internal storage
     */
    public void resetResultList() {
        baosList = new ArrayList<>();
    }
}
