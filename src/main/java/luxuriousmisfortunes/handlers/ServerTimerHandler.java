package luxuriousmisfortunes.handlers;

import java.time.LocalTime;
import java.util.UUID;

import luxuriousmisfortunes.common.items.ItemSubstance;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class ServerTimerHandler {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ItemSubstance.timers != null && !ItemSubstance.timers.isEmpty()) {
                for (UUID id : ItemSubstance.timers.keySet()) {
                    if (id != null) {
                        LocalTime time = ItemSubstance.timers.get(id);

                        if (event.world.getWorldTime() % 20 == 0) {
                            ItemSubstance.timers.put(id, time.minusSeconds(1));
                        }
                    }
                }
            }
        }
    }

}
