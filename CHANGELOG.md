# Change log & Release notes

In the following sections you will find an overview of historical releases and
what major changes where part of them.

## 0.2.0

### Added

* Enhanced documentation
* Added collected of data which is not in split nodes

## 0.1.0

### Added

* Basic splitting implementation based on stax. It is now possible to
split a xml document by one element.
* Providing two types of splitter
    * In Memory
    * File Based
* Supporting some three events which occurres during the splitting 
process:
    * afterStartDocument
    * beforeEndDocument
    * finishedDocument