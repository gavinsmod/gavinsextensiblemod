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
    // MOVEMENT
    private static final ModXray XRay = new ModXray();
    private static final ModFly Fly = new ModFly();
    private static final ModAutoJump AutoJump = new ModAutoJump();
    private static final ModClimb Climb = new ModClimb();
    // RENDER
    private static final ModFastMine FastMine = new ModFastMine();
    private static final ModFullBright FullBright = new ModFullBright();
    private static final ModChestEsp ChestEsp = new ModChestEsp();
    private static final ModChestTracer ChestTracer = new ModChestTracer();
    private static final ModMobEsp MobEsp = new ModMobEsp();
    private static final ModMobTracer MobTracer = new ModMobTracer();
    private static final ModEntityItemTracer EntityItemTracer = new ModEntityItemTracer();
    private static final ModEntityItemEsp EntityItemEsp = new ModEntityItemEsp();
    public static ArrayList<Mod> mods;

    @Override
    public void onInitialize() {
        LOGGER.info("GavinsMod initialized");
        mods = new ArrayList<>() {
            {
                // MOVEMENT
                add(Fly);
                add(FastMine);
                add(AutoJump);
                add(Climb);

                // RENDER
                add(XRay);
                add(FullBright);
                add(ChestEsp);
                add(ChestTracer);

                add(MobEsp);
                add(MobTracer);

                add(EntityItemEsp);
                add(EntityItemTracer);
            }
        };
    }


    public static boolean FlyEnabled() {
        return Fly.isActive();
    }

    public static boolean FastMineEnabled() {
        return FastMine.isActive();
    }

    public static boolean AutoJumpEnabled() {
        return AutoJump.isActive();
    }
    public static boolean XRayEnabled() {
        return XRay.isActive();
    }


    public static boolean FullBrightEnabled() {
        return FullBright.isActive();
    }

    public static boolean ChestEspEnabled() {
        return ChestEsp.isActive();
    }

    public static boolean EntityTracerEnabled() {
        return MobTracer.isActive();
    }

    public static boolean EntityEspEnabled() {
        return MobEsp.isActive();
    }

    public static boolean ChestTracerEnabled() {
        return ChestTracer.isActive();
    }

    public static boolean EntityItemTracerEnabled() {
        return EntityItemTracer.isActive();
    }

    public static boolean EntityItemEspEnabled() {
        return EntityItemEsp.isActive();
    }

    public static boolean ClimbEnabled() {
        return Climb.isActive();
    }

}

