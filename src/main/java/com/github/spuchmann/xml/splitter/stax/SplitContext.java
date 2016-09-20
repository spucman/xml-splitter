package com.github.spuchmann.xml.splitter.stax;

/**
 * internal class for providing the current splitting state
 *
 * @since 0.1.0
 */
public class SplitContext {

    private String basename;

    private int currentCount;

    private String encoding;

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
}
