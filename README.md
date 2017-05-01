# xml-splitter
| Branch | Status |
| --- | --- |
| Master | [![Build Status](https://travis-ci.org/spucman/xml-splitter.svg?branch=master)](https://travis-ci.org/spucman/xml-splitter) |
| Develop | [![Build Status](https://travis-ci.org/spucman/xml-splitter.svg?branch=develop)](https://travis-ci.org/spucman/xml-splitter) |

XML Splitter is a small library which splits one xml into different parts.
As an example take a look at following xml structure:

```xml
<notes>
    <note scope="private">
        <to>Tove</to>
        <from>Jani</from>
        <heading>Reminder</heading>
        <body><![CDATA[Don't forget me this <b>weekend</b>!]]></body>
    </note>
    <note scope="public">
        <to>Pete</to>
        <from>Sarah</from>
        <heading>Urgent</heading>
        <body><![CDATA[Don't forget me this <b>weekend</b>!]]></body>
    </note>
</notes>
```
If you want to split up those list of notes in it different note element (e.g. to have them in separate files) you can 
just use this library.

Just look at the usage section to get a brief overview of how you can use xml-splitter.

## Latest release
The most recent release is xml-splitter 0.2.0, released May 01, 2017.

### Maven

```xml
<dependency>
    <groupId>com.github.spucman</groupId>
    <artifactId>xml-splitter</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Gradle

```gradle
compile 'com.github.spucman:xml-splitter:0.2.0'
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.spucman/xml-splitter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.spucman/xml-splitter)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

xml-splitter is compiled against JDK7+ and has only slf4j as a required dependency.
Nevertheless it is recommended to use woodstox as a stax implementation.

## Usage

At the moment you have two different choices to use xml-splitter. As a file based splitter
or as an in-memory splitter.
To use them transparent in your project just include the `XmlSplitter` interface with
one of the following implementations:

* `FileStaxNodeSpitter`
* `InMemoryStaxNodeSplitter`

In the future examples, we will just look into the `FileStaxNodeSplitter` because the 
in-memory sibling is much simpler ;).

For the upcoming samples we are assuming following xml structure: 
```xml
<listElement>
    <global>globalValue</global>
    <global1>globalValue1</global1>
    <element id="0">
        <name>name0</name>
        <address>address0</address>
        <to>to0</to>
        <from>from0</from>
        <email>email0</email>
        <other>other0</other>
        <stuff>stuff0</stuff>
    </element>
    <element id="1">
        <name>name1</name>
        <address>address1</address>
        ...
    </element>
    ...
</listElement>
```
We have a simple list of elements which also contains some arbitrary attributes of the top level.


### Java

#### <a name="simple-splitting"></a>Simple Splitting by node
If we want to split up the node sample from the beginning, we can just configure the xml-splitter
like this:

```java
FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();
fileStaxNodeSplitter.setOutputFolder("path/to/output/folder");
fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));

fileStaxNodeSplitter.init();

XmlSplitStatistic statistic;

try (InputStream is = JavaXmlSplitterTest.class.getResourceAsStream("/xml/testInput.xml")) {
    statistic = splitter.split("simpleElementSplit", is);
}
```
So what is happening up there?
1. you are instantiating a instance of a FileStaxNodeSplitter
2. after that you are defining where the result files should be stored
3. you are defining, which is the element where you want to split
4. you are initializing the `FileStaxNodeSplitter` and it will take care that the specified
folder is existing on your FileSystem
5. last but not least you are just calling split with your base target name 
and an `InputStream` of the XML structure.

The gernated output will look like following:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<element id="0">
    <name>name0</name>
    <address>address0</address>
    <to>to0</to>
    <from>from0</from>
    <email>email0</email>
    <other>other0</other>
    <stuff>stuff0</stuff>
</element>
```
The filename is simply the the number of the list element, so in this case the filename would
be `simpleElementSplit_0.xml`

#### <a name="splitting-with-surrounding-elements"></a>Simple Splitting by node with surrounding elements
If you want to use the same xml structure you used before you split you can also do that
in a very simple way:

```java
FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();

fileStaxNodeSplitter.setOutputFolder("path/to/output/folder");
fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));
fileStaxNodeSplitter.setDocumentEventHandler(new XmlSurroundingNodeDocumentEventHandler(new QName("parentElement")));

fileStaxNodeSplitter.init();
...
```
This sample looks very similar to the sample before just with one additional command `setDocumentEventHandler`.
With this hook you have a simple way to modify your output xml by placing custom stuff in following events:

* afterStartDocument
* beforeEndDocument
* finishedDocument

As one implementation there is a `XmlSurroundingNodeDocumentEventHandler` existing which simply takes
one QName and places it around the result.

Now our xml will look like this:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<parentElement>
    <element id="0">
        <name>name0</name>
        <address>address0</address>
        <to>to0</to>
        <from>from0</from>
        <email>email0</email>
        <other>other0</other>
        <stuff>stuff0</stuff>
    </element>
</parentElement>
```

#### <a name="splitting-with-global-values"></a>Simple Splitting by node with surrounding elements and global values
Sometimes you are in the mess that despite of splitting you must provide to each result element
additional information of a higher level in your xml tree.

```java
FileStaxNodeSplitter fileStaxNodeSplitter = new FileStaxNodeSplitter();
fileStaxNodeSplitter.setOutputFolder("path/to/output/folder");
fileStaxNodeSplitter.setSplittingNodeName(new QName("element"));
fileStaxNodeSplitter.setGlobalDataCollectorNameList(Lists.newArrayList(new QName("global"), new QName("global1")));

XmlSurroundingNodeDocumentEventHandler eventHandler = new XmlSurroundingNodeDocumentEventHandler();
eventHandler.setNode(new QName("root"));
eventHandler.setGlobalValueList(Lists.<QName>newArrayList(new QName("global1")));

fileStaxNodeSplitter.setDocumentEventHandler(eventHandler);

fileStaxNodeSplitter.init();
...
```
As before we defined our FileStaxNodeSplitter in the same way as we have done it before.
The difference from before is in the `setGlobalDataCollectorNameList` and the `setGlobalValueList` of 
the `FileStaxNodeSplitter` and the `XmlSurroundingNodeDocumentEventHandler`

At the `FileStaxNodeSplitter` you are defining which data you want to reuse later on (e.g. in one EventHanlder).
In the `XmlSurroundingNodeDocumentEventHandler` you are just defining, which data you have collected before hand
should be added to your result xml.

So if we are no looking at our new results it looks like following:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<parentElement>
    <global1>globalValue1</global1>
    <element id="0">
        <name>name0</name>
        <address>address0</address>
        <to>to0</to>
        <from>from0</from>
        <email>email0</email>
        <other>other0</other>
        <stuff>stuff0</stuff>
    </element>
</parentElement>
```

### Usage with Spring
This section will briefly covers the same configuration we have done before with java via spring xml files. Just for 
convenience we are defining the all xml node beforehand and will just be referenced in the different sections.

```xml
<bean id="globalValue" class="javax.xml.namespace.QName">
    <constructor-arg name="localPart" value="global"/>
</bean>

<bean id="globalValue1" class="javax.xml.namespace.QName">
    <constructor-arg name="localPart" value="global1"/>
</bean>

<bean id="tagElement" class="javax.xml.namespace.QName">
    <constructor-arg name="localPart" value="element"/>
</bean>

<bean id="tagParentElement" class="javax.xml.namespace.QName">
    <constructor-arg name="localPart" value="parentElement"/>
</bean>
```

#### Simple Splitting by node
For a detailed explanation of this sample look at the [java configuration section](#simple-splitting).

```xml
<bean id="simpleFileStaxNodeSplitter" class="com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter" init-method="init">
    <property name="outputFolder" value="#{ T(com.google.common.io.Files).createTempDir().getAbsolutePath() }"/>
    <property name="splittingNodeName" ref="tagElement"/>
</bean>
```

#### Simple Splitting by node with surrounding elements
For a detailed explanation of this sample look at the [java configuration section](#splitting-with-surrounding-elements).
```xml
<bean id="surroundingFileStaxNodeSplitter" class="com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter"
      init-method="init">
    <property name="outputFolder" value="#{ T(com.google.common.io.Files).createTempDir().getAbsolutePath() }"/>
    <property name="splittingNodeName" ref="tagElement"/>
    <property name="documentEventHandler">
        <bean class="com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler">
            <property name="node" ref="tagParentElement"/>
        </bean>
    </property>
</bean>
```

#### Simple Splitting by node with surrounding elements and global values
For a detailed explanation of this sample look at the [java configuration section](#splitting-with-global-values).
```xml
<bean id="globalValueFileStaxSplitter" class="com.github.spuchmann.xml.splitter.stax.FileStaxNodeSplitter"
      init-method="init">
    <property name="outputFolder" value="#{ T(com.google.common.io.Files).createTempDir().getAbsolutePath() }"/>
    <property name="splittingNodeName" ref="tagElement"/>
    <property name="globalDataCollectorNameList">
        <list>
            <ref bean="globalValue"/>
            <ref bean="globalValue1"/>
        </list>
    </property>
    <property name="documentEventHandler">
        <bean class="com.github.spuchmann.xml.splitter.stax.XmlSurroundingNodeDocumentEventHandler">
            <property name="node" ref="tagParentElement"/>
            <property name="globalValueList">
                <list>
                    <ref bean="globalValue1"/>
                </list>
            </property>
        </bean>
    </property>
</bean>
```

## License
The xml-splitter is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0