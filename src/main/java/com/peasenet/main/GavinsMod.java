package com.peasenet.main;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gt3ch1
 * @version 5/14/2022
 */
public class GavinsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gavinsmod");

    @Override
    public void onInitialize() {
        LOGGER.info("GavinsMod initialized");
    }
}

