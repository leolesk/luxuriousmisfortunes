package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.TextComponentTranslation;

public class EffectGluttony extends Potion {

    public static String name = "gluttony";

    public static TextComponentTranslation complaint = new TextComponentTranslation("complaint" + "." + Main.MODID + "." + name);

    public static Item[] accepted = {
            Items.GOLDEN_APPLE,
            Items.GOLDEN_CARROT
    };

    protected EffectGluttony(boolean isBadEffectIn, int liquidColorIn) {
        super(true, 0);

        this.setRegistryName(Main.MODID, name);
        this.setPotionName("effect." + name);

        EffectInit.POTIONS.add(this);
    }

}
