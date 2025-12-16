package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemFoodBase;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPorcelainBowl extends ItemFoodBase {

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public static String name = "porcelain_bowl";
    public static String key = "full";

    public static final Item[] types = {
            Items.BEETROOT_SOUP,
            Items.MUSHROOM_STEW,
            Items.RABBIT_STEW
    };

    public ItemPorcelainBowl(String name, int amount, boolean isWolfFood) {
        super(name, amount, isWolfFood);
        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation(Main.MODID, "full"),
                new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return ItemPorcelainBowl.getPropertyFull(stack, entityIn);
            }
        });
    }

    public static float getPropertyFull (ItemStack stack, @Nullable EntityLivingBase entityIn) {

        if (!stack.isEmpty() && stack.getItem() instanceof ItemPorcelainBowl) {
            if (stack.getSubCompound(Main.MODID) != null)
                return stack.getSubCompound(Main.MODID).getDouble(ItemPorcelainBowl.key) != 0 ? 1 : 0;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y ++) {
                    if (compound.getString(String.valueOf(x)).equals(types[y].getRegistryName().toString())) {
                        tooltip.add(types[y].getItemStackDisplayName(new ItemStack(types[y])));
                    }
                }
            }

        }
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
                        for (int y = 0; y < 3; y ++) {
                            if (compound.getString(String.valueOf(x)).equals(types[y].getRegistryName().toString())) {
                                pass = true;
                                break outerLoop;
                            }
                        }
                    }

                if (playerIn.canEat(false) && pass) {
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

        ItemStack empty = new ItemStack(ItemInit.BOWL);

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);
            int flag1 = 0;
            boolean flag2 = false;

            outerLoop:
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (compound.getString(String.valueOf(x)).equals(types[y].getRegistryName().toString())) {
                            compound.removeTag(String.valueOf(x));
                            flag2 = (y == 2);
                            break outerLoop;
                        }
                    }
                    flag1 += 1;
                }

            Potion toApply = flag2 ? MobEffects.INSTANT_HEALTH : MobEffects.RESISTANCE;
            int ticksToApply = flag2 ? 400 : 1200;
            int levelToApply = flag2 ? 0 : 1;

            if (flag2) {
                entityLiving.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 400, 0));
            } else {
                entityLiving.addPotionEffect(new PotionEffect(EffectInit.REPLENISHED, 400, 0));
                entityLiving.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 2400, 0));
            }

            if (flag1 == 2) {
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
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
