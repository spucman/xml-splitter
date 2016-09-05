package com.github.spuchmann.xml.splitter;

import java.util.Date;

public class XmlSplitStatistic {

    private int count;

    private Date startTime;

    private Date endTime;

    public XmlSplitStatistic() {
        this.startTime = new Date();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
