package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPyriteFishingRod extends ItemFishingRod implements IHasModel {

    public static String name = "pyrite_fishing_rod";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public ItemPyriteFishingRod(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(CreativeTabs.SEARCH);

        ItemInit.ITEMS.add(this);
    }

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

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
