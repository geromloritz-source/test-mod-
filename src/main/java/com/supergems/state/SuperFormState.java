package com.supergems.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Unveränderlicher Zustand der Super-Form.
 * Statt jeden Tick zu zählen, speichern wir absolute Spielzeit-Zeitpunkte.
 * Dadurch muss der Zustand nur bei Änderungen synchronisiert werden.
 *
 * @param active           ist die Super-Form gerade aktiv?
 * @param activeUntil      Spielzeit (Ticks), bis zu der die Form anhält
 * @param activeDuration   Gesamtdauer der aktuellen Form in Ticks (für die Energieanzeige)
 * @param cooldownUntil    Spielzeit (Ticks), bis zu der die Diamanten aufladen
 * @param cooldownDuration Gesamtdauer der Abklingzeit in Ticks (für die Ladeanzeige)
 */
public record SuperFormState(boolean active, long activeUntil, int activeDuration,
                             long cooldownUntil, int cooldownDuration) {

    public static final SuperFormState DEFAULT = new SuperFormState(false, 0L, 0, 0L, 0);

    public static final Codec<SuperFormState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("active").forGetter(SuperFormState::active),
            Codec.LONG.fieldOf("activeUntil").forGetter(SuperFormState::activeUntil),
            Codec.INT.fieldOf("activeDuration").forGetter(SuperFormState::activeDuration),
            Codec.LONG.fieldOf("cooldownUntil").forGetter(SuperFormState::cooldownUntil),
            Codec.INT.fieldOf("cooldownDuration").forGetter(SuperFormState::cooldownDuration)
    ).apply(instance, SuperFormState::new));

    public static final StreamCodec<ByteBuf, SuperFormState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SuperFormState::active,
            ByteBufCodecs.VAR_LONG, SuperFormState::activeUntil,
            ByteBufCodecs.VAR_INT, SuperFormState::activeDuration,
            ByteBufCodecs.VAR_LONG, SuperFormState::cooldownUntil,
            ByteBufCodecs.VAR_INT, SuperFormState::cooldownDuration,
            SuperFormState::new);

    /** Verbleibende Ticks der Super-Form. */
    public long activeRemaining(long now) {
        return active ? Math.max(0L, activeUntil - now) : 0L;
    }

    /** Verbleibende Ticks der Abklingzeit. */
    public long cooldownRemaining(long now) {
        return Math.max(0L, cooldownUntil - now);
    }

    public SuperFormState activate(long now, int durationTicks) {
        return new SuperFormState(true, now + durationTicks, durationTicks, cooldownUntil, cooldownDuration);
    }

    public SuperFormState deactivate(long now, int cooldownTicks) {
        return new SuperFormState(false, 0L, 0, now + cooldownTicks, cooldownTicks);
    }
}
