#!/bin/bash

# Read the number of terms from the user
read -p "Enter the number of terms in the Fibonacci series: " n

# Initialize the first two terms
a=0
b=1

# Print the Fibonacci series
for (( i=0; i<n; i++ )); do
    echo -n "$a "
    (( fn = a + b ))  # Calculate the next term
    a=$b
    b=$fn
done

echo  # for a new line after the series
