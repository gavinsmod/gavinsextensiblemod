# Gavin's Extensible Mod v1.0.2

A successor to the non-extensible [gavinsmod](https://github.com/gavinsmod/minecraft-mod) Minecraft client.

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
  your Minecraft directory if wanted (see the [How do I build this
  project?](#how-do-i-build-this-project) section)

This repository will also be the repository to get up-to-date releases of the
GEMs contained within this repository (for example: fullbright, xray, tracers),
as well an up-to-date release of the gemclient-core library, which is required
for all GEMs to work.

This repository will _not_ host an up-to-date release of GavUI nor
gavinsmod-events. You will need to get releases for those at the following
locations:
- GavUI: [https://github.com/GT3CH1/gavui](https://github.com/GT3CH1/gavui)
- gavinsmod-events: [https://github.com/gavinsmod/gavinsmod-events](https://github.com/gavinsmod/gavinsmod-events)

## How can I help contribute to this project?

If you are interested in contributing to this project, please read the [CONTRIBUTING.md](CONTRIBUTING.md) file.
If you want to request a feature, please open an issue in this repository! If your feature is accepted,
a separate repository for that feature will be created (unless otherwise warranted), and that project will be included
in this repository.

## How do I build this project?

Please run the `build.py` python script to build a portion or all of the core
gavinsmod-client project. There are several options in this script that will
help you with building this project.
- `--clean`
    - Whether to clean a given project before building. For example, `./build.py
      --clean`
    - Default: `False`
- `--minecraft-dir` 
    - The path to the directory of your Minecraft install. For example, `./build.py
    --minecraft-dir=%APPDATA%\.minecraft\`
    - Default: `~/.minecraft/`
- `--copy`
    - After a successful build, copy the jar file to your Minecraft directory.
      For example, `./build.py --copy`
    - Default: `False`
- `--all`
    - Builds __all__ projects contained into this repository. For example,
      `./build.py --all`
    - Default: `False`
- `--directory`
    - Builds only one of the projects under the given directory. For example,
      `./build.py --directory mods/xray`
    - Default: `None`

As an example, if you want to build everything from scratch and copy to an alternative
Minecraft installation, you may do so by calling: `./build.py --all --copy
--minecraft-dir=/some/other/directory`

## How can I create my own GEM?

Instructions for creating your own GEM will be found at
the [gemclient-core](https://github.com/gavinsmod/gemclient-core/wiki) wiki.
Alternatively, you can clone the [mod-template](https://github.com/gavinsmod/mod-template) repository to create your own
basic GEM.
