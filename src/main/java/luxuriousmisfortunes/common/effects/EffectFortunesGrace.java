package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.potion.Potion;

public class EffectFortunesGrace extends Potion {

    public static String name = "fortunes_grace";

    public EffectFortunesGrace(String name) {
        super(false, 0);
        this.setRegistryName(Main.MODID, name);
        this.setPotionName("effect." + name);

        EffectInit.POTIONS.add(this);
    }

}
