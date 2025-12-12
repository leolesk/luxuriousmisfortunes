package luxuriousmisfortunes.network.proxy;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy {

    public void registerItemVariants(Item item, int meta, String... names) {}

    public void registerItemRenderer(Item item, int meta, String id) {}

    public void registerMetaRenderer(Item item, String name, int meta, String id) {}

}
