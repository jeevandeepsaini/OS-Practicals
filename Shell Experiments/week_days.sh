#!/bin/bash

# Prompt the user to enter a number for the day of the week
read -p "Enter a number (1-7) for the day of the week: " day

# Use a case statement to determine the day
case $day in
    1)
        echo "Monday"
        ;;
    2)
        echo "Tuesday"
        ;;
    3)
        echo "Wednesday"
        ;;
    4)
        echo "Thursday"
        ;;
    5)
        echo "Friday"
        ;;
    6)
        echo "Saturday"
        ;;
    7)
        echo "Sunday"
        ;;
    *)
        echo "Invalid input! Please enter a number between 1 and 7."
        ;;
esac
