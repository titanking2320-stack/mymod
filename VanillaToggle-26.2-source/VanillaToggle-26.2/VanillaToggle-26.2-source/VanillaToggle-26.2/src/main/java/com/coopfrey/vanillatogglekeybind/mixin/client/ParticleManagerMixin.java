package com.coopfrey.vanillatogglekeybind.mixin.client;

import com.coopfrey.vanillatogglekeybind.client.VanillaToggleState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ParticleEngine.class)
public class ParticleManagerMixin {
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void vanillatogglekeybind$suppressParticles(ParticleOptions parameters, double x, double y, double z,
                                                        double velocityX, double velocityY, double velocityZ,
                                                        CallbackInfoReturnable<Particle> cir) {
        if (VanillaToggleState.shouldSuppress(parameters)) {
            cir.setReturnValue(null);
        }
    }
}
