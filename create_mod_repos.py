#!/usr/bin/env python3
import os
from git import Repo, Submodule


repo = Repo('.')

main_dir = 'src/main/kotlin/com/peasenet/mods/'
main_folders = os.listdir(main_dir)
files = [ f for f in main_folders if os.path.isdir(main_dir+f)]
mods = []
for file in files:
    if file == "gui":
        continue
    for mod in os.listdir(main_dir+file):
        mods.append(mod)

folder_names = list(map(lambda x: x.replace('Mod','').replace('.kt','').lower(), mods))

[Submodule.add(repo, folder, "mods/"+folder, "https://github.com/gavinsmod/mod-template") for folder in folder_names]
