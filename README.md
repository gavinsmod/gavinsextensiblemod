# Gavin's Extensible Mod v1.0.0

A successor to the non-extensible [gavinsmod](https://github.com/GT3CH1/minecraft-mod)

## Quick Terminology

- **GEM**: A Minecraft mod that is built with the gemclient-core, gavui, or gavinsmod-events libraries.
  These mods are meant to be extensible, and exist without dependencies to other GEMs.

## What is this repository?

This repository serves as a conglomerate of the following projects:

- [gavui](https://github.com/GT3CH1/gavui)
- [gavinsmod-events](https://github.com/gavinsmod/gavinsmod-events)
- [gemclient-core](https://github.com/gavinsmod/gemclient-core)
- And all of the other mod-* repositories seen in the [gavinsmod organization](https://github.com/gavinsmod)
  The sole purpose of repository is to aid in the development of _preexisting_ GEMs and the core
  projects that make up the GEM client. This repository is not meant to be used as a dependency
  nor is it meant to be used as a standalone mod. However, there is a helper script that will allow you
  to build all the GEMs and the core projects into their respective jars, as well as install them into
  your Minecraft directory if wanted (see the `build-all.py` python script).

## How can I help contribute to this project?

If you are interested in contributing to this project, please read the [CONTRIBUTING.md](CONTRIBUTING.md) file.
If you want to request a feature, please open an issue in this repository! If your feature is accepted,
a separate repository for that feature will be created (unless otherwise warranted), and that project will be included
in this repository.

## How do I build this project?

Please run the `build-all.py` python script to build all the GEMs and the core projects into their respective jars.
Optionally, you may set COPY=1 in the environment variables to copy the jars into your Minecraft directory.

## How can I create my own GEM?

Instructions for creating your own GEM will be found at
the [gemclient-core](https://github.com/gavinsmod/gemclient-core/wiki) wiki.
Alternatively, you can clone the [mod-template](https://github.com/gavinsmod/mod-template) repository to create your own
basic GEM.
