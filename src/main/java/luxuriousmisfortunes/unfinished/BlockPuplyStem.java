package luxuriousmisfortunes.unfinished;

import java.util.List;
import java.util.Random;

import luxuriousmisfortunes.common.basic.BlockBase;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPuplyStem extends BlockBase {

    public static String name = "pulpy_stem";

    public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, 6);

    //if you drop a whole stack, it will consume it and not grow

    public BlockPuplyStem(String name) {
        super(name, Material.CLAY);

        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(GROWTH, 0));
    }

    //    @Override
    //    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //
    //        if (state.getValue(GROWTH) == 6) {
    //            drops.add(new ItemStack(ItemInit.PSEUDO_MEAT, 6));
    //        } else {
    //            drops.clear();
    //        }
    //    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {

            if (state.getValue(GROWTH) != 6) {

                AxisAlignedBB aabb = new AxisAlignedBB(
                        pos.getX(), pos.getY() + 1, pos.getZ(),
                        pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1
                        );

                List<Entity> entitiesAbove = world.getEntitiesWithinAABB(Entity.class, aabb);

                for (Entity e : entitiesAbove) {
                    if (e instanceof EntityItem) {
                        EntityItem item = (EntityItem)e;

                        if (item.getItem().getItem().equals(Items.ROTTEN_FLESH)) {

                            if (world.rand.nextLong() > 0.8) {

                                int growth = state.getValue(GROWTH);

                                world.setBlockState(pos, state.withProperty(GROWTH, growth + 1));
                                e.setDead();
                                break;
                            }

                        }
                    }
                }


                world.scheduleBlockUpdate(pos, this, 200, 0);
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {GROWTH});
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(GROWTH);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int range = MathHelper.clamp(meta, 0, 6);
        return this.getDefaultState().withProperty(GROWTH, range);
    }



}
