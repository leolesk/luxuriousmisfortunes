package luxuriousmisfortunes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.blocks.BlockTotemMouth;
import luxuriousmisfortunes.common.blocks.BlockTotemPersona;
import luxuriousmisfortunes.common.blocks.BlockTotemTooth;
import luxuriousmisfortunes.common.blocks.BlockTotemToothVariant;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.SoundInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.packets.PacketSpawnParticles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class TotemAssembleScheduler {

    static int wait = 120;

    static int[] timeToRead = {
            0,
            110,
            50,
            90,
            70,
            90,
            90,
            90,
            190,
            150
    };

    private static final HashMap<UUID, PositionAndTime> buildTasks = new HashMap<UUID, PositionAndTime>();

    public static void scheduleTotemAssembleAt(EntityPlayer player, BlockPos bracePos, World world) {
        if (!buildTasks.containsKey(player.getUniqueID())) {
            buildTasks.put(player.getUniqueID(), new PositionAndTime(bracePos, world, wait));
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            if (!buildTasks.isEmpty()) {

                for (UUID id : buildTasks.keySet()) {
                    PositionAndTime pos = buildTasks.get(id);

                    if (pos != null) {

                        pos.ticksLeft--;

                        if (pos.ticksLeft == 80) {
                            if (checkStructure(pos.world, pos.pos)) {

                                placeMouths(pos.world, pos.pos, id);

                            } else
                                return;
                        } else if (pos.ticksLeft == 40) {
                            if (checkStructure(pos.world, pos.pos)) {

                                placeTeeth(pos.world, pos.pos, id);

                            } else
                                return;
                        } else if (pos.ticksLeft == 20) {

                            if (pos.world.getMinecraftServer().getPlayerList().getPlayerByUUID(id) != null) {

                                ArrayList<MessageScheduler.MessageTask> messageTasks = new ArrayList<MessageScheduler.MessageTask>();

                                for (int y = 0; y < 10; y++) {
                                    TextComponentTranslation line = new TextComponentTranslation("scene" + "." + Main.MODID + "." + BlockTotemPersona.name + "." + "line" + "_" + y);
                                    messageTasks.add(new MessageScheduler.MessageTask(timeToRead[y], line, SoundInit.SOUNDS.get(y)));
                                }

                                MessageScheduler.scheduleMessages(pos.world.getMinecraftServer().getPlayerList().getPlayerByUUID(id), messageTasks);
                            }

                            buildTasks.remove(id);
                        }
                    }
                }

            }

        }
    }

    private static class PositionAndTime {
        BlockPos pos;
        World world;
        int ticksLeft;

        PositionAndTime(BlockPos pos, World world, int ticksLeft) {
            this.pos = pos;
            this.world = world;
            this.ticksLeft = ticksLeft;
        }
    }

    private static boolean checkStructure(World world, BlockPos bracePos) {

        BlockPos down = bracePos.offset(EnumFacing.DOWN, 2);
        BlockPos up = bracePos.offset(EnumFacing.UP, 2);

        for (int i = 0; i < 4; i++) {

            if (!world.getBlockState(up.offset(EnumFacing.byHorizontalIndex(i))).getBlock().equals(BlockInit.TOTEM_TOOTH))
                return false;

            if (!world.getBlockState(down.offset(EnumFacing.byHorizontalIndex(i))).getBlock().equals(BlockInit.TOTEM_MOUTH))
                return false;

        }

        boolean poles = world.getBlockState(bracePos.offset(EnumFacing.UP, 1)).getBlock().equals(BlockInit.TOTEM_POLE)
                && world.getBlockState(bracePos.offset(EnumFacing.DOWN, 1)).getBlock().equals(BlockInit.TOTEM_POLE);
        boolean stones = world.getBlockState(bracePos.offset(EnumFacing.UP, 2)).getBlock().equals(BlockInit.TOTEM_STONE)
                && world.getBlockState(bracePos.offset(EnumFacing.DOWN, 2)).getBlock().equals(BlockInit.TOTEM_STONE);

        return poles && stones;
    }

    private static void placeMouths(World world, BlockPos bracePos, UUID id) {

        BlockPos down = bracePos.offset(EnumFacing.DOWN, 2);

        world.playSound(
                null,
                down,
                SoundInit.GRATES_OPEN,
                SoundCategory.BLOCKS,
                2.0F,
                1.0F
                );

        for (int i = 0; i < 4; i++) {

            BlockPos pos = down.offset(EnumFacing.byHorizontalIndex(i));

            EnumFacing facing = world.getBlockState(pos).getValue(BlockTotemTooth.FACING);

            if (world.getMinecraftServer().getPlayerList().getPlayerByUUID(id) != null) {
                Network.sendToPlayerMP(new PacketSpawnParticles(pos, EnumParticleTypes.CRIT), world.getMinecraftServer().getPlayerList().getPlayerByUUID(id));
            }

            world.setBlockState(down.offset(EnumFacing.byHorizontalIndex(i)), BlockInit.TOTEM_MOUTH.getDefaultState().withProperty(BlockTotemMouth.IS_SHUT, false).withProperty(BlockTotemMouth.FACING, facing));

        }

    }

    private static void placeTeeth(World world, BlockPos bracePos, UUID id) {

        BlockPos up = bracePos.offset(EnumFacing.UP, 2);

        world.playSound(
                null,
                up,
                SoundInit.MATTERS_APPEAR,
                SoundCategory.BLOCKS,
                2.0F,
                1.0F
                );

        for (int i = 0; i < 4; i++) {

            BlockPos pos = up.offset(EnumFacing.byHorizontalIndex(i));

            EnumFacing facing = world.getBlockState(pos).getValue(BlockTotemTooth.FACING);

            if (world.getMinecraftServer().getPlayerList().getPlayerByUUID(id) != null) {
                Network.sendToPlayerMP(new PacketSpawnParticles(pos, EnumParticleTypes.ENCHANTMENT_TABLE), world.getMinecraftServer().getPlayerList().getPlayerByUUID(id));
            }



            world.setBlockState(pos,
                    BlockInit.TOTEM_TOOTH_HOLDING.getDefaultState()
                    .withProperty(BlockTotemToothVariant.HOLDING, i)
                    .withProperty(BlockTotemToothVariant.FACING, facing));

        }

    }



}
