package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EffectReplenished extends Potion{

    public static String name = "replenished";

    public EffectReplenished(String name) {
        super(false, 0);
        this.setRegistryName(Main.MODID, name);
        this.setPotionName("effect." + name);

        EffectInit.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {

        if (entityLivingBaseIn instanceof EntityPlayer) {

            EntityPlayer playerIn = (EntityPlayer)entityLivingBaseIn;

            if (playerIn.getFoodStats().getFoodLevel() < 20) {
                playerIn.getFoodStats().addStats(1, 1);
            }

        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {

        return duration % 20 == 0;
    }

    //    @SideOnly(Side.CLIENT)
    //    @Override
    //    public void renderInventoryEffect(PotionEffect effect, net.minecraft.client.Minecraft mc, int x, int y, float z) {
    //        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "textures/effects/" + name + ".png"));
    //    }
    //
    //    @SideOnly(Side.CLIENT)
    //    @Override
    //    public void renderHUDEffect(PotionEffect effect, net.minecraft.client.Minecraft mc, int x, int y, float alpha, net.minecraft.client.gui.Gui gui) {
    //        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "textures/effects/" + name + ".png"));
    //    }

}
