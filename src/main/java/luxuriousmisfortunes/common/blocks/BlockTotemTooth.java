package luxuriousmisfortunes.common.blocks;

import java.util.ArrayList;

import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemTooth extends BlockTotemStructure {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private static final double T = 2.0 / 3.0;
    static final AxisAlignedBB BB_NORTH =
            new AxisAlignedBB(
                    0.0, 0.0, 1.0 - T,
                    1.0, 1.0, 1.0
                    );
    static final AxisAlignedBB BB_SOUTH =
            new AxisAlignedBB(
                    0.0, 0.0, 0.0,
                    1.0, 1.0, T
                    );
    static final AxisAlignedBB BB_WEST =
            new AxisAlignedBB(
                    1.0 - T, 0.0, 0.0,
                    1.0,     1.0, 1.0
                    );
    static final AxisAlignedBB BB_EAST =
            new AxisAlignedBB(
                    0.0, 0.0, 0.0,
                    T,   1.0, 1.0
                    );

    public static String name = "totem_tooth";

    public BlockTotemTooth(String name) {
        super(name, Material.ROCK);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        if (side != EnumFacing.UP && side != EnumFacing.DOWN)
            return true;

        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH: return BB_NORTH;
            case SOUTH: return BB_SOUTH;
            case WEST:  return BB_WEST;
            case EAST:  return BB_EAST;
            default:    return FULL_BLOCK_AABB;
        }
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Item itemHeld = playerIn.getHeldItem(hand).getItem();

        if (playerIn.isCreative()) {
            for (int i = 0; i < ItemInit.acceptables.length; i++) {
                if (itemHeld.equals(ItemInit.acceptables[i])) {
                    worldIn.setBlockState(pos, BlockInit.TOTEM_TOOTH_HOLDING.getDefaultState()
                            .withProperty(FACING, state.getValue(FACING))
                            .withProperty(BlockTotemToothVariant.HOLDING, i));
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        return this.getDefaultState().withProperty(FACING, facing);
    }
}
