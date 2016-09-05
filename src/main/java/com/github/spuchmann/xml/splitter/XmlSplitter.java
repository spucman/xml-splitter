package com.github.spuchmann.xml.splitter;

import com.github.spuchmann.xml.splitter.XmlSplitStatistic;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

public interface XmlSplitter {

    XmlSplitStatistic split(String name, InputStream inputStream) throws XMLStreamException;
}
