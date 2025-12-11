package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.nbt.NBTTagCompound;

public class SubarmorEquipped implements ISubarmorEquipped {

    private boolean isArmorOn;

    public static final String name = "wears_subarmor";

    @Override
    public boolean isArmorOn() {
        return this.isArmorOn;
    }

    @Override
    public void setArmorOn(boolean value) {
        this.isArmorOn = value;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(name, this.isArmorOn);
        return tag;
    }

    public NBTTagCompound deserializeNBT(NBTTagCompound nbt) {
        this.isArmorOn = nbt.getBoolean(name);

        return nbt;
    }

}
