#!/bin/bash

# This script changes the name of the matrix in the file

for i in {1..100}; do
    mv tricky_matrix$i.txt rigged_matrix$i.txt
done