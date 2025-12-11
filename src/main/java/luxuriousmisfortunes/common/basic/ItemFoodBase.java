package luxuriousmisfortunes.common.basic;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.IHasModel;
import net.minecraft.item.ItemFood;

public class ItemFoodBase extends ItemFood implements IHasModel{

    public ItemFoodBase(String name, int amount, boolean isWolfFood) {
        super(amount, isWolfFood);
        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(Main.tabMod);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
