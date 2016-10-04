package com.github.spuchmann.xml.splitter.stax;

import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static com.github.spuchmann.xml.splitter.stax.XmlTestFileGenerationHelper.LIST_ELEMENT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StaxNodeSplitterTest {

    private XmlTestFileGenerationHelper xmlGenerator = new XmlTestFileGenerationHelper();

    private InMemoryStaxNodeSplitter splitter;

    @Before
    public void before() {
        splitter = new InMemoryStaxNodeSplitter();
        splitter.setSplittingNodeName(new QName(LIST_ELEMENT));
    }

    @Test
    public void testSplit() throws XMLStreamException, XmlSplitException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int amountOfElements = 5;

        xmlGenerator.generateTestXml(baos, amountOfElements);

        XmlSplitStatistic splitStatistic = splitter.split("test1", new ByteArrayInputStream(baos.toByteArray()));

        assertThat(splitter.getResultList().size(), is(amountOfElements));
        assertThat(splitStatistic.getCount(), is(amountOfElements));

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

    @Test
    public void testSplitEventHandler() throws XMLStreamException, XmlSplitException {
        XmlDocumentEventHandler eventHandler = spy(XmlDocumentEventHandler.class);
        splitter.setDocumentEventHandler(eventHandler);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int amountOfElements = 3;

        xmlGenerator.generateTestXml(baos, amountOfElements);

        XmlSplitStatistic splitStatistic = splitter.split("test1", new ByteArrayInputStream(baos.toByteArray()));

        assertThat(splitter.getResultList().size(), is(amountOfElements));
        assertThat(splitStatistic.getCount(), is(amountOfElements));
        verify(eventHandler, times(amountOfElements))
                .afterStartDocument(any(XMLStreamWriter.class), any(SplitContext.class));
        verify(eventHandler, times(amountOfElements)).beforeEndDocument(any(XMLStreamWriter.class));
        verify(eventHandler, times(amountOfElements)).finishedDocument();
    }

    @Test
    public void testCollectGlobalData() throws XMLStreamException, XmlSplitException {
        TestContextSavingDocumentEventHandler handler = new TestContextSavingDocumentEventHandler();
        splitter.setDocumentEventHandler(handler);

        List<QName> collectDataList = new ArrayList<>();
        collectDataList.add(new QName("global"));
        collectDataList.add(new QName("global1"));
        splitter.setGlobalDataCollectorNameList(collectDataList);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int amountOfElements = 3;

        xmlGenerator.generateTestXml(baos, amountOfElements);

        splitter.split("test1", new ByteArrayInputStream(baos.toByteArray()));

        Map<QName, String> collectedData = handler.getSplitContext().getCollectedData();

        assertThat(collectedData.size(), is(2));
        assertThat(collectedData.get(new QName("global1")), is("globalValue1"));
    }
}