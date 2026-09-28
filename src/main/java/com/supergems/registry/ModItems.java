package com.supergems.registry;

import com.supergems.SuperGems;
import com.supergems.item.GemColor;
import com.supergems.item.GemItem;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SuperGems.MODID);

    /** Alle sieben Diamanten, nach Farbe sortiert. */
    public static final Map<GemColor, DeferredItem<GemItem>> GEMS = new EnumMap<>(GemColor.class);

    static {
        for (GemColor color : GemColor.values()) {
            GEMS.put(color, ITEMS.register(color.itemName(),
                    () -> new GemItem(new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.EPIC)
                            .fireResistant(), color)));
        }
    }

    private ModItems() {
    }
}
