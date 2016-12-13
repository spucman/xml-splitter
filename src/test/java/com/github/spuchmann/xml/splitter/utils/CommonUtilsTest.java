package com.github.spuchmann.xml.splitter.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.spuchmann.xml.splitter.utils.CommonUtils.isNotNullOrEmpty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CommonUtilsTest {

    @Test
    public void testIsNotNullOrEmptyString() {
        assertThat(isNotNullOrEmpty((String) null), is(false));
        assertThat(isNotNullOrEmpty(""), is(false));
        assertThat(isNotNullOrEmpty("test"), is(true));
    }

    @Test
    public void testIsNotNullOrEmptyList() {
        assertThat(isNotNullOrEmpty((List<String>) null), is(false));
        assertThat(isNotNullOrEmpty(new ArrayList<String>()), is(false));
        assertThat(isNotNullOrEmpty(Lists.newArrayList("test")), is(true));
    }

    @Test
    public void testIsNotNullOrEmptyStringMap() {
        assertThat(isNotNullOrEmpty((Map<String, String>) null), is(false));
        assertThat(isNotNullOrEmpty(new HashMap<String, String>()), is(false));
        Map<String, String> testMap = new HashMap<>();
        testMap.put("test", "test");
        assertThat(isNotNullOrEmpty(testMap), is(true));
    }
}