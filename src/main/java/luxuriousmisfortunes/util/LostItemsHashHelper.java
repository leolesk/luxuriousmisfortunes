package luxuriousmisfortunes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.util.Random;

public class LostItemsHashHelper {

    public static HashMap<UUID, ArrayList<EntityItem>> global_record = new HashMap<UUID, ArrayList<EntityItem>>();
    static int limit = 50;

    public static void tryAddItemToMap(EntityItem entityItem, UUID playerId) {

        if (global_record.containsKey(playerId)) {
            ArrayList<EntityItem> current = global_record.get(playerId);
            if (current.size() >= limit) {
                current.remove(new Random().nextInt(current.size()));
            }
            current.add(entityItem);
            global_record.put(playerId, current);
        } else {
            ArrayList<EntityItem> fresh = new ArrayList<EntityItem>();
            fresh.add(entityItem);
            global_record.put(playerId, fresh);
        }
    }

    public static EntityItem pullRandomItemFromMap(UUID playerId) {

        if (global_record.containsKey(playerId)) {
            ArrayList<EntityItem> record = global_record.get(playerId);

            for (EntityItem entity : record) {
                if (entity.isDead)
                    return entity;
            }
        }
        return null;
    }

    public static void tryDeleteItemFromMap(EntityItem entityItem, UUID playerId) {
        if (global_record.containsKey(playerId)) {
            ArrayList<EntityItem> record = global_record.get(playerId);

            record.remove(entityItem);
        }
    }

}
