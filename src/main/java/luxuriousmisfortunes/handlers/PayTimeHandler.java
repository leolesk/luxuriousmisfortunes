package luxuriousmisfortunes.handlers;

import java.time.LocalTime;

import luxuriousmisfortunes.common.capabilities.CapabilityPayTime;
import luxuriousmisfortunes.util.IPayTime;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class PayTimeHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        IPayTime cap = player.getCapability(CapabilityPayTime.CAP, null);

        if (cap != null && !PayTimeHash.payment_record.containsKey(player.getUniqueID())) {

            LocalTime value = cap.getPayTime();
            PayTimeHash.payment_record.put(player.getUniqueID(), value);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        IPayTime cap = player.getCapability(CapabilityPayTime.CAP, null);

        if (cap != null) {
            LocalTime value = PayTimeHash.payment_record.get(player.getUniqueID());
            PayTimeHash.payment_record.put(player.getUniqueID(), value);
        }
    }

}
