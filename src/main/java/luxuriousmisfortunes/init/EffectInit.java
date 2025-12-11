package luxuriousmisfortunes.init;

import java.util.ArrayList;
import java.util.List;

import luxuriousmisfortunes.common.effects.EffectFortunesGrace;
import luxuriousmisfortunes.common.effects.EffectGluttony;
import luxuriousmisfortunes.common.effects.EffectReplenished;
import net.minecraft.potion.Potion;

public class EffectInit {

    public static final List<Potion> POTIONS = new ArrayList<Potion>();

    public static final Potion REPLENISHED = new EffectReplenished(EffectReplenished.name);
    public static final Potion FORTUNES_GRACE = new EffectFortunesGrace(EffectFortunesGrace.name);
    public static final Potion GLUTTONY = new EffectFortunesGrace(EffectGluttony.name);

}
