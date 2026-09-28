package com.supergems.registry;

import com.supergems.SuperGems;
import com.supergems.item.GemColor;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SuperGems.MODID);

    public static final Supplier<CreativeModeTab> SUPER_GEMS_TAB = TABS.register("super_gems",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.supergems"))
                    .icon(() -> new ItemStack(ModItems.GEMS.get(GemColor.RED).get()))
                    .displayItems((parameters, output) ->
                            ModItems.GEMS.values().forEach(item -> output.accept(item.get())))
                    .build());

    private ModCreativeTabs() {
    }
}
