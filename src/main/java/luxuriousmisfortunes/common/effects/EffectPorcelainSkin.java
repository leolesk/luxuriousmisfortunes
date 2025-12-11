package luxuriousmisfortunes.common.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class EffectPorcelainSkin extends Potion{

    protected EffectPorcelainSkin(boolean isBadEffectIn, int liquidColorIn) {
        super(true, 0);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {

        if (entityLivingBaseIn instanceof EntityPlayer) {

            EntityPlayer playerIn = (EntityPlayer)entityLivingBaseIn;

            if (playerIn.getEntityWorld().canSeeSky(playerIn.getPosition())) {
                ItemStack helmet = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (helmet.isEmpty() || !helmet.isItemStackDamageable()) {
                    playerIn.setFire(5);
                }
            }

        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {

        return duration % 100 == 0;
    }

}
