package com.github.spuchmann.xml.splitter.stax;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * internal class for creating a split state
 *
 * @since 0.2.0
 */
public final class SplitContextBuilder {

    private String basename;

    private int currentCount;

    private String encoding;

    private Map<QName, String> collectedData;

    private SplitContextBuilder() {
    }

    public static SplitContextBuilder newBuilder() {
        return new SplitContextBuilder();
    }

    public SplitContext build() {
        SplitContext splitContext = new SplitContext();
        splitContext.setBasename(basename);
        splitContext.setEncoding(encoding);
        splitContext.setCurrentCount(currentCount);
        splitContext.setCollectedData(new HashMap<>(collectedData));
        return splitContext;
    }

    public SplitContextBuilder basename(String basename) {
        this.basename = basename;
        return this;
    }

    public SplitContextBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public SplitContextBuilder currentCount(int currentCount) {
        this.currentCount = currentCount;
        return this;
    }

    public SplitContextBuilder collectedData(Map<QName, String> collectedData) {
        this.collectedData = collectedData;
        return this;
    }

}
