package com.supergems.logic;

import com.supergems.SuperGems;
import com.supergems.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Game-Bus-Events: Tick-Logik und Fallschaden-Schutz. */
@EventBusSubscriber(modid = SuperGems.MODID)
public final class SuperFormEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SuperFormManager.tick(player);
        }
    }

    /** Kein Fallschaden in der Super-Form. */
    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player
                && player.getData(ModAttachments.SUPER_FORM).active()) {
            event.setCanceled(true);
        }
    }

    private SuperFormEvents() {
    }
}
