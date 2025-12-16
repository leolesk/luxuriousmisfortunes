package luxuriousmisfortunes.common.worldgen;

import java.util.Random;

import luxuriousmisfortunes.api.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class UndergroundForestGen implements IWorldGenerator {

    public static String name = "underground_forest";

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world,
            IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        if (world.provider.getDimension() == 0) {
            this.generateOverworld(rand, chunkX, chunkZ, world);
        }
    }

    private void generateOverworld(Random rand, int chunkX, int chunkZ, World world) {


        if (rand.nextInt(80) != 0)
            return;

        int x = (chunkX * 16) + rand.nextInt(16);
        int z = (chunkZ * 16) + rand.nextInt(16);
        int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY() - 21 - rand.nextInt(20);

        BlockPos pos = new BlockPos(x, y, z);

        this.generateDungeon(world, rand, pos);
    }

    private void generateDungeon(World world, Random rand, BlockPos pos) {

        MinecraftServer server = world.getMinecraftServer();
        if (server == null) return;

        TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();

        ResourceLocation location = new ResourceLocation(Main.MODID, name);

        Template template = manager.getTemplate(server, location);
        if (template == null) {
            System.out.println("Template not found: " + location);
            return;
        }

        PlacementSettings settings = new PlacementSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        if (this.canPlaceStructure(world, pos, template)) {
            template.addBlocksToWorld(world, pos, settings);
        }
    }

    private boolean canPlaceStructure(World world, BlockPos pos, Template template) {

        BlockPos size = template.getSize();

        BlockPos min = pos;
        BlockPos max = pos.add(size.getX() - 1, size.getY() - 1, size.getZ() - 1);

        int padding = 1;

        for (int x = min.getX() - padding; x <= max.getX() + padding; x++) {
            for (int y = min.getY() - padding; y <= max.getY() + padding; y++) {
                for (int z = min.getZ() - padding; z <= max.getZ() + padding; z++) {

                    if (x >= min.getX() && x <= max.getX()
                            && y >= min.getY() && y <= max.getY()
                            && z >= min.getZ() && z <= max.getZ()) {
                        continue;
                    }

                    BlockPos checkPos = new BlockPos(x, y, z);

                    if (world.isAirBlock(checkPos))
                        return false;
                }
            }
        }

        return true;
    }

}