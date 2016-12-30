#!/bin/sh
# MIT License
# Copyright (c) 2016 Corentin Limier 
# See LICENSE file at root of repository for more informations

scriptsdir=$(dirname $0)
rootdir=${scriptsdir}/..
current=$(pwd)

cd $rootdir
activator playUpdateSecret
activator clean compile stage
cd $current
