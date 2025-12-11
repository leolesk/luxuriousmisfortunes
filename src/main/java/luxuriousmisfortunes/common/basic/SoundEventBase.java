package luxuriousmisfortunes.common.basic;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.SoundInit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundEventBase extends SoundEvent{

    public SoundEventBase(String name) {
        super(new ResourceLocation(Main.MODID, name));
        this.setRegistryName(Main.MODID, name);

        SoundInit.SOUNDS.add(this);
    }

}
