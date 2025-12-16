package luxuriousmisfortunes.init;

import java.util.ArrayList;
import java.util.List;

import luxuriousmisfortunes.common.basic.BlockTotemStructure;
import luxuriousmisfortunes.common.blocks.BlockTotemBrace;
import luxuriousmisfortunes.common.blocks.BlockTotemBrazier;
import luxuriousmisfortunes.common.blocks.BlockStructureFiller;
import luxuriousmisfortunes.common.blocks.BlockTotemMouth;
import luxuriousmisfortunes.common.blocks.BlockTotemPersona;
import luxuriousmisfortunes.common.blocks.BlockTotemPersonaVariant;
import luxuriousmisfortunes.common.blocks.BlockTotemTooth;
import luxuriousmisfortunes.common.blocks.BlockTotemToothVariant;
import luxuriousmisfortunes.common.blocks.BlockTotemWall;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit {

    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block TOTEM_HEAD_INACTIVE = new BlockTotemPersona(BlockTotemPersona.name);

    public static final Block TOTEM_HEAD_PYRITE = new BlockTotemPersonaVariant(BlockTotemPersonaVariant.variant[0]);
    public static final Block TOTEM_HEAD_PORCELAIN = new BlockTotemPersonaVariant(BlockTotemPersonaVariant.variant[1]);
    public static final Block TOTEM_HEAD_VELVET = new BlockTotemPersonaVariant(BlockTotemPersonaVariant.variant[2]);
    public static final Block TOTEM_HEAD_NACRE = new BlockTotemPersonaVariant(BlockTotemPersonaVariant.variant[3]);

    public static final Block TOTEM_BODY = new BlockTotemBrace(BlockTotemBrace.name);
    public static final Block TOTEM_STONE = new BlockTotemStructure("totem_stone", Material.ROCK);
    public static final Block TOTEM_POLE = new BlockTotemWall("totem_pole");
    public static final Block TOTEM_MOUTH = new BlockTotemMouth(BlockTotemMouth.name);
    public static final Block TOTEM_TOOTH = new BlockTotemTooth(BlockTotemTooth.name);
    public static final Block TOTEM_TOOTH_HOLDING = new BlockTotemToothVariant(BlockTotemToothVariant.name);

    public static final Block TOTEM_BRAZIER = new BlockTotemBrazier(BlockTotemBrazier.name)
            .setHardness(2);
    public static final Block TOTEM_BRAZIER_FILLER = new BlockStructureFiller(BlockStructureFiller.name)
            .setHardness(2);


}
