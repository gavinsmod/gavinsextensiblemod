#!/usr/bin/env python3
import logging
import os
import subprocess
import pathlib
import re
import sys
import argparse

logging.basicConfig(level=logging.DEBUG,
                    format="[%(levelname)s] %(message)s")

# Path to the directory containing this script
SCRIPT_DIR = pathlib.Path(__file__).parent.absolute()

parser = argparse.ArgumentParser (
        prog='build.py',
        description='Build everything that is part of the gavinsmod-client',
        epilog='Made by @GT3CH1')

parser.add_argument('--copy',required=False, action='store_true',
                    help='Whether to copy freshly built GEMs to your Minecraft mods directory.')
parser.add_argument('--clean',required=False, action='store_true',
                    help="Whether to clean before buildling")
parser.add_argument('--directory',required=False,
                    help="A directory to a project")
parser.add_argument('--minecraft-dir',required=False,default='~/.minecraft/mods',
                    help="The directory to your Minecraft installation")
parser.add_argument('--all',required=False, action='store_true',
                    help="Whether to build all modules in this project.")
parser.add_argument('--build',required=False,action='store_true',
                     help="Whether to build everything in this project.")
args = parser.parse_args()
print(args)
CLEAN = args.clean
MINECRAFT_DIR = args.minecraft_dir
COPY = args.copy
ALL = args.all
DIRECTORY = args.directory
BUILD = args.build



def do_build(build_locs: []):
    for mod_dir in build_locs:
        if mod_dir.is_dir():
            os.chdir(mod_dir)
            name = mod_dir.name
            if CLEAN:
                logging.info("Cleaning %s",name)
                res = os.system("gradle clean >/dev/null")
                if res != 0:
                    logging.error("Failed to clean %s", name)
                    exit(1)
            logging.info("Building %s", name)
            if BUILD:
                res = os.system("gradle build >/dev/null")
                if res != 0:
                    logging.error("Failed to build %s", name)
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
                    logging.error("Failed to find jar file for %s", name)
                    exit(1)
                logging.info("Copying %s to %s", jar_file.name, MINECRAFT_DIR)
                res = os.system('cp "%s" "%s"' % (jar_file, MINECRAFT_DIR))
            
if ALL:
    logging.info("Building all!")
    # check if "COPY" is set in the environment
    build_locs = []
    # add "gavui", "gemclient-core", and "gavinsmod-events" to the build list
    build_locs.append(SCRIPT_DIR.joinpath("gavui"))
    build_locs.append(SCRIPT_DIR.joinpath("gemclient-core"))
    build_locs.append(SCRIPT_DIR.joinpath("gavinsmod-events"))

    [build_locs.append(f) for f in SCRIPT_DIR.joinpath("mods").iterdir() if f.is_dir()]
    do_build(build_locs)
    exit(0)

if DIRECTORY is not None:
    build_locs = []
    build_locs.append(SCRIPT_DIR.joinpath(DIRECTORY))
    do_build(build_locs)
    exit(0)

parser.print_help(sys.stderr)
