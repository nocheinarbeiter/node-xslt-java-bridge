#!/bin/bash

tmp=./builder-temp-dir
mkdir ${tmp}
javac -cp ./vendor/SaxonHE9-7-0-14J/saxon9he.jar -d ${tmp} Transformer.java
pushd ${tmp}
jar cf ../Transformer.jar *.class
popd
rm -fr ${tmp}
