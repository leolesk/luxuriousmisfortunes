package luxuriousmisfortunes.init;

import org.lwjgl.input.Keyboard;

import luxuriousmisfortunes.api.Main;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeybindInit {

    public static String name = "key" + "." + Main.MODID + "." + "put_subarmor_on";
    static String description = "category" + "." + Main.MODID;

    public static KeyBinding PUT_SUBARMOR_ON;

    public static void init() {
        PUT_SUBARMOR_ON = new KeyBinding(
                name,
                Keyboard.KEY_M,
                description
                );

        ClientRegistry.registerKeyBinding(PUT_SUBARMOR_ON);
    }

}
