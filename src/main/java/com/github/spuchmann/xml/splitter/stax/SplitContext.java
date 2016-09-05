package com.github.spuchmann.xml.splitter.stax;


public class SplitContext {

    private String basename;

    private int currentCount;

    public SplitContext() {

    }

    public SplitContext(String basename, int currentCount) {
        this.basename = basename;
        this.currentCount = currentCount;
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
}
