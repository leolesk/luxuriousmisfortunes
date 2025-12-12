package luxuriousmisfortunes.handlers;

import java.util.UUID;

import luxuriousmisfortunes.common.items.ItemPyriteFishingRod;
import luxuriousmisfortunes.util.LostItemsHashHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

@Mod.EventBusSubscriber
public class LostItemsHandler {

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        UUID id = event.getPlayer().getPersistentID();
        EntityItem entity = event.getEntityItem();

        LostItemsHashHelper.tryAddItemToMap(entity, id);
    }

    @SubscribeEvent
    public static void onItemPickup(ItemPickupEvent event) {
        EntityItem entity = event.getOriginalEntity();
        String name = entity.getThrower();

        if (name != null) {
            if (event.player.world.getPlayerEntityByName(name) != null) {

                UUID id = event.player.world.getPlayerEntityByName(name).getPersistentID();
                LostItemsHashHelper.tryDeleteItemFromMap(entity, id);

            }
        }
    }

    @SubscribeEvent
    public static void onItemFish(ItemFishedEvent event) {

        NonNullList<ItemStack> stacks = event.getDrops();
        EntityPlayer player = (EntityPlayer)event.getEntity();
        EntityFishHook hook = event.getHookEntity();
        World world = player.getEntityWorld();

        double d0 = player.posX - hook.posX;
        double d1 = player.posY - hook.posY;
        double d2 = player.posZ - hook.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        double d4 = 0.1D;

        ItemStack stackHeld = player.getHeldItemMainhand();

        if (stackHeld.getItem() instanceof ItemPyriteFishingRod) {
            stacks.clear();
            event.setCanceled(true);

            EntityItem stackEntity = LostItemsHashHelper.pullRandomItemFromMap(player.getPersistentID());
            if (stackEntity != null) {
                EntityItem entityItem = new EntityItem(world);
                entityItem.setItem(stackEntity.getItem());
                entityItem.setPosition(hook.posX, hook.posY, hook.posZ);
                entityItem.motionX = d0 * 0.1D;
                entityItem.motionY = d1 * 0.1D + MathHelper.sqrt(d3) * 0.08D;
                entityItem.motionZ = d2 * 0.1D;
                world.spawnEntity(entityItem);

                LostItemsHashHelper.tryDeleteItemFromMap(stackEntity, player.getPersistentID());
            } else if (world.rand.nextInt(10) > 7) {
                EntityGuardian entity = new EntityGuardian(world);
                entity.setPosition(hook.posX, hook.posY, hook.posZ);
                entity.motionX = d0 * 0.1D;
                entity.motionY = d1 * 0.1D + MathHelper.sqrt(d3) * 0.08D;
                entity.motionZ = d2 * 0.1D;
                world.spawnEntity(entity);
            }

            event.damageRodBy(1);
        }
    }

}
