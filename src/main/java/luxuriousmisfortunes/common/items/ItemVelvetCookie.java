package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemFoodBase;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.util.IHasMeta;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemVelvetCookie extends ItemFoodBase implements IHasMeta{

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");
    TextComponentTranslation description_conducted = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + meta_name + "." + "description");

    public static String name = "experiment";
    public static String meta_name = "experiment_conducted";

    public ItemVelvetCookie(String name, int amount, boolean isWolfFood) {
        super(name, amount, isWolfFood);

        this.setHasSubtypes(true);
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
        Main.proxy.registerMetaRenderer(this, ":meta_name", 1, "inventory");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.getMetadata() == 0) {

            if (playerIn.canEat(false)) {

                playerIn.setActiveHand(handIn);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);

            }

        } else if (stack.getMetadata() == 1) {

            int amplifier = 1;

            if (playerIn.getActivePotionEffect(EffectInit.FORTUNES_GRACE) != null) {
                amplifier = Math.max(1, Math.min(4, playerIn.getActivePotionEffect(EffectInit.FORTUNES_GRACE).getAmplifier()));
            }

            if (playerIn.canEat(false)  && (amplifier * 4) == playerIn.experienceLevel) {

                playerIn.setActiveHand(handIn);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);

            } else {

                worldIn.playSound(
                        null,
                        playerIn.getPosition(),
                        SoundEvents.ENTITY_BAT_HURT,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F
                        );

                playerIn.dropItem(stack, false);
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, new ItemStack(Items.AIR));

            }

        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        EntityPlayer player = (EntityPlayer)entityLiving;

        player.setHealth(player.getHealth() + this.getHealAmount(stack));

        if (stack.getMetadata() == 0) {

            player.addExperience(8);

        } else if (stack.getMetadata() == 1) {

            PotionEffect effect = player.getActivePotionEffect(EffectInit.FORTUNES_GRACE);
            int level = 0;

            if (effect != null) {

                int amp = effect.getAmplifier();
                level = amp < 4 ? amp + 1 : 0;

            }

            player.addExperienceLevel(level * 4);

            player.addPotionEffect(new PotionEffect(EffectInit.FORTUNES_GRACE, 600, level));

        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound compound = stack.getSubCompound(Main.MODID);

        if (stack.getMetadata() == 1) {

            tooltip.add(this.description_conducted.getFormattedText());

        } else {
            tooltip.add(this.description.getFormattedText());

        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
