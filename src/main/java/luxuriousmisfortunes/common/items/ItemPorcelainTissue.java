package luxuriousmisfortunes.common.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemFoodBase;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.util.IHasMeta;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPorcelainTissue extends ItemFoodBase implements IHasMeta{

    public static String name = "porcelain_tissue";
    public static String meta_name = "porcelain_tissue_fried";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public ItemPorcelainTissue(String name, int amount, boolean isWolfFood) {
        super(name, amount, isWolfFood);

        this.setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        EntityPlayer player = (EntityPlayer)entityLiving;

        player.setHealth(player.getHealth() + this.getHealAmount(stack));

        if (stack.getMetadata() == 0) {

            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60, 0));
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 0));


        } else if (stack.getMetadata() == 1) {

            List<Potion> potions = new ArrayList<>();

            for (Potion x : Potion.REGISTRY) {
                if (!x.isBadEffect()) {
                    potions.add(x);
                }

                Potion potion = potions.get(worldIn.rand.nextInt(potions.size()));

                player.addPotionEffect(new PotionEffect(potion, 200, 0));

            }
        }

        stack.shrink(1);

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void registerItemVariants() {
        Main.proxy.registerItemVariants(this, 0, name);
        Main.proxy.registerItemVariants(this, 1, meta_name);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
        Main.proxy.registerItemRenderer(this, 1, "inventory");
    }

}
