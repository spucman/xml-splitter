package com.github.spuchmann.xml.splitter.stax;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


public class SplitContextBuilderTest {

    private static final String BASENAME = "basename";

    private static final String ENCODING = "encoding";

    private static final int CURRENT_COUNT = 5;

    private static final Map<QName, String> COLLECTED_DATA = createCollectedData();

    private static Map<QName, String> createCollectedData() {
        Map<QName, String> map = new HashMap<>();
        map.put(new QName("test"), "test");
        map.put(new QName("test2"), "test2");
        return map;
    }

    @Test
    public void testBuild() {
        SplitContextBuilder builder = SplitContextBuilder.newBuilder()
                .basename(BASENAME)
                .currentCount(CURRENT_COUNT)
                .encoding(ENCODING)
                .collectedData(COLLECTED_DATA);

        SplitContext context = builder.build();
        verify(context);

        COLLECTED_DATA.put(new QName("test3"), "test3");
        SplitContext anotherContext = builder.build();
        assertThat(context, is(not(anotherContext)));
        verify(anotherContext);
        assertThat(context.getCollectedData().size(), is(2));
    }

    private void verify(SplitContext splitContext) {
        assertThat(splitContext.getBasename(), is(BASENAME));
        assertThat(splitContext.getCurrentCount(), is(CURRENT_COUNT));
        assertThat(splitContext.getEncoding(), is(ENCODING));
        assertThat(splitContext.getCollectedData(), is(notNullValue()));
        assertThat(splitContext.getCollectedData().size(), is(COLLECTED_DATA.size()));
    }
}