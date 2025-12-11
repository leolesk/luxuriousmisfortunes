package luxuriousmisfortunes.common.basic;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock implements IHasModel {

    public ItemBlockBase(Block block) {
        super(block);

        this.setRegistryName(block.getRegistryName());
        this.setTranslationKey(block.getTranslationKey());
        this.setCreativeTab(Main.tabMod);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
