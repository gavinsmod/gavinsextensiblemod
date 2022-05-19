package com.peasenet.main;

import com.peasenet.mods.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * @author gt3ch1
 * @version 5/14/2022
 */
public class GavinsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gavinsmod");
    private static final ModXray XRay = new ModXray();
    private static final ModFly Fly = new ModFly();
    private static final ModFastMine FastMine = new ModFastMine();
    private static final ModFullBright FullBright = new ModFullBright();
    private static final ModMobTracer MobTracer = new ModMobTracer();
    private static final ModChestFinder ChestFinder = new ModChestFinder();
    public static ArrayList<Mod> mods;

    @Override
    public void onInitialize() {
        LOGGER.info("GavinsMod initialized");
        mods = new ArrayList<>() {
            {
                add(XRay);
                add(Fly);
                add(FastMine);
                add(FullBright);
                add(MobTracer);
                add(ChestFinder);
            }
        };
    }

    public static boolean XRayEnabled() {
        return XRay.isActive();
    }

    public static boolean FlyEnabled() {
        return Fly.isActive();
    }

    public static boolean FastMineEnabled() {
        return FastMine.isActive();
    }

    public static boolean FullBrightEnabled() {
        return FullBright.isActive();
    }

    public static boolean MobTracerEnabled() {
        return MobTracer.isActive();
    }

    public static boolean ChestFinderEnabled() {
        return ChestFinder.isActive();
    }
}

