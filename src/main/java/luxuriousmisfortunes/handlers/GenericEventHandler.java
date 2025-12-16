package luxuriousmisfortunes.handlers;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import luxuriousmisfortunes.common.capabilities.CapabilityChewingGum;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.capabilities.CapabilityPayTime;
import luxuriousmisfortunes.common.effects.EffectGluttony;
import luxuriousmisfortunes.common.items.ItemPyriteFishingRod;
import luxuriousmisfortunes.common.items.ItemNacreSubarmor;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.KeybindInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.packets.PacketSubarmorEquipped;
import luxuriousmisfortunes.util.IChewingGum;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import luxuriousmisfortunes.util.IPayTime;
import luxuriousmisfortunes.util.LostItemsHashHelper;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class GenericEventHandler {

    @SubscribeEvent
    public static void onItemExplosion(ExplosionEvent.Detonate event) {

        List<Entity> list = event.getAffectedEntities();

        for (Entity e : list) {
            if (e instanceof EntityItem) {
                EntityItem item = (EntityItem)e;
                if (item.getItem().getItem().equals(ItemInit.COOKIE)) {

                    int amount = item.getItem().getCount();

                    World worldIn = item.getEntityWorld();

                    ItemStack stackToSpawn = new ItemStack(ItemInit.COOKIE, amount);
                    stackToSpawn.setItemDamage(1);
                    EntityItem entityToSpawn = new EntityItem(worldIn, item.posX, item.posY, item.posZ, stackToSpawn);

                    worldIn.spawnEntity(entityToSpawn);

                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDestroyItem(PlayerDestroyItemEvent event) {

        ItemStack original = event.getOriginal();
        EntityPlayer player = event.getEntityPlayer();

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i).getItem().equals(ItemInit.PATCH)) {
                ItemStack stack = player.inventory.getStackInSlot(i);

                original.setItemDamage(original.getItemDamage() - 20);
                if (!player.addItemStackToInventory(original.copy())) {
                    player.dropItem(original.copy(), false);
                }
                stack.shrink(1);
                break;
            }
        }

    }

    @SubscribeEvent
    public static void onIncorrectFoodEat(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        Item item = event.getItemStack().getItem();
        boolean pass = false;

        if (player.getActivePotionEffect(EffectInit.GLUTTONY) != null) {
            if (item instanceof ItemFood) {
                for (Item x : EffectGluttony.accepted) {
                    if (item.equals(x)) {
                        pass = true;
                        break;
                    }
                }

                if (!pass) {
                    event.setCanceled(true);
                    player.sendStatusMessage(new TextComponentString(I18n.format(EffectGluttony.complaint.getFormattedText())), true);
                }
            }
        }
    }






}
