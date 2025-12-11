package luxuriousmisfortunes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.blocks.BlockTotemPersona;
import luxuriousmisfortunes.common.blocks.BlockTotemTooth;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.SoundInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.packets.PacketSpawnParticles;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
public class TotemSwitchScheduler {

    static int wait = 40;

    private static final HashMap<UUID, PositionAndTime> switchTasks = new HashMap<UUID, PositionAndTime>();

    public static void scheduleTotemSwitchAt(EntityPlayer player, BlockPos bracePos, World world, int headType) {
        if (!switchTasks.containsKey(player.getUniqueID())) {
            switchTasks.put(player.getUniqueID(), new PositionAndTime(bracePos, world, wait, headType));
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            if (!switchTasks.isEmpty()) {

                for (UUID id : switchTasks.keySet()) {
                    PositionAndTime pos = switchTasks.get(id);

                    if (pos != null) {

                        pos.ticksLeft--;

                        if (pos.ticksLeft == 30) {
                            switchTeeth(pos.world, pos.pos);
                        } else if (pos.ticksLeft == 20) {

                            switchHead(pos.world, pos.pos, pos.type, id);

                            switchTasks.remove(id);
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
        int type;

        PositionAndTime(BlockPos pos, World world, int ticksLeft, int type) {
            this.pos = pos;
            this.world = world;
            this.ticksLeft = ticksLeft;
            this.type = type;
        }
    }

    private static void switchTeeth(World world, BlockPos upperStonePos) {

        for (int i = 0; i < 4; i++) {

            EnumFacing facing = world.getBlockState(upperStonePos.offset(EnumFacing.byHorizontalIndex(i))).getValue(BlockTotemTooth.FACING);

            world.setBlockState(upperStonePos.offset(EnumFacing.byHorizontalIndex(i)), BlockInit.TOTEM_TOOTH.getDefaultState().withProperty(BlockTotemTooth.FACING, facing));

        }

    }

    private static void switchHead(World world, BlockPos upperStonePos, int type, UUID id) {

        BlockPos down = upperStonePos.offset(EnumFacing.DOWN, 2);

        world.playSound(
                null,
                down,
                SoundInit.PERSONA_CHANGE,
                SoundCategory.BLOCKS,
                2.0F,
                1.0F
                );

        Block block = null;

        switch (type) {
            case 0: block = BlockInit.TOTEM_HEAD_PYRITE;
            break;
            case 1: block = BlockInit.TOTEM_HEAD_PORCELAIN;
            break;
            case 2: block = BlockInit.TOTEM_HEAD_VELVET;
            break;
            case 3: block = BlockInit.TOTEM_HEAD_NACRE;
            break;
        }

        for (int i = 0; i < 4; i++) {
            if (world.getBlockState(down.offset(EnumFacing.byHorizontalIndex(i))).getBlock().equals(BlockInit.TOTEM_HEAD_INACTIVE)) {

                EnumFacing facing = world.getBlockState(down.offset(EnumFacing.byHorizontalIndex(i))).getValue(BlockTotemPersona.FACING);
                BlockPos pos = down.offset(EnumFacing.byHorizontalIndex(i));

                if (world.getMinecraftServer().getPlayerList().getPlayerByUUID(id) != null) {
                    Network.sendToPlayerMP(new PacketSpawnParticles(pos, EnumParticleTypes.ENCHANTMENT_TABLE), world.getMinecraftServer().getPlayerList().getPlayerByUUID(id));
                }

                if (block != null) {
                    world.setBlockState(pos, block.getDefaultState().withProperty(BlockTotemPersona.FACING, facing).withProperty(BlockTotemPersona.IS_LOCKED, true));
                }
            }
        }
    }

}
