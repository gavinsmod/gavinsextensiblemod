package com.peasenet.main;

import com.peasenet.gui.GuiClick;
import com.peasenet.mods.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * @author gt3ch1
 * @version 5/24/2022
 */
public class GavinsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gavinsmod");
    public static final String VERSION = "v1.1.0";
    public static final GuiClick gui = new GuiClick();
    // MOVEMENT
    private static final ModXray XRay = new ModXray();
    private static final ModFly Fly = new ModFly();
    private static final ModAutoJump AutoJump = new ModAutoJump();
    private static final ModClimb Climb = new ModClimb();
    private static final ModNoClip NoClip = new ModNoClip();
    private static final ModNoFall NoFall = new ModNoFall();
    // COMBAT
    private static final ModKillAura KillAura = new ModKillAura();
    private static final ModAutoCrit AutoCrit = new ModAutoCrit();
    // RENDER
    private static final ModAntiHurt AntiHurt = new ModAntiHurt();
    private static final ModFastMine FastMine = new ModFastMine();
    private static final ModFullBright FullBright = new ModFullBright();
    private static final ModChestEsp ChestEsp = new ModChestEsp();
    private static final ModChestTracer ChestTracer = new ModChestTracer();
    private static final ModMobEsp MobEsp = new ModMobEsp();
    private static final ModMobTracer MobTracer = new ModMobTracer();
    private static final ModEntityItemTracer EntityItemTracer = new ModEntityItemTracer();
    private static final ModEntityItemEsp EntityItemEsp = new ModEntityItemEsp();
    private static final ModEntityPlayerTracer EntityPlayerTracer = new ModEntityPlayerTracer();
    private static final ModEntityPlayerEsp EntityPlayerEsp = new ModEntityPlayerEsp();
    private static final ModAntiPumpkin AntiPumpkin = new ModAntiPumpkin();

    // GUI
    private static final ModGui Gui = new ModGui();
    private static final ModGuiTextOverlay ModGuiTextOverlay = new ModGuiTextOverlay();
    public static ArrayList<Mod> mods;

    public static boolean isEnabled(Type mod) {
        // find mod via stream and check if it is enabled.
        return mods.stream().filter(m -> m.getType() == mod).findFirst().get().isActive();
    }

    public static void setEnabled(Type mod, boolean enabled) {
        // find mod via stream and set it to enabled.
        var theMod = mods.stream().filter(m -> m.getType() == mod).findFirst().get();
        if(enabled)
            theMod.activate();
        else
            theMod.deactivate();
    }

    /**
     * Gets all of the mods in the given category.
     *
     * @param category The category to get the mods from.
     * @return The mods in the given category.
     */
    public static ArrayList<Mod> getModsInCategory(Type.Category category) {
        // use stream to filter by category and sort by mod name
        return mods.stream()
                .filter(mod -> mod.getCategory() == category)
                .sorted(Comparator.comparing(Mod::getName))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }


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
                add(NoClip);
                add(NoFall);
                // COMBAT
                add(KillAura);
                add(AutoCrit);

                // RENDER
                add(AntiHurt);
                add(XRay);
                add(FullBright);
                add(ChestEsp);
                add(ChestTracer);

                add(MobEsp);
                add(MobTracer);

                add(EntityItemEsp);
                add(EntityItemTracer);

                add(EntityPlayerEsp);
                add(EntityPlayerTracer);

                add(AntiPumpkin);

                //GUI
                add(Gui);
                add(ModGuiTextOverlay);
            }
        };
    }

}

