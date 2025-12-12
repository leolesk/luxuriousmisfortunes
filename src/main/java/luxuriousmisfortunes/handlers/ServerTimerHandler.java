package luxuriousmisfortunes.handlers;

import java.time.LocalTime;
import java.util.UUID;

import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class ServerTimerHandler {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote) {
            if (event.phase == TickEvent.Phase.END) {
                if (PayTimeHash.payment_record != null && !PayTimeHash.payment_record.isEmpty()) {
                    for (UUID id : PayTimeHash.payment_record.keySet()) {
                        if (id != null) {
                            LocalTime time = PayTimeHash.payment_record.get(id);

                            if (event.world.getWorldTime() % 20 == 0) {
                                PayTimeHash.payment_record.put(id, time.minusSeconds(1));

                                if (PayTimeHash.payment_record.get(id).equals(LocalTime.of(0, 0))) {
                                    PayTimeHash.payment_record.remove(id);

                                    if (event.world.getPlayerEntityByUUID(id) != null) {
                                        EntityPlayer player = event.world.getPlayerEntityByUUID(id);

                                        BlockPos pos = player.getPosition();

                                        EntityLightningBolt bolt = new EntityLightningBolt(event.world, pos.getX(), pos.getY(), pos.getZ(), false);
                                        event.world.addWeatherEffect(bolt);

                                        player.addPotionEffect(new PotionEffect(PayTimeHash.acceptables[event.world.rand.nextInt(PayTimeHash.acceptables.length)], Integer.MAX_VALUE, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
