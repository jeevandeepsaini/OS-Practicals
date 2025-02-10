#!/bin/bash

# Function to find the largest of three numbers
find_largest() {
    if [ "$1" -ge "$2" ] && [ "$1" -ge "$3" ]; then
        echo "$1 is the largest number."
    elif [ "$2" -ge "$1" ] && [ "$2" -ge "$3" ]; then
        echo "$2 is the largest number."
    else
        echo "$3 is the largest number."
    fi
}

# Read three numbers from the user
echo "Enter three numbers:"
read -p "Number 1: " num1
read -p "Number 2: " num2
read -p "Number 3: " num3

# Call the function to find the largest number
find_largest "$num1" "$num2" "$num3"
