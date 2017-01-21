#!/bin/bash
# Grabs and kill a process from the pidlist that has the word myapp

pid=`ps aux | grep codenapper | awk '{print $2}'`
sudo kill -9 $pid
