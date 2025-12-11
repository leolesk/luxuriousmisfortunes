package luxuriousmisfortunes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import luxuriousmisfortunes.init.SoundInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class MessageScheduler {

    private static final Map<UUID, Queue<MessageTask>> playerTasks = new HashMap<>();

    public static void scheduleMessages(EntityPlayer player, ArrayList<MessageTask> messages) {

        Queue<MessageTask> sequence = new LinkedList<>();

        for (MessageTask message : messages) {
            sequence.add(message);
        }

        if (!playerTasks.containsKey(player.getUniqueID())) {
            playerTasks.put(player.getUniqueID(), sequence);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            if (!playerTasks.isEmpty()) {
                for (UUID id : new ArrayList<>(playerTasks.keySet())) {
                    Queue<MessageTask> sequence = playerTasks.get(id);
                    if (sequence == null || sequence.isEmpty()) {
                        continue;
                    }

                    MessageTask task = sequence.peek();
                    task.ticksLeft--;
                    if (task.ticksLeft <= 0) {
                        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                                .getPlayerList().getPlayerByUUID(id);
                        if (player != null) {
                            player.sendMessage(task.message);

                            player.world.playSound(
                                    null,
                                    player.getPosition(),
                                    task.voiceOver,
                                    SoundCategory.BLOCKS,
                                    2.0F,
                                    1.0F
                                    );

                        }
                        sequence.poll();
                    }
                    if (sequence.isEmpty()) {
                        playerTasks.remove(id);
                    }
                }
            }
        }
    }

    public static class MessageTask {
        int ticksLeft;
        TextComponentTranslation message;
        SoundEvent voiceOver;

        MessageTask(int ticks, TextComponentTranslation message, SoundEvent voiceOver) {
            this.ticksLeft = ticks;
            this.message = message;
            this.voiceOver = voiceOver;
        }
    }

}
