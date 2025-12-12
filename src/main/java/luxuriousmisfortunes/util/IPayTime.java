package luxuriousmisfortunes.util;

import java.time.LocalTime;

public interface IPayTime {

    public LocalTime getPayTime();

    public void setPayTime(int hours, int minutes);
}
