#!/bin/bash

scenariosPath=$1
scenarioIdFrom=$2
scenarioTo=$3

cp -R "${scenariosPath}/${scenarioIdFrom}" "${scenariosPath}/${scenarioTo}" 
