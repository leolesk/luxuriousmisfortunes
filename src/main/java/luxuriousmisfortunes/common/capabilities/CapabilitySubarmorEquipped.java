package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitySubarmorEquipped {
    @CapabilityInject(ISubarmorEquipped.class)
    public static final Capability<ISubarmorEquipped> CAP = null;

    public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, SubarmorEquipped.name);

    public static void register() {
        CapabilityManager.INSTANCE.register(
                ISubarmorEquipped.class,
                new Capability.IStorage<ISubarmorEquipped>() {
                    @Override
                    public NBTBase writeNBT(Capability<ISubarmorEquipped> capability, ISubarmorEquipped instance, EnumFacing side) {
                        if (instance instanceof SubarmorEquipped)
                            return ((SubarmorEquipped) instance).serializeNBT();
                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<ISubarmorEquipped> capability, ISubarmorEquipped instance, EnumFacing side, NBTBase nbt) {
                        if (instance instanceof ChewingGum) {
                            ((ChewingGum) instance).deserializeNBT((NBTTagCompound) nbt);
                        }
                    }
                },

                SubarmorEquipped::new
                );
    }

}
