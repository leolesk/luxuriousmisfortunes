package luxuriousmisfortunes.common.basic;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(CreativeTabs.SEARCH);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
