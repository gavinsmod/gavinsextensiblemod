#!/bin/bash
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
MINECRAFT_DIR=~/.minecraft
cd $SCRIPT_DIR/mods;
for f in *; do
    cd $f;
    gradle build >/dev/null;
    if [[ ! $? -eq 0 ]]; then
        echo "[ERROR] Failed to build gem-$f"
        exit 1
    else
        echo "[INFO] Successfully built gem-$f"
    fi
    cp -v build/libs/gem-$f-1.0.[0-9].jar $MINECRAFT_DIR/mods
    cd ..
done
cd $SCRIPT_DIR
cd gavui
gradle clean >/dev/null;
gradle build >/dev/null;
if [[ ! $? -eq 0 ]]; then
    echo "[ERROR] Failed to build gavui"
    exit 1
else
    echo "[INFO] Successfully built gavui"
fi
cp -v build/libs/gavui-[0-9].[0-9].[0-9].jar $MINECRAFT_DIR/mods
cd ../gavinsmod-events
if [[ ! $? -eq 0 ]]; then
    echo "[ERROR] Failed to build gavinsmod-events"
    exit 1
else
    echo "[INFO] Successfully built gavinsmod-events"
fi
cp -v build/libs/gavinsmod-events-[0-9].[0-9].[0-9].jar $MINECRAFT_DIR/mods
exit 0
