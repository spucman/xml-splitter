package com.github.spuchmann.xml.splitter.stax;

import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import static com.github.spuchmann.xml.splitter.stax.XmlTestFileGenerationHelper.LIST_ELEMENT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FileStaxNodeSplitterTest {

    private static final Logger log = LoggerFactory.getLogger(FileStaxNodeSplitterTest.class);

    private FileStaxNodeSplitter splitter;

    private XmlTestFileGenerationHelper xmlGenerator = new XmlTestFileGenerationHelper();

    @Before
    public void before() {
        splitter = new FileStaxNodeSplitter("target/testOutput");
        splitter.setSplittingNodeName(new QName(LIST_ELEMENT));
        new File(splitter.getOutputFolder()).mkdirs();
    }

    @After
    public void after() throws IOException {
        delete(new File(splitter.getOutputFolder()));
    }

    @Test
    public void testCreateFilename() {
        assertThat(splitter.createFilename("basename", "x"), is("basename_x.xml"));
        assertThat(splitter.createFilename("basename", "1"), is("basename_1.xml"));
    }

    @Test
    public void testCreateFile() {
        File outFile = splitter.createFile(new SplitContext("basename", 3));
        assertThat(outFile.getName(), is("basename_3.xml"));
        assertThat(outFile.getPath(), is("target/testOutput/basename_3.xml"));
    }

    @Test
    public void testSplit() throws XMLStreamException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int amountOfElements = 5;

        xmlGenerator.generateTestXml(baos, amountOfElements);

        XmlSplitStatistic splitStatistic = splitter.split("test1", new ByteArrayInputStream(baos.toByteArray()));

        assertThat(splitStatistic.getCount(), is(amountOfElements));

        File[] generatedFiles = new File(splitter.getOutputFolder()).listFiles();
        assertThat(generatedFiles.length, is(amountOfElements));

        for (int i = 0; i < generatedFiles.length; i++) {
            String generatedXml = Files.toString(generatedFiles[i], Charsets.UTF_8);
            Diff diff = DiffBuilder.compare(generatedXml)
                    .withTest(xmlGenerator.generateSimpleElementAsDocument(i))
                    .checkForSimilar()
                    .ignoreWhitespace()
                    .build();
            assertThat(diff.toString(), diff.hasDifferences(), is(false));
        }
    }

    private void delete(File file) throws IOException {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delete(subFile);
            }
        }

        if (!file.delete()) {
            log.warn("Couldn't delete file: {}", file.getName());
        }
    }
}