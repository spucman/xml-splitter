package com.github.spuchmann.xml.splitter.stax;

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XmlTestFileGenerationHelperTest {

    private XmlTestFileGenerationHelper generationHelper = new XmlTestFileGenerationHelper();

    @Test
    public void generateTestXml() throws XMLStreamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        generationHelper.generateTestXml(os, 7);
        Diff diff = DiffBuilder.compare(os.toByteArray())
                .withTest(getResourceAsStream("/xml/generatedTestOutput.xml"))
                .checkForSimilar()
                .ignoreWhitespace()
                .build();

        assertThat(diff.hasDifferences(), is(false));
    }

    @Test
    public void generateSimpleElementAsDocument() throws XMLStreamException {
        String generatedXml = generationHelper.generateSimpleElementAsDocument(5);

        Diff diff = DiffBuilder.compare(generatedXml)
                .withTest(getResourceAsStream("/xml/generatedTestOutputSingleElement.xml"))
                .checkForSimilar()
                .ignoreWhitespace()
                .build();

        assertThat(diff.hasDifferences(), is(false));
    }

    private InputStream getResourceAsStream(String resourcePath) {
        return XmlTestFileGenerationHelperTest.class.getResourceAsStream(resourcePath);
    }
}