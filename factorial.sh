#!/bin/bash

echo "Enter any number:"
read num
Fact=1

for ((i=2; i<=num; i++)); do
    Fact=$((Fact * i))
done

echo "The factorial of $num is $Fact"
