#!/bin/bash

echo "Enter the Basic Salary: "
read basic_salary

if [ $basic_salary -lt 1500 ]
then
  gsal=$((basic_salary + (basic_salary/100) * 10 + (basic_salary/100) * 90))
  echo "The Gross Salary: $gsal"
else
  gsal=$((basic_salary + 500 + (basic_salary/100) * 98))
  echo "The Gross Salary: $gsal"
fi
