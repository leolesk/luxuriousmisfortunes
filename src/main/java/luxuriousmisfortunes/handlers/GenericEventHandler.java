package luxuriousmisfortunes.handlers;

import java.util.List;
import java.util.UUID;

import luxuriousmisfortunes.common.capabilities.CapabilityChewingGum;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.effects.EffectGluttony;
import luxuriousmisfortunes.common.items.ItemPyriteFishingRod;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.KeybindInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.packets.PacketSubarmorEquipped;
import luxuriousmisfortunes.util.IChewingGum;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import luxuriousmisfortunes.util.LostItemsHashHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class GenericEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;

        if (player.ticksExisted % 100 == 0) {
            if (player.hasCapability(CapabilityChewingGum.CAP, null)) {
                IChewingGum cap = player.getCapability(CapabilityChewingGum.CAP, null);

                if (cap.getTimeInitial() - player.getEntityWorld().getTotalWorldTime() >= 12000L) {
                    cap.setChewing(false);
                    cap.setTimeInitial(0L);

                    ItemStack stack = new ItemStack(ItemInit.GUM);
                    stack.setItemDamage(stack.getMaxDamage());
                    if (!player.addItemStackToInventory(stack)) {
                        player.dropItem(stack, false);
                    }
                } else {
                    if (player.getFoodStats().getFoodLevel() < 6) {
                        player.getFoodStats().setFoodLevel(6);
                    }
                }
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
    public static void onItemExplosion(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();

        List<Entity> list = event.getAffectedEntities();

        for (Entity e : list) {
            if (e instanceof EntityItem) {
                EntityItem item = (EntityItem)e;
                if (item.getItem().getItem().equals(ItemInit.COOKIE)) {

                    World worldIn = item.getEntityWorld();

                    ItemStack stackToSpawn = new ItemStack(ItemInit.COOKIE);
                    stackToSpawn.setItemDamage(1);
                    EntityItem entityToSpawn = new EntityItem(worldIn, item.posX, item.posY, item.posZ, stackToSpawn);

                    worldIn.spawnEntity(entityToSpawn);

                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent event) {
        if (KeybindInit.PUT_SUBARMOR_ON.isPressed()) {

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ISubarmorEquipped armor = player.getCapability(CapabilitySubarmorEquipped.CAP, null);

            if (armor != null) {
                if (!armor.isArmorOn()) {
                    if (player.getHeldItemMainhand().getItem().equals(ItemInit.SUBARMOR)) {
                        armor.setArmorOn(true);
                        Network.sendToPlayerSP(new PacketSubarmorEquipped(true));
                    }
                } else {
                    armor.setArmorOn(false);
                    Network.sendToPlayerSP(new PacketSubarmorEquipped(false));
                }
            }

        }
    }

    @SubscribeEvent
    public static void onIncorrectFoodEat(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        Item item = event.getItemStack().getItem();
        boolean pass = true;

        if (player.getActivePotionEffect(EffectInit.GLUTTONY) != null) {
            if (item instanceof ItemFood) {
                for (Item x : EffectGluttony.accepted) {
                    if (!item.equals(x)) {
                        pass = false;
                        break;
                    }
                }

                if (!pass) {
                    event.setCanceled(pass);
                    player.sendStatusMessage(new TextComponentString(I18n.format(EffectGluttony.complaint.getFormattedText())), true);
                }
            }
        }
    }






}
