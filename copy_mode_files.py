#!/usr/bin/env python3
import os
import shutil
from pathlib import Path
from git import Repo, Submodule


repo = Repo('.')

main_dir = 'src/main/kotlin/com/peasenet/mods/'
main_folders = os.listdir(main_dir)
files = [ f for f in main_folders if os.path.isdir(main_dir+f)]
mods = []
mod_names = []
for file in files:
    if file == "gui" :
        continue
    [mods.append(main_dir+file+'/'+mod) for mod in os.listdir(main_dir+file) if os.path.splitext(mod)[1] == ".kt"]
    [mod_names.append(os.path.splitext(mod)[0][3:].lower()) for mod in os.listdir(main_dir+file) if os.path.splitext(mod)[1] == ".kt" and os.path.splitext(mod)[0][-3:] != "mod"]
#    [mods.append(main_dir+file+'/'+mod) for mod in os.listdir(main_dir+file) if os.path.splitext(mod)[1] == ".kt" and os.path.isfile(main_dir+file+'/'+mod)]

for idx, item in enumerate(mod_names):

    parent_dir = str(Path(mods[idx]).parents[:-1][0])
    print(parent_dir)
    dir = f'mods/{mod_names[idx]}/{parent_dir}'
    print(f'Copying {mods[idx]} to {dir}')
    try:
        os.makedirs(dir)
    except:
        pass
    shutil.copy(mods[idx], dir)


