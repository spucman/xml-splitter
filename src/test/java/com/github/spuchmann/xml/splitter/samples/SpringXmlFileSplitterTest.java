package com.github.spuchmann.xml.splitter.samples;

import com.github.spuchmann.xml.splitter.XmlSplitException;
import com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/filesplitter.beans.xml"})
public class SpringXmlFileSplitterTest extends AbstractXmlFileSplitterTest {

    @Autowired
    private FileStaxNodeSplitter simpleFileStaxNodeSplitter;

    @Autowired
    private FileStaxNodeSplitter surroundingFileStaxNodeSplitter;

    @Autowired
    private FileStaxNodeSplitter globalValueFileStaxSplitter;

    @Test
    public void testSimpleJavaSplittingWithFiles() throws IOException, XmlSplitException {
        File outputFolder = new File(simpleFileStaxNodeSplitter.getOutputFolder());

        splitXml(simpleFileStaxNodeSplitter, outputFolder);
    }

    @Test
    public void testSurroundingJavaSplitting() throws IOException, XmlSplitException {
        File outputFolder = new File(surroundingFileStaxNodeSplitter.getOutputFolder());

        System.out.println("\n\n\n******************************");
        System.out.println("Splitting Xml With surrounding node");
        System.out.println("******************************");

        splitXml(surroundingFileStaxNodeSplitter, outputFolder);
    }

    @Test
    public void testSurroundingJavaSplitterWithGlobalValues() throws IOException, XmlSplitException {
        File outputFolder = new File(globalValueFileStaxSplitter.getOutputFolder());

        System.out.println("\n\n\n******************************");
        System.out.println("Splitting Xml With Global Values");
        System.out.println("******************************");
        splitXml(globalValueFileStaxSplitter, outputFolder);
    }
}
