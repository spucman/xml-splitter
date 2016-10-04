package com.github.spuchmann.xml.splitter.stax;

import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import com.github.spuchmann.xml.splitter.XmlSplitter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * basic xml splitter implementation which uses stax. As a splitting criteria node names can be configured and taken.
 *
 * To hook into the splitting process you can define an eventHandler {@link com.github.spuchmann.xml.splitter.stax.XmlDocumentEventHandler}
 *
 * @since 0.1.0
 */
public abstract class StaxNodeSplitter implements XmlSplitter {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    private XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    private XmlDocumentEventHandler documentEventHandler;

    private QName splittingNodeName;

    private XmlReadWriterMapper xmlReadWriterMapper = new XmlReadWriterMapper();

    private List<QName> globalDataCollectorNameList = new ArrayList<>();

    public XmlSplitStatistic split(String name, InputStream inputStream) throws XmlSplitException {
        try {
            return doSplit(name, inputStream);
        } catch (XMLStreamException e) {
            throw new XmlSplitException("Unable to split " + name, e);
        }
    }

    private XmlSplitStatistic doSplit(String name, InputStream inputStream) throws XMLStreamException {
        XmlSplitStatistic xmlSplitStatistic = new XmlSplitStatistic();

        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(inputStream);

        int count = 0;
        XMLStreamWriter streamWriter = null;
        String version = streamReader.getVersion();
        String encoding = streamReader.getEncoding();

        SplitContextBuilder splitContextBuilder = SplitContextBuilder.newBuilder()
                .basename(name)
                .encoding(encoding);

        Map<QName, String> globalDataCollector = new HashMap<>();
        boolean collect = false;
        QName currentName = null;
        
        while (streamReader.hasNext()) {
            streamReader.next();
            int event = streamReader.getEventType();
            switch (event) {
                case END_ELEMENT:
                    QName qName = streamReader.getName();
                    if (splittingNodeName.equals(qName)) {
                        closeStreamWriter(streamWriter);
                        streamWriter = null;
                        finishDocument();
                        count++;
                        break;
                    }
                case CHARACTERS:
                    if (collect) {
                        globalDataCollector.put(currentName, streamReader.getText());
                        currentName = null;
                        collect = false;
                    }
                    break;
                case START_ELEMENT:
                    qName = streamReader.getName();
                    if (splittingNodeName.equals(qName)) {
                        splitContextBuilder.currentCount(count)
                                .collectedData(globalDataCollector);
                        streamWriter = createNewStreamWriter(version, splitContextBuilder.build());
                    } else if (globalDataCollectorNameList.contains(qName)) {
                        currentName = qName;
                        collect = true;
                    }

                default:
            }
            if (streamWriter != null) {
                xmlReadWriterMapper.map(streamReader, streamWriter);
            }
        }

        xmlSplitStatistic.setEndTime(new Date());
        xmlSplitStatistic.setCount(count);
        return xmlSplitStatistic;
    }

    protected XMLStreamWriter createNewStreamWriter(String version, SplitContext context)
            throws XMLStreamException {
        XMLStreamWriter streamWriter = createNewStreamWriter(context);
        streamWriter.writeStartDocument(context.getEncoding(), version);

        if (documentEventHandler != null) {
            documentEventHandler.afterStartDocument(streamWriter, context);
        }

        return streamWriter;
    }

    /**
     * provides a new XmlStreamWriter for the processing
     */
    protected abstract XMLStreamWriter createNewStreamWriter(SplitContext context) throws XMLStreamException;

    protected void closeStreamWriter(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEndElement();

        if (documentEventHandler != null) {
            documentEventHandler.beforeEndDocument(writer);
        }

        writer.writeEndDocument();
        writer.close();
        closeInternalStream();
    }

    protected abstract void closeInternalStream();

    private void finishDocument() {
        if (documentEventHandler != null) {
            documentEventHandler.finishedDocument();
        }
    }

    protected XMLOutputFactory getOutputFactory() {
        return outputFactory;
    }

    public XmlDocumentEventHandler getDocumentEventHandler() {
        return documentEventHandler;
    }

    public void setDocumentEventHandler(XmlDocumentEventHandler documentEventHandler) {
        this.documentEventHandler = documentEventHandler;
    }

    public QName getSplittingNodeName() {
        return splittingNodeName;
    }

    public void setSplittingNodeName(QName splittingNodeName) {
        this.splittingNodeName = splittingNodeName;
    }

    public void setXmlReadWriterMapper(XmlReadWriterMapper xmlReadWriterMapper) {
        this.xmlReadWriterMapper = xmlReadWriterMapper;
    }

    public List<QName> getGlobalDataCollectorNameList() {
        return globalDataCollectorNameList;
    }

    /**
     * sets the QNames of all nodes where data should be collected from - data can only collected outside from split
     * nodes
     */
    public void setGlobalDataCollectorNameList(List<QName> globalDataCollectorNameList) {
        this.globalDataCollectorNameList = globalDataCollectorNameList;
    }
}
