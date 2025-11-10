package luxuriousmisfortunes.init;

import java.util.ArrayList;
import java.util.List;

import luxuriousmisfortunes.common.blocks.BlockTotemBrace;
import luxuriousmisfortunes.common.blocks.BlockTotemPersona;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;

public class BlockInit {

    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block TOTEM_HEAD = new BlockTotemPersona("persona");
    public static final Block TOTEM_BODY = new BlockTotemBrace("totem_brace");

}
