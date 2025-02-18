#!/bin/bash

# Read the number of elements
read -p "Enter the number of elements (N): " N

# Check if N is a positive integer
if ! [[ "$N" =~ ^[0-9]+$ ]] || [ "$N" -le 0 ]; then
    echo "Please enter a valid positive integer for N."
    exit 1
fi

# Initialize sum
sum=0

# Read N numbers and calculate the sum
for ((i=1; i<=N; i++)); do
    read -p "Enter number $i: " num
    sum=$((sum + num))
done

# Calculate and print the average
average=$(echo "scale=2; $sum / $N" | bc)
echo "The average of the numbers is: $average"
