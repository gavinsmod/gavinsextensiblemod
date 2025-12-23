package com.peasenet.mixins;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

/**
 *
 * @author GT3CH1
 * @version 12-22-2025
 * @since 12-22-2025
 */
//@Mixin(RegistrySetBuilder.class)
//public interface RegistrySetBuilderAccessor {
//    @Inject(at = @At("RETURN"), method = "buildProviderWithContext")
//    private static void test(RegistrySetBuilder.UniversalOwner universalOwner, RegistryAccess registryAccess, Stream<HolderLookup.RegistryLookup<?>> stream, CallbackInfoReturnable<HolderLookup.Provider> cir) {
//
//    }
//}
