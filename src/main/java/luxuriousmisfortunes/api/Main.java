package luxuriousmisfortunes.api;

import luxuriousmisfortunes.client.render.entity.RenderGolemPyrite;
import luxuriousmisfortunes.client.render.entity.RenderNacreGhast;
import luxuriousmisfortunes.client.render.entity.RenderPorcelainSpider;
import luxuriousmisfortunes.client.render.entity.RenderVelvetCow;
import luxuriousmisfortunes.client.render.entity.RenderVelvetSlime;
import luxuriousmisfortunes.common.capabilities.CapabilityChewingGum;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.entities.EntityGolemPyrite;
import luxuriousmisfortunes.common.entities.EntityNacreGhast;
import luxuriousmisfortunes.common.entities.EntityPorcelainSpider;
import luxuriousmisfortunes.common.entities.EntityVelvetCow;
import luxuriousmisfortunes.common.entities.EntityVelvetSlime;
import luxuriousmisfortunes.handlers.RegistryHandler;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.KeybindInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
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

    @Mod.Instance
    public static Main instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        RegistryHandler.registerEntities();
        Network.registerPackets();
        CapabilityChewingGum.register();
        CapabilitySubarmorEquipped.register();

        RenderingRegistry.registerEntityRenderingHandler(
                EntityGolemPyrite.class,
                renderManager -> new RenderGolemPyrite(renderManager)
                );
        RenderingRegistry.registerEntityRenderingHandler(
                EntityNacreGhast.class,
                renderManager -> new RenderNacreGhast(renderManager)
                );
        RenderingRegistry.registerEntityRenderingHandler(
                EntityPorcelainSpider.class,
                renderManager -> new RenderPorcelainSpider(renderManager)
                );
        RenderingRegistry.registerEntityRenderingHandler(
                EntityVelvetCow.class,
                renderManager -> new RenderVelvetCow(renderManager)
                );
        RenderingRegistry.registerEntityRenderingHandler(
                EntityVelvetSlime.class,
                renderManager -> new RenderVelvetSlime(renderManager)
                );
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void Init(FMLInitializationEvent event) {
        KeybindInit.init();
    }


    public static CreativeTabs tabMod = new CreativeTabs("tabLuxuriousMisfortunes") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ItemInit.TOTEM_BODY);
        }
    };

}
