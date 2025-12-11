package luxuriousmisfortunes.common.blocks;

import java.util.ArrayList;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockBase;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.util.MessageScheduler;
import luxuriousmisfortunes.util.TotemAssembleScheduler;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemPersona extends BlockBase {

    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool IS_LOCKED = PropertyBool.create("is_locked");
    public static String name = "totem_persona";

    protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);

    TextComponentTranslation breakAttempt = new TextComponentTranslation("interaction" + "." + Main.MODID + "." + name);

    public BlockTotemPersona(String name) {
        super(name, Material.GLASS);

        this.setBlockUnbreakable();
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_LOCKED, false));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!state.getValue(IS_LOCKED)) {

            if (placer instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)placer;

                for (int x = 0; x < EnumFacing.HORIZONTALS.length; x++) {

                    BlockPos by = pos.offset(EnumFacing.byHorizontalIndex(x));

                    if (worldIn.getBlockState(by).getBlock() instanceof BlockTotemBrace) {

                        TotemAssembleScheduler.scheduleTotemAssembleAt(player, by, worldIn);
                        worldIn.setBlockState(pos, state.withProperty(IS_LOCKED, true));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        ItemStack stack = new ItemStack(BlockInit.TOTEM_HEAD_INACTIVE);

        if (!state.getValue(IS_LOCKED)) {
            if (playerIn.isSneaking()) {
                worldIn.setBlockToAir(pos);
                if (!playerIn.addItemStackToInventory(stack)) {
                    playerIn.dropItem(stack, false);
                }
            }
        }

        return false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (worldIn.isRemote) {
            if (!state.getValue(IS_LOCKED)) {
                playerIn.sendStatusMessage(new TextComponentString(I18n.format(this.breakAttempt.getFormattedText())), true);
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
            case UP:
                return DEFAULT_AABB;
            default:
                return DEFAULT_AABB;
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
                return EAST_AABB;
        }
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, IS_LOCKED});
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing).withProperty(IS_LOCKED, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        EnumFacing facing = EnumFacing.byIndex(meta & 7);
        boolean is_locked = (meta & 8) != 0;

        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(IS_LOCKED, is_locked);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        meta |= state.getValue(FACING).getIndex();  // 0–5
        if (state.getValue(IS_LOCKED)) {
            meta |= 8;
        }

        return meta;
    }
}
