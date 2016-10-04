package com.github.spuchmann.xml.splitter.stax;

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * internal class for providing the current splitting state
 *
 * @since 0.1.0
 */
public class SplitContext {

    private String basename;

    private int currentCount;

    private String encoding;

    private Map<QName, String> collectedData;

    public SplitContext() {

    }

    public SplitContext(String basename, int currentCount) {
        this.basename = basename;
        this.currentCount = currentCount;
    }

    public SplitContext(String basename, int currentCount, String encoding) {
        this(basename, currentCount);
        this.encoding = encoding;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<QName, String> getCollectedData() {
        return collectedData;
    }

    public void setCollectedData(Map<QName, String> collectedData) {
        this.collectedData = collectedData;
    }
}
