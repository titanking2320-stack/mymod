package com.coopfrey.vanillatogglekeybind.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class VanillatogglekeybindClient implements ClientModInitializer {
    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("vanillatogglekeybind", "toggle"));

    private KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.vanillatoggle.toggle", InputConstants.Type.KEYSYM, 296, CATEGORY));

        // start in the "vanilla off" state: bobbing off, FOV effects 0%, damage tilt 0%
        ClientLifecycleEvents.CLIENT_STARTED.register(VanillaToggleState::apply);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                boolean enabled = VanillaToggleState.toggle(client);
                if (client.player != null) {
                    client.gui.getChat().addMessage(Component.translatable(
                            enabled ? "text.vanillatoggle.toggle.on" : "text.vanillatoggle.toggle.off"));
                }
            }
        });

        AppleSkinIntegration.registerSaturationHandler();
    }
}
