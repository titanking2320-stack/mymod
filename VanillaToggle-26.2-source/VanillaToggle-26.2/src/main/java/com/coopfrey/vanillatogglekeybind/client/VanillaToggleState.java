package com.coopfrey.vanillatogglekeybind.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class VanillaToggleState {
    /** true = vanilla mode ON */
    private static boolean cleanMode = false;

    private VanillaToggleState() {
    }

    public static boolean toggle(Minecraft client) {
        cleanMode = !cleanMode;
        apply(client);
        return cleanMode;
    }

    /** Applies the settings for the current state (also used once at game start). */
    public static void apply(Minecraft client) {
        VanillaToggleConfig config = VanillaToggleConfigManager.getConfig();

        // Vanilla ON  -> view bobbing ON,  FOV effects at config scale (100% default), damage tilt 100%
        // Vanilla OFF -> view bobbing OFF, FOV effects 0%, damage tilt 0%
        if (config.forceBobView) {
            client.options.bobView().set(cleanMode);
        }
        if (config.forceFovEffectScale) {
            client.options.fovEffectScale().set(cleanMode ? config.fovEffectScale : 0.0D);
        }
        client.options.damageTiltStrength().set(cleanMode ? 1.0D : 0.0D);
    }

    public static boolean shouldSuppress(ParticleOptions effect) {
        if (cleanMode) {
            return false;
        }
        Identifier identifier = BuiltInRegistries.PARTICLE_TYPE.getKey(effect.getType());
        return VanillaToggleConfigManager.shouldSuppressParticle(identifier);
    }

    public static boolean isCleanMode() {
        return cleanMode;
    }
}
