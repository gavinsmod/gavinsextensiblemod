#!/usr/bin/env python3
import logging
import os
import subprocess
import pathlib
import re
logging.basicConfig(level=logging.DEBUG,
                    format="[%(levelname)s] %(message)s")

# Path to the directory containing this script
SCRIPT_DIR = pathlib.Path(__file__).parent.absolute()

# check if "COPY" is set in the environment
COPY=False
if "COPY" in os.environ:
    COPY=True

MINECRAFT_DIR = "~/.minecraft"
if "MINECRAFT_DIR" in os.environ:
    MINECRAFT_DIR = os.environ["MINECRAFT_DIR"]

# Run "gradle build" for each directory under the "mods" directory

build_locs = []
# add "gavui", "gemclient-core", and "gavinsmod-events" to the build list
build_locs.append(SCRIPT_DIR.joinpath("gavui"))
build_locs.append(SCRIPT_DIR.joinpath("gemclient-core"))
build_locs.append(SCRIPT_DIR.joinpath("gavinsmod-events"))

[build_locs.append(f) for f in SCRIPT_DIR.joinpath("mods").iterdir() if f.is_dir()]

for mod_dir in build_locs:
    if mod_dir.is_dir():
        logging.info("Building %s", mod_dir.name)
        os.chdir(mod_dir)
        res = os.system("gradle build >/dev/null")
        if res != 0:
            logging.error("Failed to build %s", mod_dir.name)
            exit(1)
        if COPY:
            # get the right file by matching for gem-<file>-[0-9]*-[0-9]*-[0-9]*.jar file
            jar_file = None
            for file in mod_dir.joinpath("build/libs").iterdir():
                if re.match(r"gem-.*\-[0-9]*\.[0-9]*\.[0-9]*\.jar", file.name):
                    jar_file = file
                    break
            if jar_file is None:
                # attempt to find a jar file at <name>-[0-9]*-[0-9]*-[0-9]*.jar
                for file in mod_dir.joinpath("build/libs").iterdir():
                    if re.match(r".*\-[0-9]*\.[0-9]*\.[0-9]*\.jar", file.name):
                        jar_file = file
                        break
            if jar_file is None:
                logging.error("Failed to find jar file for %s", mod_dir.name)
                exit(1)
            logging.info("Copying %s to %s", jar_file.name, MINECRAFT_DIR)
            res = os.system("cp %s %s/mods" % (jar_file, MINECRAFT_DIR))

        
