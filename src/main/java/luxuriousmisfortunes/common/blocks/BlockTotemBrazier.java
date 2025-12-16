package luxuriousmisfortunes.common.blocks;

import java.time.LocalTime;
import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockBase;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemBrazier extends BlockBase {

    public static String name = "totem_brazier";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockTotemBrazier(String name) {
        super(name, Material.ROCK);

        this.setLightLevel(5);
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.offset(EnumFacing.UP), BlockInit.TOTEM_BRAZIER_FILLER.getDefaultState());
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.getBlockState(pos.offset(EnumFacing.UP)).getBlock() instanceof BlockStructureFiller) {
            worldIn.setBlockToAir(pos.offset(EnumFacing.UP));
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        boolean block = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        boolean above = worldIn.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isReplaceable(worldIn, pos.offset(EnumFacing.UP));

        return block && above;
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());
    }
}
