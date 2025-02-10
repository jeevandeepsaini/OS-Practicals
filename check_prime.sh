#!/bin/bash

read -p "Enter a number: " num

if ! [[ "$num" =~ ^[0-9]+$ ]] || [ "$num" -lt 2 ]; then
    echo "$num is not prime."
    exit 1
fi

for ((i=2; i*i<=num; i++)); do
    if (( num % i == 0 )); then
        echo "$num is not prime."
        exit 0
    fi
done

echo "$num is prime."
