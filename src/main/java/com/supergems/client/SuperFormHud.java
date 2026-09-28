package com.supergems.client;

import com.supergems.item.GemColor;
import com.supergems.registry.ModAttachments;
import com.supergems.state.SuperFormState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** Zeichnet die Energie- bzw. Ladeanzeige über der Hotbar. */
public final class SuperFormHud {
    private static final int BAR_WIDTH = 110;
    private static final int BAR_HEIGHT = 7;

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.options.hideGui) {
            return;
        }

        SuperFormState state = mc.player.getData(ModAttachments.SUPER_FORM);
        long now = mc.level.getGameTime();

        float progress;
        int color;
        Component label;

        if (state.active()) {
            long remaining = state.activeRemaining(now);
            progress = state.activeDuration() > 0 ? remaining / (float) state.activeDuration() : 0F;
            color = 0xFFFFC107; // Gold
            label = Component.translatable("hud.supergems.super_form", (remaining + 19) / 20);
        } else {
            long remaining = state.cooldownRemaining(now);
            if (remaining <= 0) {
                return; // nichts anzeigen, wenn bereit
            }
            progress = 1F - remaining / (float) Math.max(1, state.cooldownDuration());
            color = 0xFF4FC3F7; // Hellblau
            label = Component.translatable("hud.supergems.recharging", (remaining + 19) / 20);
        }

        int x = (graphics.guiWidth() - BAR_WIDTH) / 2;
        int y = graphics.guiHeight() - 66;

        // Text
        graphics.drawCenteredString(mc.font, label, graphics.guiWidth() / 2, y - 11, 0xFFFFFF);

        // Balken: Rahmen, Hintergrund, Füllung
        graphics.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, 0xFF000000);
        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF333333);
        graphics.fill(x, y, x + (int) (BAR_WIDTH * progress), y + BAR_HEIGHT, color);

        // Sieben kleine Farbsegmente unter dem Balken
        GemColor[] colors = GemColor.values();
        int segment = BAR_WIDTH / colors.length;
        for (int i = 0; i < colors.length; i++) {
            graphics.fill(x + i * segment + 1, y + BAR_HEIGHT + 2,
                    x + (i + 1) * segment - 1, y + BAR_HEIGHT + 5, 0xFF000000 | colors[i].getRgb());
        }
    }

    private SuperFormHud() {
    }
}
