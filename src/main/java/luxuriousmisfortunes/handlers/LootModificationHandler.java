package luxuriousmisfortunes.handlers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class LootModificationHandler {

    @SubscribeEvent
    public static void onModifiedLootRoll(PlayerContainerEvent.Open event) {

        if (event.getEntityPlayer().world.isRemote) return;

        EntityPlayer player = event.getEntityPlayer();
        PotionEffect effect = player.getActivePotionEffect(EffectInit.FORTUNES_GRACE);

        if (player.getActivePotionEffect(EffectInit.FORTUNES_GRACE) != null) {

            int amplifier = effect.getAmplifier() > 1 ? effect.getAmplifier() : 2;
            int bonusRollsModifier = effect.getAmplifier() == 0 ? 5 : effect.getAmplifier() * 10;

            if (event.getContainer() instanceof ContainerChest) {

                ContainerChest container = (ContainerChest)event.getContainer();

                IInventory tile = container.getLowerChestInventory();

                if (tile instanceof TileEntityChest) {

                    TileEntityChest chest = (TileEntityChest)tile;

                    ItemStack empty = new ItemStack(Items.AIR);

                    if (!chest.getTileData().getString("LootTableSeal").isEmpty()) {

                        for (int i = 0; i < chest.getSizeInventory(); i++) {
                            chest.setInventorySlotContents(i, empty);
                        }

                        LootTable lootTable = chest.getWorld().getLootTableManager().getLootTableFromLocation(new ResourceLocation(chest.getTileData().getString("LootTableSeal")));

                        long seed = chest.getTileData().getLong("LootTableSeed");

                        chest.getTileData().setString("LootTableSeal", "");

                        Random random;

                        if (seed == 0L)
                        {
                            random = new Random();
                        }
                        else
                        {
                            random = new Random(seed);
                        }

                        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)chest.getWorld());

                        lootcontext$builder.withPlayer(player);

                        fillModifiedInventory(tile, lootTable, random, amplifier, bonusRollsModifier, lootcontext$builder.build());

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;

            if (chest.getLootTable() != null) {
                chest.getTileData().setString("LootTableSeal", chest.getLootTable().toString());
            }
        }
    }

    public static void fillModifiedInventory(IInventory inventory, LootTable table, Random rand, int amplifier, int bonusRollsModifier, LootContext context) {

        List<ItemStack> list = generateLootForPoolsModified(rand, amplifier, bonusRollsModifier, context, table);
        List<Integer> listEmpty = getEmptySlotsRandomized(inventory, rand);
        shuffleItems(list, rand);

        for (ItemStack itemstack : list)
        {
            if (itemstack.isEmpty())
            {
                inventory.setInventorySlotContents(listEmpty.remove(listEmpty.size() - 1).intValue(), ItemStack.EMPTY);
            }
            else
            {
                inventory.setInventorySlotContents(listEmpty.remove(listEmpty.size() - 1).intValue(), itemstack);
            }
        }

    }

    public static void shuffleItems(List<ItemStack> stacks, Random rand)
    {
        List<ItemStack> list = Lists.<ItemStack>newArrayList();
        Iterator<ItemStack> iterator = stacks.iterator();

        while (iterator.hasNext())
        {
            ItemStack itemstack = iterator.next();

            if (itemstack.isEmpty())
            {
                iterator.remove();
            }
            else if (itemstack.getCount() > 1)
            {
                list.add(itemstack);
                iterator.remove();
            }
        }
    }

    public static List<ItemStack> generateLootForPoolsModified(Random rand, int amplifier, int bonusRollsModifier, LootContext context, LootTable table)
    {
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        if (context.addLootTable(table))
        {
            for (LootPool lootpool : getPoolsIndirectly(table))
            {
                generateLootModified(list, rand, amplifier, bonusRollsModifier, lootpool, context, table);
            }

            context.removeLootTable(table);
        }

        return list;
    }

    public static List<LootPool> getPoolsIndirectly(LootTable table) {
        try {

            Field f;
            f = LootTable.class.getDeclaredField("pools");
            f.setAccessible(true);
            List<LootPool> pools = (List<LootPool>) f.get(table);
            return pools;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<LootEntry> getEntriesIndirectly(LootPool pool) {
        try {

            Field f;
            f = LootPool.class.getDeclaredField("lootEntries");
            f.setAccessible(true);
            List<LootEntry> entries = (List<LootEntry>) f.get(pool);
            return entries;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void generateLootModified(Collection<ItemStack> stacks, Random rand, int amplifier, int bonusRollsModifier, LootPool pool, LootContext context, LootTable table)
    {

        int i = pool.getRolls().generateInt(rand) * bonusRollsModifier;

        for (int j = 0; j < i; ++j)
        {
            createLootRoll(stacks, rand, amplifier, context, pool, table);
        }
    }

    public static void createLootRoll(Collection<ItemStack> stacks, Random rand, int amplifier, LootContext context, LootPool pool, LootTable table)
    {
        List<LootEntry> list = Lists.<LootEntry>newArrayList();
        int i = 0;

        for (LootEntry lootentry : getEntriesIndirectly(pool))
        {
            int j = getEffectiveWeightModified(lootentry, amplifier, pool);

            if (j > 0)
            {
                list.add(lootentry);
                i += j;
            }
        }

        if (i != 0 && !list.isEmpty())
        {
            int k = rand.nextInt(i);

            for (LootEntry lootentry1 : list)
            {
                k -= getEffectiveWeightModified(lootentry1, amplifier, pool);

                if (k < 0)
                {
                    lootentry1.addLoot(stacks, rand, context);
                    return;
                }
            }
        }
    }

    public static int getEffectiveWeightModified(LootEntry entry, int amplifier, LootPool pool)
    {
        int clearWeight = entry.getEffectiveWeight(1);

        int maxWeight = clearWeight;
        for (LootEntry e : getEntriesIndirectly(pool)) {
            int w = e.getEffectiveWeight(1);
            if (w > maxWeight) {
                maxWeight = w;
            }
        }

        double rarityFactor = (double) maxWeight / (double) clearWeight;

        double modifiedWeight = clearWeight * (1.0 + amplifier * 0.2 * (rarityFactor - 1.0));

        if (modifiedWeight < 1) {
            modifiedWeight = 1;
        }

        return (int) modifiedWeight;

    }

    public static List<Integer> getEmptySlotsRandomized(IInventory inventory, Random rand)
    {
        List<Integer> list = Lists.<Integer>newArrayList();

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            if (inventory.getStackInSlot(i).isEmpty())
            {
                list.add(Integer.valueOf(i));
            }
        }

        Collections.shuffle(list, rand);
        return list;
    }

}
