package luxuriousmisfortunes.network.packets;

import luxuriousmisfortunes.api.Main;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Network {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);
    private static int packetId = 0;

    public static void registerPackets() {

        INSTANCE.registerMessage(
                PacketSubarmorEquipped.Handler.class,
                PacketSubarmorEquipped.class,
                packetId++,
                Side.SERVER
                );

        INSTANCE.registerMessage(
                PacketSpawnParticles.Handler.class,
                PacketSpawnParticles.class,
                packetId++,
                Side.CLIENT
                );
    }

    public static void sendToPlayerMP(IMessage msg, EntityPlayerMP player) {
        INSTANCE.sendTo(msg, player);
    }

    public static void sendToPlayerSP(IMessage msg) {
        INSTANCE.sendToServer(msg);
    }

}
