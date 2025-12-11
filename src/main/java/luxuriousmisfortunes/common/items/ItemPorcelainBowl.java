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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
                        tooltip.add(types[x].getItemStackDisplayName(new ItemStack(types[y])));
                    }
                }
            }

        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        System.out.println(stack.getTagCompound());

        if (stack.getSubCompound(Main.MODID) != null) {
            System.out.println("found modid");

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
            boolean flag1 = false;
            boolean flag2 = false;

            outerLoop:
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (compound.getString(String.valueOf(x)).equals(types[y].getRegistryName().toString())) {
                            compound.removeTag(String.valueOf(x));
                            flag2 = (y == 2);
                            flag1 = true;
                            break outerLoop;
                        }
                    }
                }
            System.out.println(compound);

            int ticksToApply = flag2 ? 6000 : 1200;

            entityLiving.addPotionEffect(new PotionEffect(EffectInit.REPLENISHED, ticksToApply, 1));

            if (!flag1) {
                empty.getOrCreateSubCompound(Main.MODID).setDouble(key, 0d);
            } else {
                NBTTagCompound sub = empty.getOrCreateSubCompound(Main.MODID);
                sub.merge(compound.copy());
                empty.setTagCompound(sub);
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
