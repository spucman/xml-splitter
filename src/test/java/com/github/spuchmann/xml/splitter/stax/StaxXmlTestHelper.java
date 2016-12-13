package com.github.spuchmann.xml.splitter.stax;

import com.google.common.base.Charsets;

import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * helper class which helps testing xml files
 *
 * @since 0.3.0
 */
public final class StaxXmlTestHelper {

    private static XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    private StaxXmlTestHelper() {

    }

    public static XMLStreamWriter createStreamWriter(OutputStream os) throws XMLStreamException {
        XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(os);

        streamWriter.writeStartDocument(Charsets.UTF_8.name(), "1.0");
        streamWriter.writeStartElement("root");
        return streamWriter;
    }

    public static void closeDocument(XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
        streamWriter.writeEndDocument();
    }
}
