package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class EffectHeavyLuxury extends Potion{

    public static String name = "heavy_luxury";

    public EffectHeavyLuxury(String name) {
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

            IAttributeInstance speedAttribute = playerIn.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

            int enchantmentCount = 0;
            for (ItemStack armorPiece : playerIn.inventory.armorInventory) {
                if (!armorPiece.isEmpty()) {
                    enchantmentCount += EnchantmentHelper.getEnchantments(armorPiece).size();
                }
            }

            double slowMultiplier = 1.0 - Math.min(enchantmentCount * 0.05, 0.3);

            speedAttribute.setBaseValue(playerIn.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * slowMultiplier);

        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {

        return duration % 20 == 0;
    }


}
