package com.peasenet.mixins;

import com.peasenet.util.ModCommands;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gt3ch1
 * @version 5/24/2022
 */
@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    public void checkModCommands(String message, Text preview, CallbackInfo ci) {
        // Checks to see if the messages starts with a . followed immediately by the name of the mod
        if (message.startsWith(".")) {
            // If so, it will be sent to the mod's command handler
            var success = ModCommands.handleCommand(message);
            // If the command was handled, the message will not be sent to the server
            if (success) ci.cancel();
        }
    }
}
