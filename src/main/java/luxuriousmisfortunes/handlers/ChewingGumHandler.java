package luxuriousmisfortunes.handlers;

import luxuriousmisfortunes.common.capabilities.CapabilityChewingGum;
import luxuriousmisfortunes.util.IChewingGum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class ChewingGumHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;

        if (player.hasCapability(CapabilityChewingGum.CAP, null)) {
            IChewingGum cap = player.getCapability(CapabilityChewingGum.CAP, null);

            if (player.ticksExisted % 100 == 0) {
                if (player.getFoodStats().getFoodLevel() < 8) {
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        if (player.inventory.getStackInSlot(i).getItem().equals(Items.SUGAR)) {
                            ItemStack stack = player.inventory.getStackInSlot(i);

                            player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() + 1);
                            stack.shrink(1);
                            break;
                        }
                    }
                }
            }
        }
    }

}
