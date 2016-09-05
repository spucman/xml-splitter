package com.github.spuchmann.xml.splitter.stax;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlReadWriterMapperTest {

    private XmlReadWriterMapper mapper = new XmlReadWriterMapper();

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    private XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    @Test
    public void testMapSimpleTest() throws IOException, XMLStreamException {
        doTestXml("/xml/simpleTest.xml");
    }

    @Test
    public void testMapNamespacesTest() throws IOException, XMLStreamException {
        doTestXml("/xml/differentNamespacesTest.xml");
    }

    private void doTestXml(String resourcePath) throws IOException, XMLStreamException {
        try (InputStream is = getResourceAsStream(resourcePath)) {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(is);
            XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(os);

            do {
                mapper.map(streamReader, streamWriter);

                streamReader.next();
            } while (streamReader.hasNext());

            mapper.map(streamReader, streamWriter);

            String generatedXml = new String(os.toByteArray());
            assertThat(generatedXml, isSimilarTo(getResourceAsStream(resourcePath)));
        }
    }

    private InputStream getResourceAsStream(String resourcePath) {
        return XmlReadWriterMapperTest.class.getResourceAsStream(resourcePath);
    }
}