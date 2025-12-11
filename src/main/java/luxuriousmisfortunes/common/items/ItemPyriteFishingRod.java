package luxuriousmisfortunes.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import scala.util.Random;

public class ItemPyriteFishingRod extends ItemFishingRod {

    public static String name = "pyrite_fishing_rod";

    public ItemPyriteFishingRod(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(Main.tabMod);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
