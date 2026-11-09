package com.coopfrey.vanillatogglekeybind.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class VanillaToggleConfigScreen extends Screen {
    private final Screen parent;
    private VanillaToggleConfig editingConfig;
    private final List<ToggleInfo> toggleInfos = new ArrayList<>();

    public VanillaToggleConfigScreen(Screen parent) {
        super(Component.translatable("screen.vanillatogglekeybind.config.title"));
        this.parent = parent;
        this.editingConfig = VanillaToggleConfigManager.getConfig().copy();
    }

    @Override
    protected void init() {
        super.init();
        toggleInfos.clear();

        int centerX = this.width / 2;

        // title and section headings
        addLabel(centerX, 8, this.title, 0xFFFFFFFF);
        addLabel(centerX, 24, Component.translatable("text.vanillatogglekeybind.config.section.pvp"), 0xFFFFFFFF);
        addLabel(centerX, 36, Component.translatable("text.vanillatogglekeybind.config.section.vanilla"), 0xFFAAAAAA);

        int y = this.height / 6;
        addRenderableWidget(createToggleButton(centerX, y, "text.vanillatogglekeybind.config.short.bob",
                () -> editingConfig.forceBobView,
                () -> editingConfig.forceBobView = !editingConfig.forceBobView));
        y += 22;
        addRenderableWidget(createToggleButton(centerX, y, "text.vanillatogglekeybind.config.short.fov",
                () -> editingConfig.forceFovEffectScale,
                () -> editingConfig.forceFovEffectScale = !editingConfig.forceFovEffectScale));
        y += 26;
        addRenderableWidget(createToggleButton(centerX, y, "text.vanillatogglekeybind.config.short.suppress",
                () -> editingConfig.suppressParticles,
                () -> editingConfig.suppressParticles = !editingConfig.suppressParticles));
        y += 26;
        addLabel(centerX, y - 14,
                Component.translatable("text.vanillatogglekeybind.config.particles_hint",
                        editingConfig.suppressedParticles.size()), 0xFFAAAAAA);
        addRenderableWidget(createToggleButton(centerX, y, "text.vanillatogglekeybind.config.short.appleskin",
                () -> editingConfig.hideAppleSkinSaturation,
                () -> editingConfig.hideAppleSkinSaturation = !editingConfig.hideAppleSkinSaturation));
        y += 26;
        addLabel(centerX, y - 14, Component.translatable("text.vanillatogglekeybind.config.appleskin_hint"), 0xFF888888);

        int buttonWidth = 90;
        int gap = 10;
        int totalWidth = buttonWidth * 3 + gap * 2;
        int startX = centerX - totalWidth / 2;
        int buttonY = this.height - 30;
        addRenderableWidget(createControlButton(startX, buttonY, buttonWidth, 20,
                Component.translatable("gui.cancel"), this::cancel));
        addRenderableWidget(createControlButton(startX + buttonWidth + gap, buttonY, buttonWidth, 20,
                Component.translatable("text.vanillatogglekeybind.config.button.reset"), this::resetToDefaults));
        addRenderableWidget(createControlButton(startX + (buttonWidth + gap) * 2, buttonY, buttonWidth, 20,
                Component.translatable("gui.done"), this::done));
    }

    private void addLabel(int centerX, int y, Component text, int color) {
        int w = this.font.width(text);
        StringWidget label = new StringWidget(centerX - w / 2, y, w, 9, text, this.font);
        label.setColor(color);
        addRenderableWidget(label);
    }

    private Button createToggleButton(int centerX, int y, String translationKey, BooleanSupplier state, Runnable onPress) {
        Button button = Button.builder(toggleText(translationKey, state.getAsBoolean()), b -> {
            onPress.run();
            b.setMessage(toggleText(translationKey, state.getAsBoolean()));
        }).bounds(centerX - 110, y, 220, 20).build();
        toggleInfos.add(new ToggleInfo(button, translationKey, state));
        return button;
    }

    private Button createControlButton(int x, int y, int w, int h, Component text, Runnable onPress) {
        return Button.builder(text, b -> onPress.run()).bounds(x, y, w, h).build();
    }

    private void resetToDefaults() {
        this.editingConfig = new VanillaToggleConfig();
        refreshToggleMessages();
    }

    private void refreshToggleMessages() {
        for (ToggleInfo info : toggleInfos) {
            info.button().setMessage(toggleText(info.translationKey(), info.stateSupplier().getAsBoolean()));
        }
    }

    private static Component toggleText(String translationKey, boolean state) {
        return Component.translatable(translationKey, stateText(state));
    }

    private static Component stateText(boolean state) {
        return Component.translatable("text.vanillatogglekeybind.config.state." + (state ? "on" : "off"));
    }

    private void cancel() {
        this.minecraft.setScreenAndShow(this.parent);
    }

    private void done() {
        VanillaToggleConfigManager.updateConfig(this.editingConfig);
        this.minecraft.setScreenAndShow(this.parent);
    }

    private record ToggleInfo(Button button, String translationKey, BooleanSupplier stateSupplier) {
    }
}
