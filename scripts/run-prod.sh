#!/bin/sh
# MIT License
# Copyright (c) 2016 Corentin Limier 
# See LICENSE file at root of repository for more informations

scriptsdir=$(dirname $0)
rootdir=${scriptsdir}/..
current=$(pwd)

PORT=${1:-9000}

cd $rootdir
rootdir=$(pwd)
echo "Attention : vous devez exécuter le script compile.sh pour compiler la dernière version de l'application"
sed -i "s,^\(.*\)default.url.*$,\1default.url = \"jdbc:sqlite:${rootdir}/database/roshiny.db\"," ./target/universal/stage/conf/application.conf
./target/universal/stage/bin/roshiny -Dhttp.port=${PORT}
cd $current
