package luxuriousmisfortunes.network.proxy;

import java.util.ArrayList;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemVariants(Item item, int meta, String... names) {

        for (int i = 0; i <= meta; i++) {
            ModelBakery.registerItemVariants(item, new ResourceLocation(Main.MODID, names[i]));
        }

    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {

        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void registerMetaRenderer(Item item, String name, int meta, String id) {

        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, id));
    }

}
