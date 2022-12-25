package com.peasenet.config;

import com.peasenet.annotations.Exclude;
import com.peasenet.main.Settings;

public abstract class Config<E> {

    @Exclude
    private transient String key;

    public abstract E getInstance();

    public abstract void setInstance(E data);

    public void saveConfig() {
        var cfg = getInstance();
        Settings.settings.put(key, cfg);
        Settings.save();
    }


    public E readFromSettings() {
        var _e = Settings.getConfig(this.getClass(), key);
        setInstance((E) _e);
        return (E) _e;
    }

    protected void setKey(String key) {
        this.key = key;
    }
}
