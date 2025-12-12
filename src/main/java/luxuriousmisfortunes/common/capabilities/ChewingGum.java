package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.util.IChewingGum;
import net.minecraft.nbt.NBTTagCompound;

public class ChewingGum implements IChewingGum{

    private boolean isChewing = false;
    private long timeInitial = 0;

    public static final String name = "chewing_gum";

    @Override
    public boolean isChewing() {
        return this.isChewing;
    }

    @Override
    public void setChewing(boolean stat) {
        this.isChewing = stat;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(name, this.isChewing);
        return tag;
    }

    public NBTTagCompound deserializeNBT(NBTTagCompound nbt) {
        this.isChewing = nbt.getBoolean(name);

        return nbt;
    }

}
