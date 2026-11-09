package com.coopfrey.vanillatogglekeybind.client;

import java.util.ArrayList;
import java.util.List;

public class VanillaToggleConfig {
    public static final List<String> DEFAULT_PARTICLES = List.of(
            "minecraft:explosion",
            "minecraft:explosion_emitter",
            "minecraft:poof",
            "minecraft:flame",
            "minecraft:small_flame",
            "minecraft:smoke",
            "minecraft:large_smoke",
            "minecraft:white_smoke"
    );

    public boolean forceBobView = true;
    public boolean forceFovEffectScale = true;
    public double fovEffectScale = 1.0D;
    public boolean suppressParticles = true;
    public boolean hideAppleSkinSaturation = true;
    public List<String> suppressedParticles = new ArrayList<>(DEFAULT_PARTICLES);

    public VanillaToggleConfig() {
    }

    private VanillaToggleConfig(VanillaToggleConfig other) {
        this.forceBobView = other.forceBobView;
        this.forceFovEffectScale = other.forceFovEffectScale;
        this.fovEffectScale = other.fovEffectScale;
        this.suppressParticles = other.suppressParticles;
        this.hideAppleSkinSaturation = other.hideAppleSkinSaturation;
        this.suppressedParticles = new ArrayList<>(other.suppressedParticles);
    }

    public VanillaToggleConfig copy() {
        return new VanillaToggleConfig(this);
    }
}
