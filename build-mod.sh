#!/bin/bash
mod=$1
if [[ -z $mod ]]; then
    echo "[ERROR] No mod specified"
    exit 1
fi
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
MINECRAFT_DIR=~/.minecraft
cd $SCRIPT_DIR/mods/$mod;
gradle build >/dev/null;
if [[ ! $? -eq 0 ]]; then
    echo "[ERROR] Failed to build gem-$mod"
    exit 1
else
    echo "[INFO] Successfully built gem-$mod"
fi
cp -v build/libs/gem-$mod-1.0.[0-9].jar $MINECRAFT_DIR/mods
cd ..

