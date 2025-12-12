package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class EffectPaperSkin extends Potion{

    public static String name = "paper_skin";

    protected EffectPaperSkin(String name) {
        super(true, 0);

        this.setRegistryName(Main.MODID, name);
        this.setPotionName("effect." + name);

        EffectInit.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {

        if (entityLivingBaseIn instanceof EntityPlayer) {

            EntityPlayer playerIn = (EntityPlayer)entityLivingBaseIn;
            World world = playerIn.world;

            if (!world.isRemote && world.isDaytime()) {

                if (world.canSeeSky(playerIn.getPosition())) {

                    ItemStack helmet = playerIn.inventory.armorInventory.get(3);

                    if (helmet == null || helmet.getItem() == Items.AIR) {
                        playerIn.setFire(8);
                    } else {
                        helmet.setItemDamage(helmet.getItemDamage() + 1);
                    }
                }

            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {

        return duration % 100 == 0;
    }

}
