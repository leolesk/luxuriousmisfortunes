package luxuriousmisfortunes.common.capabilities;

import java.time.LocalTime;

import luxuriousmisfortunes.util.IPayTime;
import net.minecraft.nbt.NBTTagCompound;

public class PayTime implements IPayTime{

    private long timeToPay = 0;

    private int hours = 0;
    private int minutes = 0;

    public static final String name = "time_to_pay";
    public static final String hours_name = "hours";
    public static final String minutes_name = "minutes";

    @Override
    public LocalTime getPayTime() {
        return LocalTime.of(this.hours, this.minutes);
    }

    @Override
    public void setPayTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(hours_name, this.hours);
        tag.setInteger(minutes_name, this.minutes);
        return tag;
    }

    public NBTTagCompound deserializeNBT(NBTTagCompound nbt) {
        this.timeToPay = nbt.getLong(name);

        return nbt;
    }

}
