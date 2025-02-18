#!/bin/bash

read -p "Enter a number: " number

if ! [[ "$number" =~ ^-?[0-9]+$ ]]; then
    echo "Please enter a valid integer."
    exit 1
fi

(( number % 2 == 0 )) && echo "$number is even." || echo "$number is odd."
