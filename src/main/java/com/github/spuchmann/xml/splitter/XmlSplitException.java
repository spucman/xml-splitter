package com.github.spuchmann.xml.splitter;

/**
 * root exception for all split operations
 *
 * @since 1.0.0
 */
public class XmlSplitException extends Exception {

    public XmlSplitException() {
    }

    public XmlSplitException(String message) {
        super(message);
    }

    public XmlSplitException(String message, Throwable cause) {
        super(message, cause);
    }
}
