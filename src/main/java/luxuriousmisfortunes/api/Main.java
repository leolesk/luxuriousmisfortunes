package luxuriousmisfortunes.api;

import luxuriousmisfortunes.common.tiles.TileEntityTotemPersona;
import luxuriousmisfortunes.handlers.GenericEventHandler;
import luxuriousmisfortunes.network.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME)
public class Main {

    public static final String MODID = "luxuriousmisfortunes";
    public static final String NAME = "Luxurious Misfortunes";
    public static final String VERSION = "1.0.0";

    public static SimpleNetworkWrapper packetHandler;

    @SidedProxy(clientSide = "luxuriousmisfortunes.network.proxy.ClientProxy", serverSide = "luxuriousmisfortunes.network.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new GenericEventHandler());

        GameRegistry.registerTileEntity(TileEntityTotemPersona.class, new ResourceLocation(MODID, "wheel"));
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    public static CreativeTabs tabMod = new CreativeTabs("tabLuxuriousMisfortunes") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.BAKED_POTATO);
        }
    };

}
