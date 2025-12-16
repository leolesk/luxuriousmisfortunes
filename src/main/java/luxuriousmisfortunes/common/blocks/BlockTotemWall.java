package luxuriousmisfortunes.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.init.BlockInit;
import net.minecraft.block.BlockWall;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemWall extends BlockWall{

    public BlockTotemWall(String name) {
        super(Blocks.COBBLESTONE);

        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(CreativeTabs.SEARCH);

        this.setHardness(this.blockHardness);

        BlockInit.BLOCKS.add(this);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(BlockTotemStructure.unbreakable.getFormattedText());
    }

}
