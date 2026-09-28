package com.supergems;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Einstellungen der Mod (config/supergems-common.toml). */
public final class SuperGemsConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue DURATION_SECONDS = BUILDER
            .comment("Wie lange die Super-Form anhält (in Sekunden).")
            .defineInRange("durationSeconds", 60, 5, 3600);

    public static final ModConfigSpec.IntValue COOLDOWN_SECONDS = BUILDER
            .comment("Abklingzeit, in der sich die Diamanten wieder aufladen (in Sekunden).")
            .defineInRange("cooldownSeconds", 300, 0, 86400);

    public static final ModConfigSpec.BooleanValue ALLOW_FLIGHT = BUILDER
            .comment("Darf man in der Super-Form fliegen?")
            .define("allowFlight", true);

    public static final ModConfigSpec.DoubleValue FLY_SPEED = BUILDER
            .comment("Fluggeschwindigkeit in der Super-Form (Vanilla-Creative: 0.05).")
            .defineInRange("flySpeed", 0.1D, 0.05D, 0.5D);

    public static final ModConfigSpec.IntValue SPEED_LEVEL = BUILDER
            .comment("Stufe von Geschwindigkeit (0 = aus, 3 = Speed III).")
            .defineInRange("speedLevel", 3, 0, 10);

    public static final ModConfigSpec.IntValue STRENGTH_LEVEL = BUILDER
            .comment("Stufe von Stärke (0 = aus).")
            .defineInRange("strengthLevel", 2, 0, 10);

    public static final ModConfigSpec.IntValue REGENERATION_LEVEL = BUILDER
            .comment("Stufe von Regeneration (0 = aus).")
            .defineInRange("regenerationLevel", 2, 0, 10);

    public static final ModConfigSpec.IntValue RESISTANCE_LEVEL = BUILDER
            .comment("Stufe von Resistenz (0 = aus).")
            .defineInRange("resistanceLevel", 2, 0, 10);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private SuperGemsConfig() {
    }
}
