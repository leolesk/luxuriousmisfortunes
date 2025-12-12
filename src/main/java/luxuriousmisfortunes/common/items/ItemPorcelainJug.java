package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemFoodBase;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPorcelainJug extends ItemFoodBase {

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public static String name = "porcelain_jug";
    public static String key = "full";

    public ItemPorcelainJug(String name, int amount, boolean isWolfFood) {
        super(name, amount, isWolfFood);
        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation(Main.MODID, "full"),
                new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return ItemPorcelainJug.getPropertyFull(stack, entityIn);
            }
        });
    }

    public static float getPropertyFull (ItemStack stack, @Nullable EntityLivingBase entityIn) {

        if (!stack.isEmpty() && stack.getItem() instanceof ItemPorcelainJug) {
            if (stack.getSubCompound(Main.MODID) != null)
                return stack.getSubCompound(Main.MODID).getDouble(key) != 0 ? 1 : 0;
        }
        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);
            boolean pass = false;

            if (compound.getDouble(key) != 0d) {

                outerLoop:
                    for (int x = 0; x < 3; x++) {

                        for (PotionType type : PotionType.REGISTRY) {

                            if (compound.getString(String.valueOf(x)).equals(type.getRegistryName().toString())) {
                                pass = true;
                            }
                        }

                    }

                if (pass) {
                    playerIn.setActiveHand(handIn);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                }


            }

        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        ItemStack empty = new ItemStack(ItemInit.JUG);

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);
            int flag1 = 0;
            PotionType toApply = null;

            outerLoop:
                for (int x = 0; x < 3; x++) {

                    for (PotionType type : PotionType.REGISTRY) {

                        if (compound.getString(String.valueOf(x)).equals(type.getRegistryName().toString())) {
                            compound.removeTag(String.valueOf(x));
                            toApply = type;
                            break outerLoop;
                        }
                    }
                    flag1 += 1;
                }

            if (toApply != null) {
                Potion effect = toApply.getEffects().get(0).getPotion();
                if (!effect.isInstant()) {
                    entityLiving.addPotionEffect(new PotionEffect(effect, 9600, 1));
                } else {
                    entityLiving.addPotionEffect(new PotionEffect(effect));
                }
            }

            if (flag1 == 1) {
                //                empty.getOrCreateSubCompound(Main.MODID).setDouble(key, 0d);
                empty.removeSubCompound(Main.MODID);
            } else {
                NBTTagCompound newRoot = new NBTTagCompound();
                NBTTagCompound sub = compound.copy();
                newRoot.setTag(Main.MODID, sub);
                empty.setTagCompound(newRoot);
            }

        }

        return empty;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);

            for (int i = 0; i < 3; i++) {

                for (PotionType type : PotionType.REGISTRY) {

                    if (compound.getString(String.valueOf(i)).equals(type.getRegistryName().toString())) {

                        String potionName = I18n.format(PotionType.getPotionTypeForName(compound.getString(String.valueOf(i)))
                                .getEffects().get(0).getEffectName().trim());

                        tooltip.add(potionName);

                    }
                }

            }

        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
