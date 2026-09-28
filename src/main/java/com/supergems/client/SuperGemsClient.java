package com.supergems.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.supergems.SuperGems;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/** Client-Setup auf dem Mod-Bus: Taste und HUD registrieren. */
@EventBusSubscriber(modid = SuperGems.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SuperGemsClient {

    /** Standardtaste G, in den Steuerungs-Einstellungen änderbar. */
    public static final KeyMapping TRANSFORM_KEY = new KeyMapping(
            "key.supergems.transform", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.supergems");

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TRANSFORM_KEY);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SuperGems.MODID, "super_form_bar"),
                SuperFormHud::render);
    }

    private SuperGemsClient() {
    }
}
