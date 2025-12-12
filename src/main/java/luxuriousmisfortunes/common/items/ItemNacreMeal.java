package luxuriousmisfortunes.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemBase;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.unfinished.MutationEntry;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNacreMeal extends ItemBase {

    public static String name = "nacre_agent";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public static final Map<BlockPos, MutationEntry> MUTATION_MAP = new HashMap<>();

    public ItemNacreMeal(String name) {
        super(name);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        Item item = null;
        ItemStack stackDrop = null;
        ItemStack stackHeld = player.getHeldItem(hand);

        ArrayList<Block> toCopy = new ArrayList<Block>();
        toCopy.add(Blocks.WATERLILY);
        toCopy.add(Blocks.VINE);
        toCopy.add(Blocks.NETHER_WART);

        ArrayList<Block> toGrow = new ArrayList<Block>();
        toGrow.add(Blocks.CACTUS);
        toGrow.add(Blocks.REEDS);

        for (Block i : toCopy) {
            if (block.equals(i)) {

                if (i.equals(Blocks.NETHER_WART)) {
                    item = Items.NETHER_WART;
                    //                    this.increaseMutationAt(worldIn, pos, worldIn.getTotalWorldTime());
                } else {
                    item = Item.getItemFromBlock(i);
                }

                stackDrop = new ItemStack(item, 1 + worldIn.rand.nextInt(2));

                ItemDye.spawnBonemealParticles(worldIn, pos, 0);
                stackHeld.shrink(1);

                if (!worldIn.isRemote) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stackDrop));
                    return EnumActionResult.SUCCESS;
                }
            }
        }


        for (Block i : toGrow) {
            if (block.equals(i)) {
                int height = 1 + worldIn.rand.nextInt(2);

                for (int x = 0; x < height; x++) {
                    if (worldIn.isAirBlock(pos.up(x + 1))) {
                        worldIn.setBlockState(pos.up(x + 1), i.getDefaultState());
                    }
                }

                ItemDye.spawnBonemealParticles(worldIn, pos, 0);

                stackHeld.shrink(1);

            }
        }


        return EnumActionResult.PASS;
    }

    //    public void increaseMutationAt(World world, BlockPos pos, long worldTime) {
    //
    //        int usedTimes = 0;
    //
    //        if (MUTATION_MAP.containsKey(pos)) {
    //            MutationEntry entry = MUTATION_MAP.get(pos);
    //            usedTimes = entry.getUsedTimes();
    //
    //            if (usedTimes == 10 && world.getTotalWorldTime() - entry.getLastUse() <= 40) {
    //                world.setBlockState(pos, BlockInit.PUPLY_STEM.getDefaultState());
    //                MUTATION_MAP.remove(pos);
    //            } else if (world.getTotalWorldTime() - entry.getLastUse() <= 40) {
    //                MutationEntry update = new MutationEntry(worldTime, usedTimes + 1);
    //                MUTATION_MAP.put(pos, update);
    //            }
    //        } else {
    //            MutationEntry update = new MutationEntry(worldTime, 1);
    //            MUTATION_MAP.put(pos, update);
    //        }
    //
    //    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
