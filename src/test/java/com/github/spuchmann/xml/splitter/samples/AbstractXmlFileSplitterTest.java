package com.github.spuchmann.xml.splitter.samples;

import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.XmlSplitStatistic;
import com.github.spuchmann.xml.splitter.XmlSplitter;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractXmlFileSplitterTest {

    protected void splitXml(XmlSplitter splitter, File outputDir)
            throws XmlSplitException, IOException {
        XmlSplitStatistic statistic;

        try (InputStream is = JavaXmlFileSplitterTest.class.getResourceAsStream("/xml/generatedTestOutput.xml")) {
            statistic = splitter.split("simpleElementSplit", is);
        }

        assertThat(statistic.getCount(), is(7));

        assertThat(outputDir.list().length, is(7));

        for (File file : outputDir.listFiles()) {
            System.out.println("\nFilename: " + file.getName());
            System.out.println(Files.toString(file, Charsets.UTF_8));
        }
    }
}
