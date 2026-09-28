package com.supergems;

import com.supergems.network.ModNetwork;
import com.supergems.registry.ModAttachments;
import com.supergems.registry.ModCreativeTabs;
import com.supergems.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/** Haupteinstiegspunkt der Mod "Super Gems". */
@Mod(SuperGems.MODID)
public class SuperGems {
    public static final String MODID = "supergems";

    public SuperGems(IEventBus modEventBus, ModContainer container) {
        // Alle DeferredRegister an den Mod-Event-Bus hängen
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModAttachments.ATTACHMENTS.register(modEventBus);

        // Netzwerk-Payloads registrieren
        modEventBus.addListener(ModNetwork::registerPayloads);

        // Config (erzeugt config/supergems-common.toml)
        container.registerConfig(ModConfig.Type.COMMON, SuperGemsConfig.SPEC);
    }
}
