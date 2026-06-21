package com.peasenet.mixins;

import com.mojang.authlib.GameProfile;
import com.peasenet.main.Mods;
import com.peasenet.mods.misc.ModFreeCam;
import com.peasenet.util.ChatCommand;
import com.peasenet.util.FakeInput;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 * @author GT3CH1
 * @version 06-21-2026
 * @since 06-21-2026
 */
@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
    public MixinLocalPlayer(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "isShiftKeyDown", at = @At("HEAD"), cancellable = true)
    private void cancelShift(CallbackInfoReturnable<Boolean> cir) {
        if (Mods.isActive(ChatCommand.FreeCam))
            cir.setReturnValue(false);
    }

    @Override
    public void turn(double yaw, double pitch) {
        if (Mods.isActive(ChatCommand.FreeCam)) {
            ModFreeCam freeCam = Mods.getMod(ChatCommand.FreeCam);
            freeCam.turn(yaw, pitch);
            return;
        }
        super.turn(yaw, pitch);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void atTickHead(CallbackInfo ci) {
        FakeInput.Companion.swap();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void atTickReturn(CallbackInfo ci) {
        FakeInput.Companion.unswap();
    }
}
