package com.coopfrey.vanillatogglekeybind.client;

import net.fabricmc.fabric.api.event.Event;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class AppleSkinIntegration {
    private AppleSkinIntegration() {
    }

    public static void registerSaturationHandler() {
        try {
            Class<?> eventClass = Class.forName("squeek.appleskin.api.event.HUDOverlayEvent$Saturation");
            Field eventField = eventClass.getField("EVENT");
            Object eventObj = eventField.get(null);
            if (!(eventObj instanceof Event)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Event<Object> event = (Event<Object>) eventObj;

            Class<?> handlerClass = Class.forName("squeek.appleskin.api.handler.EventHandler");
            Field canceledField = eventClass.getField("isCanceled");

            InvocationHandler invocationHandler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    if (!"interact".equals(method.getName()) || args == null || args.length != 1) {
                        return null;
                    }
                    if (!VanillaToggleConfigManager.getConfig().hideAppleSkinSaturation) {
                        return null;
                    }
                    if (VanillaToggleState.isCleanMode()) {
                        try {
                            canceledField.setBoolean(args[0], true);
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                    return null;
                }
            };

            Object handler = Proxy.newProxyInstance(handlerClass.getClassLoader(), new Class<?>[]{handlerClass}, invocationHandler);
            event.register(handler);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {
            // AppleSkin not installed
        }
    }
}
