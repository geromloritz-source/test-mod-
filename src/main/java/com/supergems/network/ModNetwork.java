package com.supergems.network;

import com.supergems.logic.SuperFormManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetwork {

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ToggleSuperFormPayload.TYPE, ToggleSuperFormPayload.STREAM_CODEC,
                ModNetwork::handleToggle);
    }

    private static void handleToggle(ToggleSuperFormPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                SuperFormManager.toggle(player);
            }
        });
    }

    private ModNetwork() {
    }
}
