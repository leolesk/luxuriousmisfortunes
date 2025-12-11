package luxuriousmisfortunes.common.blocks;

import java.util.ArrayList;

public class BlockTotemPersonaVariant extends BlockTotemPersona {

    public static String[] variant = {
            "totem_persona_pyrite",
            "totem_persona_porcelain",
            "totem_persona_velvet",
            "totem_persona_nacre"
    };

    public static ArrayList<String> variant_names = new ArrayList<String>();

    public BlockTotemPersonaVariant(String name) {
        super(name);
        this.setCreativeTab(null);
    }

}
