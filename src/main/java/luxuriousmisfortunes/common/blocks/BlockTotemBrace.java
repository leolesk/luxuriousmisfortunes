package luxuriousmisfortunes.common.blocks;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.init.BlockInit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockTotemBrace extends BlockTotemStructure {

    public static String name = "totem_brace";

    TextComponentTranslation braceInspect = new TextComponentTranslation("interaction" + "." + Main.MODID + "." + name);

    public BlockTotemBrace(String name) {
        super(name, Material.ROCK);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        for (EnumFacing x : EnumFacing.VALUES) {
            if (worldIn.getBlockState(pos.offset(x)).getBlock() instanceof BlockTotemPersona)
                return false;
        }

        if (worldIn.isRemote) {
            if (playerIn.getHeldItem(hand).getItem() != Item.getItemFromBlock(BlockInit.TOTEM_HEAD_INACTIVE)) {
                playerIn.sendStatusMessage(new TextComponentString(I18n.format(this.braceInspect.getFormattedText())), true);
            }
            return true;
        }

        return false;
    }


}
