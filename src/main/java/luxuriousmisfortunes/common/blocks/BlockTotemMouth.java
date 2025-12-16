package luxuriousmisfortunes.common.blocks;

import java.time.LocalTime;

import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemMouth extends BlockTotemStructure {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool IS_SHUT = PropertyBool.create("is_shut");

    private static final double T = 1.0 / 3.0;
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

    public static String name = "totem_mouth";

    public BlockTotemMouth(String name) {
        super(name, Material.ROCK);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_SHUT, true));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, IS_SHUT});
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (PayTimeHash.payment_record.containsKey(playerIn.getUniqueID())) {
            LocalTime time = PayTimeHash.payment_record.get(playerIn.getUniqueID());

            if (playerIn.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) && time.getHour() < 10) {
                ItemStack stack = playerIn.getHeldItemMainhand();

                PayTimeHash.payment_record.put(playerIn.getUniqueID(), time.plusMinutes(30));

                stack.shrink(1);
                return true;
            }
        }

        return false;
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
        return this.getDefaultState().withProperty(FACING, facing).withProperty(IS_SHUT, true);
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
        int meta = state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(IS_SHUT)) {
            meta |= 1 << 2;
        }

        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);

        boolean active = (meta & 4) != 0;

        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(IS_SHUT, active);
    }
}
