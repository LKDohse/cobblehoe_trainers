package net.electricbudgie;

import io.netty.buffer.Unpooled;
import net.electricbudgie.networking.DialoguePayload;
import net.electricbudgie.screen.custom.DialogueScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CobbleHoeTrainersNetwork {
    // Channel/packet ID
    public static final Identifier OPEN_DIALOG = Identifier.of("yourmod", "open_dialog");

    public static void init(){
        PayloadTypeRegistry.playS2C().register(DialoguePayload.ID, DialoguePayload.CODEC);
    }

    // Send a packet from the server to a specific player
    public static void sendOpenDialogPacket(ServerPlayerEntity player, int entityId, String text) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entityId);
        buf.writeString(text);

        ServerPlayNetworking.send((ServerPlayerEntity) player, new DialoguePayload(text));
    }

    // Client-side packet registration
    public static void registerClientReceivers() {

        ClientPlayNetworking.registerGlobalReceiver(DialoguePayload.ID, (payload, context ) -> {
            String text = payload.dialogueText();

            context.client().execute(() -> {
                // Replace DialogScreen with your custom GUI screen
                context.client().setScreenAndRender(new DialogueScreen(Text.empty(), Text.of(text)));
            });
        });
    }
}
