package com.supergems.client;

import com.supergems.SuperGems;
import com.supergems.network.ToggleSuperFormPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Client-Events auf dem Game-Bus: Tastendruck an den Server schicken. */
@EventBusSubscriber(modid = SuperGems.MODID, value = Dist.CLIENT)
public final class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        while (SuperGemsClient.TRANSFORM_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ToggleSuperFormPayload());
        }
    }

    private ClientEvents() {
    }
}
