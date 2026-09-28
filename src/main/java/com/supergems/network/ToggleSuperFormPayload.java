package com.supergems.network;

import com.supergems.SuperGems;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client -> Server: "Der Spieler hat die Super-Form-Taste gedrückt." Enthält keine Daten. */
public record ToggleSuperFormPayload() implements CustomPacketPayload {
    public static final Type<ToggleSuperFormPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SuperGems.MODID, "toggle_super_form"));

    public static final StreamCodec<ByteBuf, ToggleSuperFormPayload> STREAM_CODEC =
            StreamCodec.unit(new ToggleSuperFormPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
