#!/bin/sh
# MIT License
# Copyright (c) 2016 Corentin Limier 
# See LICENSE file at root of repository for more informations

scriptsdir=$(dirname $0)
rootdir=${scriptsdir}/..
current=$(pwd)

PORT=${1:-9000}

cd $rootdir
LC_ALL=fr_FR.utf8 activator "run ${PORT}"
cd $current
