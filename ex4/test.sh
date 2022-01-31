#/usr/bin/bash

inputFile=$1
excpected_output=$2

cat $inputFile > input/Input.txt
make everything
cat output/MIPS_OUTPUT.txt 
