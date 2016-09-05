package com.github.spuchmann.xml.splitter.stax;

import org.junit.Before;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import static com.github.spuchmann.xml.splitter.stax.XmlTestFileGenerationHelper.LIST_ELEMENT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StaxNodeSplitterTest {

    private InMemoryStaxNodeSplitter splitter;

    @Before
    public void before() {
        splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName(LIST_ELEMENT));
    }

    @Test
    public void testSplit() throws XMLStreamException {
        XmlTestFileGenerationHelper xmlGenerator = new XmlTestFileGenerationHelper();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int amountOfElements = 5;

        xmlGenerator.generateTestXml(baos, amountOfElements);

        splitter.split("test1", new ByteArrayInputStream(baos.toByteArray()));

        assertThat(splitter.getResultList().size(), is(amountOfElements));

        for (int i = 0; i < amountOfElements; i++) {
            String generatedXml = new String(splitter.getResultList().get(i).toByteArray());
            Diff diff = DiffBuilder.compare(generatedXml)
                    .withTest(xmlGenerator.generateSimpleElementAsDocument(i))
                    .checkForSimilar()
                    .ignoreWhitespace()
                    .build();
            assertThat(diff.toString(), diff.hasDifferences(), is(false));


        }
    }
}