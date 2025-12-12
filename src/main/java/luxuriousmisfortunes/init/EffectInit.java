package luxuriousmisfortunes.init;

import java.util.ArrayList;
import java.util.List;

import luxuriousmisfortunes.common.effects.EffectFortunesGrace;
import luxuriousmisfortunes.common.effects.EffectGluttony;
import luxuriousmisfortunes.common.effects.EffectHeavyLuxury;
import luxuriousmisfortunes.common.effects.EffectMindtrick;
import luxuriousmisfortunes.common.effects.EffectPaperSkin;
import luxuriousmisfortunes.common.effects.EffectReplenished;
import net.minecraft.potion.Potion;

public class EffectInit {

    public static final List<Potion> POTIONS = new ArrayList<Potion>();

    public static final Potion REPLENISHED = new EffectReplenished(EffectReplenished.name);
    public static final Potion FORTUNES_GRACE = new EffectFortunesGrace(EffectFortunesGrace.name);

    public static final Potion GLUTTONY = new EffectFortunesGrace(EffectGluttony.name);
    public static final Potion HEAVY_LUXURY = new EffectFortunesGrace(EffectHeavyLuxury.name);
    public static final Potion MINDTRICK = new EffectFortunesGrace(EffectMindtrick.name);
    public static final Potion PAPER_SKIN = new EffectFortunesGrace(EffectPaperSkin.name);

}
