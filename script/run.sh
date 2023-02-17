#!/bin/bash

for i in {1..10}; do
  printf "time $i : "
  curl "http://localhost:8080/test?size=$1"
  printf " ms\n"
  sleep 10
done
