package com.github.spuchmann.xml.splitter;

import java.io.InputStream;

/**
 * basic interface for splitting an xml
 *
 * @since 1.0.0
 */
public interface XmlSplitter {

    /**
     * main trigger for the splitting action
     *
     * @param name
     *         defines the name of the splitting operation (e.g. a filename or a session name)
     * @param inputStream
     *         xml source
     * @return basic statistic object
     */
    XmlSplitStatistic split(String name, InputStream inputStream) throws XmlSplitException;
}
