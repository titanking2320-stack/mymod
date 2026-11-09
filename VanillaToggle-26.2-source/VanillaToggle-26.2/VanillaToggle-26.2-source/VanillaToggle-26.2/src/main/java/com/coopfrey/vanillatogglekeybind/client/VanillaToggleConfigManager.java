package com.coopfrey.vanillatogglekeybind.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class VanillaToggleConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaToggleConfigManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("vanillatogglekeybind.json");

    private static VanillaToggleConfig config = loadConfig();
    private static Set<String> suppressedParticles = buildSet(config.suppressedParticles);

    private VanillaToggleConfigManager() {
    }

    public static VanillaToggleConfig getConfig() {
        return config;
    }

    public static boolean shouldSuppressParticle(Identifier id) {
        return config.suppressParticles && id != null && suppressedParticles.contains(id.toString());
    }

    public static void updateConfig(VanillaToggleConfig newConfig) {
        config = newConfig.copy();
        suppressedParticles = buildSet(config.suppressedParticles);
        saveConfig();
    }

    private static VanillaToggleConfig loadConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                VanillaToggleConfig loaded = GSON.fromJson(json, VanillaToggleConfig.class);
                if (loaded != null) {
                    if (loaded.suppressedParticles == null) {
                        loaded.suppressedParticles = new ArrayList<>(VanillaToggleConfig.DEFAULT_PARTICLES);
                    }
                    return loaded;
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Failed to read vanilla toggle configuration, using defaults.", e);
        }
        VanillaToggleConfig fresh = new VanillaToggleConfig();
        saveConfig(fresh);
        return fresh;
    }

    private static void saveConfig() {
        saveConfig(config);
    }

    private static void saveConfig(VanillaToggleConfig toSave) {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(toSave));
        } catch (IOException e) {
            LOGGER.warn("Failed to write vanilla toggle configuration.", e);
        }
    }

    private static Set<String> buildSet(List<String> list) {
        Set<String> set = new LinkedHashSet<>();
        if (list != null) {
            for (String s : list) {
                if (s != null && !s.isBlank()) {
                    set.add(s.trim());
                }
            }
        }
        return set;
    }
}
