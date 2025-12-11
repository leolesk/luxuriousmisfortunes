package luxuriousmisfortunes.unfinished;

import java.util.Random;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockBase;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOreDepleted extends Block {

    public static final PropertyInteger TIMES_RESTORED = PropertyInteger.create("times_restored", 0, 2);
    public static final PropertyInteger STATUS_RESTORED = PropertyInteger.create("status_restored", 0, 2);

    private Block associated = null;

    public BlockOreDepleted(Block associated) {
        super(Material.ROCK);

        String name = associated.getRegistryName().getPath() + "_" + "depleted";

        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(Main.tabMod);

        this.setHardness(this.blockHardness);

        this.setTickRandomly(true);
        this.associated = associated;

        BlockInit.BLOCKS.add(this);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {

        ItemStack stack = playerIn.getHeldItemMainhand();
        IBlockState state = worldIn.getBlockState(pos);

        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                if (stack.getItem().equals(ItemInit.MATERIAL_PYRITE)) {
                    if (state.getValue(STATUS_RESTORED) == 0 && state.getValue(TIMES_RESTORED) != 2) {
                        worldIn.setBlockState(pos, this.getDefaultState().withProperty(STATUS_RESTORED, 1));
                        stack.shrink(1);
                    }
                }
            }
        }
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getWorldTime() % 100 == 0) {
            if (state.getValue(STATUS_RESTORED) == 1) {
                if (worldIn.rand.nextInt(10) > 9) {
                    int value = state.getValue(TIMES_RESTORED);
                    IBlockState toPlace = this.getDefaultState()
                            .withProperty(TIMES_RESTORED, value + 1)
                            .withProperty(STATUS_RESTORED, 2);
                    worldIn.setBlockState(pos, toPlace);
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (state.getValue(STATUS_RESTORED) == 2) {
            if (this.associated == Blocks.COAL_ORE)
                return Items.COAL;
            else if (this.associated == Blocks.DIAMOND_ORE)
                return Items.DIAMOND;
            else if (this.associated == Blocks.LAPIS_ORE)
                return Items.DYE;
            else if (this.associated == Blocks.EMERALD_ORE)
                return Items.EMERALD;
            else
                return this.associated == Blocks.QUARTZ_ORE ? Items.QUARTZ : Item.getItemFromBlock(this.associated);
        } else
            return Item.getItemFromBlock(Blocks.COBBLESTONE);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))
        {
            int i = random.nextInt(fortune + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        } else
            return this.quantityDropped(random);
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        if (state.getValue(STATUS_RESTORED) == 2) {
            Random rand = world instanceof World ? ((World)world).rand : new Random();
            if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
            {
                int i = 0;

                if (this.associated == Blocks.COAL_ORE)
                {
                    i = MathHelper.getInt(rand, 0, 2);
                }
                else if (this.associated == Blocks.DIAMOND_ORE)
                {
                    i = MathHelper.getInt(rand, 3, 7);
                }
                else if (this.associated == Blocks.EMERALD_ORE)
                {
                    i = MathHelper.getInt(rand, 3, 7);
                }
                else if (this.associated == Blocks.LAPIS_ORE)
                {
                    i = MathHelper.getInt(rand, 2, 5);
                }
                else if (this.associated == Blocks.QUARTZ_ORE)
                {
                    i = MathHelper.getInt(rand, 2, 5);
                }

                return i;
            }
        }
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TIMES_RESTORED, STATUS_RESTORED});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int a = meta % 3;
        int b = meta / 3;

        return this.getDefaultState()
                .withProperty(TIMES_RESTORED, a)
                .withProperty(STATUS_RESTORED, b);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int a = state.getValue(TIMES_RESTORED);
        int b = state.getValue(STATUS_RESTORED);

        return a + b * 3;
    }

}
