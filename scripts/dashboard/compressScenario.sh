#!/bin/bash

scenariosPath=$1
scenarioId=$2

current=$(pwd)

cd $scenariosPath && \
tar -cjf "${scenarioId}.tar.bz2" $scenarioId &&\
cd $current
