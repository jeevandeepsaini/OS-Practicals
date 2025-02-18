#!/bin/bash

# Function to display the menu
display_menu() {
    echo "=============================="
    echo "             MENU             "
    echo "=============================="
    echo "1. List of files"
    echo "2. Process Status"
    echo "3. Date"
    echo "4. Users in Program"
    echo "5. Quit"
    echo "=============================="
    echo -n "Please select an option (1-5): "
}

# Main loop
while true; do
    display_menu
    read option

    case $option in
        1)
            echo "Listing files in the current directory:"
            ls -l
            ;;
        2)
            echo "Displaying process status:"
            ps aux
            ;;
        3)
            echo "Current date and time:"
            date
            ;;
        4)
            echo "Users currently logged in:"
            who
            ;;
        5)
            echo "Exiting the program. Goodbye!"
            exit 0
            ;;
        *)
            echo "Invalid option. Please select a number between 1 and 5."
            ;;
    esac

    echo -e "\nPress Enter to continue..."
    read
done
