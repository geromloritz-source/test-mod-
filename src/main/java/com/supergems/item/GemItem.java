package com.supergems.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/** Ein besonderer Diamant in einer bestimmten Farbe. */
public class GemItem extends Item {
    private final GemColor color;

    public GemItem(Properties properties, GemColor color) {
        super(properties);
        this.color = color;
    }

    public GemColor getColor() {
        return color;
    }

    /** Der Name wird in der Farbe des Diamanten angezeigt. */
    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(style -> style.withColor(color.getRgb()));
    }

    /** Verzauberungs-Glitzern, damit die Diamanten besonders wirken. */
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.supergems.gem").withStyle(ChatFormatting.GRAY));
    }
}
