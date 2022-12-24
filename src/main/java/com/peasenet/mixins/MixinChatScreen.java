package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.util.listeners.OnChatSendListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    public void onChatMessage(String msg, boolean history, CallbackInfoReturnable<Boolean> cir) {
        var event = new OnChatSendListener.OnChatSendEvent(msg);
        GavinsMod.eventManager.call(event);
        if (event.isCancelled()) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(msg);
            cir.setReturnValue(true);
        }

    }
}
