package luxuriousmisfortunes.common.items;

import luxuriousmisfortunes.common.basic.ItemBase;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemVelvetSubarmor extends ItemBase {

    public static String name = "velvet_subarmor";

    public ItemVelvetSubarmor(String name) {
        super(name);

        this.setMaxStackSize(1);
        this.setMaxDamage(825);
    }

}
