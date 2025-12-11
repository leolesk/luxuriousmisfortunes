package luxuriousmisfortunes.common.basic;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {

    public BlockBase(String name, Material materialIn) {
        super(materialIn);

        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(Main.tabMod);

        this.setHardness(this.blockHardness);

        BlockInit.BLOCKS.add(this);
    }



}
