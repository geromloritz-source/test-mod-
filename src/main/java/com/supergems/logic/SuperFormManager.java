package com.supergems.logic;

import com.supergems.SuperGemsConfig;
import com.supergems.item.GemColor;
import com.supergems.item.GemItem;
import com.supergems.registry.ModAttachments;
import com.supergems.state.SuperFormState;
import java.util.EnumSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

/** Die gesamte Server-Logik der Super-Form. */
public final class SuperFormManager {
    private static final float DEFAULT_FLY_SPEED = 0.05F;
    private static final DustParticleOptions GOLD_DUST =
            new DustParticleOptions(new Vector3f(1.0F, 0.84F, 0.0F), 1.2F);

    private SuperFormManager() {
    }

    /** Wird vom Tastendruck (Netzwerk-Payload) aufgerufen. */
    public static void toggle(ServerPlayer player) {
        SuperFormState state = player.getData(ModAttachments.SUPER_FORM);
        long now = player.level().getGameTime();

        // Form ist aktiv -> beenden
        if (state.active()) {
            deactivate(player, false);
            return;
        }

        // Diamanten laden noch auf
        long cooldownLeft = state.cooldownRemaining(now);
        if (cooldownLeft > 0) {
            player.displayClientMessage(
                    Component.translatable("message.supergems.cooldown", (cooldownLeft + 19) / 20), true);
            return;
        }

        // Es fehlen Diamanten
        if (!hasAllGems(player)) {
            player.displayClientMessage(Component.translatable("message.supergems.need_all"), true);
            return;
        }

        activate(player, state, now);
    }

    /** Läuft jeden Server-Tick pro Spieler (sehr günstig, wenn die Form inaktiv ist). */
    public static void tick(ServerPlayer player) {
        SuperFormState state = player.getData(ModAttachments.SUPER_FORM);
        if (!state.active()) {
            return;
        }

        long now = player.level().getGameTime();

        // Energie leer -> zurückverwandeln
        if (now >= state.activeUntil()) {
            deactivate(player, false);
            return;
        }

        // Alle 10 Ticks prüfen, ob noch alle 7 Diamanten im Inventar sind
        if (now % 10 == 0 && !hasAllGems(player)) {
            deactivate(player, true);
            return;
        }

        // Effekte und Flug regelmäßig erneuern
        if (now % 20 == 0) {
            applyEffects(player);
            enableFlight(player);
        }

        spawnParticles(player, now);
    }

    /** Prüft, ob der Spieler alle sieben verschiedenen Diamanten im Inventar hat. */
    public static boolean hasAllGems(Player player) {
        EnumSet<GemColor> found = EnumSet.noneOf(GemColor.class);
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).getItem() instanceof GemItem gem) {
                found.add(gem.getColor());
            }
        }
        return found.size() == GemColor.values().length;
    }

    // ------------------------------------------------------------------

    private static void activate(ServerPlayer player, SuperFormState state, long now) {
        int duration = SuperGemsConfig.DURATION_SECONDS.get() * 20;
        player.setData(ModAttachments.SUPER_FORM, state.activate(now, duration));

        applyEffects(player);
        enableFlight(player);

        ServerLevel level = player.serverLevel();
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.4F);
        level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.6F, 1.5F);
        level.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.0, player.getZ(), 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0, player.getZ(),
                80, 0.4, 0.8, 0.4, 0.25);
    }

    private static void deactivate(ServerPlayer player, boolean lostGems) {
        SuperFormState state = player.getData(ModAttachments.SUPER_FORM);
        long now = player.level().getGameTime();
        int cooldown = SuperGemsConfig.COOLDOWN_SECONDS.get() * 20;
        player.setData(ModAttachments.SUPER_FORM, state.deactivate(now, cooldown));

        disableFlight(player);

        // Sanfte Landung, falls die Form mitten in der Luft endet
        player.fallDistance = 0.0F;
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, true, false, true));

        ServerLevel level = player.serverLevel();
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.POOF, player.getX(), player.getY() + 1.0, player.getZ(),
                30, 0.4, 0.8, 0.4, 0.05);

        if (lostGems) {
            player.displayClientMessage(Component.translatable("message.supergems.lost_gems"), true);
        }
    }

    private static void applyEffects(ServerPlayer player) {
        addEffect(player, MobEffects.MOVEMENT_SPEED, SuperGemsConfig.SPEED_LEVEL.get());
        addEffect(player, MobEffects.DAMAGE_BOOST, SuperGemsConfig.STRENGTH_LEVEL.get());
        addEffect(player, MobEffects.REGENERATION, SuperGemsConfig.REGENERATION_LEVEL.get());
        addEffect(player, MobEffects.DAMAGE_RESISTANCE, SuperGemsConfig.RESISTANCE_LEVEL.get());
        // Feuer- und Lava-Immunität
        addEffect(player, MobEffects.FIRE_RESISTANCE, 1);
    }

    /** level 0 = aus, level 1 = Stufe I (Amplifier 0) usw. */
    private static void addEffect(ServerPlayer player, Holder<MobEffect> effect, int level) {
        if (level <= 0) {
            return;
        }
        player.addEffect(new MobEffectInstance(effect, 60, level - 1, true, false, true));
    }

    private static void enableFlight(ServerPlayer player) {
        if (!SuperGemsConfig.ALLOW_FLIGHT.get()) {
            return;
        }
        Abilities abilities = player.getAbilities();
        float speed = SuperGemsConfig.FLY_SPEED.get().floatValue();
        if (!abilities.mayfly || abilities.getFlyingSpeed() != speed) {
            abilities.mayfly = true;
            abilities.setFlyingSpeed(speed);
            player.onUpdateAbilities();
        }
    }

    private static void disableFlight(ServerPlayer player) {
        Abilities abilities = player.getAbilities();
        // Kreativ- und Zuschauer-Spieler behalten ihren Flug
        if (!player.isCreative() && !player.isSpectator()) {
            abilities.mayfly = false;
            abilities.flying = false;
        }
        abilities.setFlyingSpeed(DEFAULT_FLY_SPEED);
        player.onUpdateAbilities();
    }

    /** Goldener Schimmer plus sieben kreisende Diamanten in ihren Farben. */
    private static void spawnParticles(ServerPlayer player, long now) {
        ServerLevel level = player.serverLevel();

        if (now % 2 == 0) {
            level.sendParticles(GOLD_DUST, player.getX(), player.getY() + 1.0, player.getZ(),
                    4, 0.35, 0.6, 0.35, 0.0);
        }

        if (now % 3 == 0) {
            GemColor[] colors = GemColor.values();
            for (int i = 0; i < colors.length; i++) {
                double angle = now * 0.12 + i * (2 * Math.PI / colors.length);
                double x = player.getX() + Math.cos(angle) * 0.9;
                double z = player.getZ() + Math.sin(angle) * 0.9;
                double y = player.getY() + 1.0 + Math.sin(now * 0.08 + i) * 0.4;
                level.sendParticles(new DustParticleOptions(colors[i].toVector(), 1.0F), x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }
}
