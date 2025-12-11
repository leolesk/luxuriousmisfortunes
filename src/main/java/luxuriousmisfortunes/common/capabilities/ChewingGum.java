package luxuriousmisfortunes.common.capabilities;

import luxuriousmisfortunes.util.IChewingGum;
import net.minecraft.nbt.NBTTagCompound;

public class ChewingGum implements IChewingGum{

    private boolean isChewing = false;
    private long timeInitial = 0;

    public static final String name = "chewing_gum";
    private static final String nameTime = "time_initial";

    @Override
    public void setTimeInitial(long time) {
        this.timeInitial = time;
    }

    @Override
    public boolean isChewing() {
        return this.isChewing;
    }

    @Override
    public void setChewing(boolean stat) {
        this.isChewing = stat;
    }

    @Override
    public long getTimeInitial() {
        return this.timeInitial;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(name, this.isChewing);
        tag.setLong(nameTime, this.timeInitial);
        return tag;
    }

    public NBTTagCompound deserializeNBT(NBTTagCompound nbt) {
        this.isChewing = nbt.getBoolean(name);
        this.timeInitial = nbt.getLong(nameTime);

        return nbt;
    }

}
