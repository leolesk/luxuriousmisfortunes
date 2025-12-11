package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.util.IChewingGum;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityChewingGum {
    @CapabilityInject(IChewingGum.class)
    public static final Capability<IChewingGum> CAP = null;

    public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, ChewingGum.name);

    public static void register() {
        CapabilityManager.INSTANCE.register(
                IChewingGum.class,
                new Capability.IStorage<IChewingGum>() {
                    @Override
                    public NBTBase writeNBT(Capability<IChewingGum> capability, IChewingGum instance, EnumFacing side) {
                        if (instance instanceof ChewingGum)
                            return ((ChewingGum) instance).serializeNBT();
                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<IChewingGum> capability, IChewingGum instance, EnumFacing side, NBTBase nbt) {
                        if (instance instanceof ChewingGum) {
                            ((ChewingGum) instance).deserializeNBT((NBTTagCompound) nbt);
                        }
                    }
                },

                ChewingGum::new
                );
    }

}
