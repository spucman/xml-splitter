package com.github.spuchmann.xml.splitter.stax;

import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InMemoryStaxNodeSplitterTest {

    private InMemoryStaxNodeSplitter nodeSplitter;

    @Before
    public void before() {
        nodeSplitter = new InMemoryStaxNodeSplitter();
    }

    @Test
    public void createNewStreamWriter() throws XMLStreamException {
        XMLStreamWriter writer = nodeSplitter.createNewStreamWriter(new SplitContext());
        assertThat(writer, is(notNullValue()));
        assertThat(nodeSplitter.getResultList().size(), is(1));
    }

    @Test
    public void getResultList() throws XMLStreamException {
        nodeSplitter.createNewStreamWriter(new SplitContext());
        assertThat(nodeSplitter.getResultList().size(), is(1));
        nodeSplitter.createNewStreamWriter(new SplitContext());
        assertThat(nodeSplitter.getResultList().size(), is(2));
    }

    @Test
    public void resetResultList() throws XMLStreamException {
        nodeSplitter.createNewStreamWriter(new SplitContext());
        assertThat(nodeSplitter.getResultList().size(), is(1));
        nodeSplitter.resetResultList();
        assertThat(nodeSplitter.getResultList().size(), is(0));
    }
}