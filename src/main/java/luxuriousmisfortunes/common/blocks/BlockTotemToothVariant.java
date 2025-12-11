package luxuriousmisfortunes.common.blocks;

import java.util.ArrayList;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.TotemSwitchScheduler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemToothVariant extends BlockTotemStructure {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger HOLDING = PropertyInteger.create("holding", 0, 3);

    public static String name = "totem_tooth_holding";

    public BlockTotemToothVariant(String name) {
        super(name, Material.ROCK);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HOLDING, 0));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, HOLDING});
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        if (side != EnumFacing.UP && side != EnumFacing.DOWN)
            return true;

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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

        for (int i = 0; i < ItemInit.acceptables.length; i++) {
            if (state.getValue(HOLDING) == i) {
                if (playerIn.isSneaking()) {
                    if (!worldIn.isRemote) {

                        EnumFacing initial = state.getValue(FACING);

                        worldIn.setBlockState(pos, BlockInit.TOTEM_TOOTH.getDefaultState().withProperty(FACING, initial));
                        ItemStack stack = new ItemStack(ItemInit.acceptables[i]);

                        if (!playerIn.addItemStackToInventory(stack)) {
                            playerIn.dropItem(stack, false);
                        }

                        BlockPos stonePos = pos.offset(state.getValue(BlockTotemToothVariant.FACING).getOpposite());
                        IBlockState stoneState = worldIn.getBlockState(stonePos);

                        if (stoneState.getBlock().equals(BlockInit.TOTEM_STONE)) {
                            TotemSwitchScheduler.scheduleTotemSwitchAt(playerIn, stonePos, worldIn, i);
                            return true;
                        }


                    }
                } else if (worldIn.isRemote){
                    TextComponentTranslation inspectInsides = new TextComponentTranslation("interaction" + "." + Main.MODID + "." + BlockTotemTooth.name + "." + ItemInit.acceptables[i].getRegistryName().getPath());
                    playerIn.sendStatusMessage(new TextComponentString(I18n.format(inspectInsides.getFormattedText())), true);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        meta |= state.getValue(FACING).getHorizontalIndex();

        meta |= (state.getValue(HOLDING) & 3) << 2;

        return meta;
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        int dir = meta & 3;
        EnumFacing facing = EnumFacing.byHorizontalIndex(dir);

        int level = (meta >> 2) & 3;

        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(HOLDING, level);
    }

}
