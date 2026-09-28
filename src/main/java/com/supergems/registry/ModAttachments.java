package com.supergems.registry;

import com.supergems.SuperGems;
import com.supergems.state.SuperFormState;
import java.util.function.Supplier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SuperGems.MODID);

    /**
     * Zustand der Super-Form pro Spieler.
     * - serialize: wird mit dem Spieler gespeichert
     * - sync: wird automatisch an den Client (für HUD) und andere Spieler gesendet
     * - kein copyOnDeath: nach dem Tod startet der Spieler wieder im Normalzustand
     */
    public static final Supplier<AttachmentType<SuperFormState>> SUPER_FORM = ATTACHMENTS.register("super_form",
            () -> AttachmentType.builder(() -> SuperFormState.DEFAULT)
                    .serialize(SuperFormState.CODEC)
                    .sync(SuperFormState.STREAM_CODEC)
                    .build());

    private ModAttachments() {
    }
}
