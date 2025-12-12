package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.util.IPayTime;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPayTime {
    @CapabilityInject(IPayTime.class)
    public static final Capability<IPayTime> CAP = null;

    public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, PayTime.name);

    public static void register() {
        CapabilityManager.INSTANCE.register(
                IPayTime.class,
                new Capability.IStorage<IPayTime>() {
                    @Override
                    public NBTBase writeNBT(Capability<IPayTime> capability, IPayTime instance, EnumFacing side) {
                        if (instance instanceof PayTime)
                            return ((PayTime) instance).serializeNBT();
                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<IPayTime> capability, IPayTime instance, EnumFacing side, NBTBase nbt) {
                        if (instance instanceof PayTime) {
                            ((PayTime) instance).deserializeNBT((NBTTagCompound) nbt);
                        }
                    }
                },

                PayTime::new
                );
    }

}
