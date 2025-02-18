#!/bin/bash

# Read a number from the user
read -p "Enter a number: " num

# Check if the input is a valid number
if ! [[ "$num" =~ ^-?[0-9]+$ ]]; then
    echo "Please enter a valid integer."
    exit 1
fi

# Reverse the number
original=$num
reversed=0

while [ $num -gt 0 ]; do
    digit=$(( num % 10 ))
    reversed=$(( reversed * 10 + digit ))
    num=$(( num / 10 ))
done

# Check if the original number is equal to the reversed number
if [ "$original" -eq "$reversed" ]; then
    echo "$original is a palindrome."
else
    echo "$original is not a palindrome."
fi
