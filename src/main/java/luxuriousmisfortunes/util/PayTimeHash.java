package luxuriousmisfortunes.util;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.potion.Potion;

public class PayTimeHash {

    public static Potion[] acceptables = {
            EffectInit.GLUTTONY,
            EffectInit.HEAVY_LUXURY,
            EffectInit.MINDTRICK,
            EffectInit.PAPER_SKIN
    };

    public static HashMap<UUID, LocalTime> payment_record = new HashMap<UUID, LocalTime>();

}
