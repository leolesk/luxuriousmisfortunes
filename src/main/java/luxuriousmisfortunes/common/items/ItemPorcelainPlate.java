package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemFoodBase;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPorcelainPlate extends ItemFoodBase {

    public static String name = "porcelain_plate";
    static String key = "full";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public ItemPorcelainPlate(String name, int amount, boolean isWolfFood) {
        super(name, amount, isWolfFood);
        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation(Main.MODID, "full"),
                new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return getPropertyFull(stack, entityIn);
            }
        });
    }

    public static float getPropertyFull (ItemStack stack, @Nullable EntityLivingBase entityIn) {

        if (!stack.isEmpty() && stack.getItem() instanceof ItemPorcelainPlate) {
            if (stack.getSubCompound(Main.MODID) != null)
                return stack.getSubCompound(Main.MODID).getDouble(key) != 0 ? 1 : 0;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        NBTTagCompound compound = player.getHeldItem(hand).getSubCompound(Main.MODID);
        Block block = worldIn.getBlockState(pos).getBlock();

        if (player.isSneaking()) {
            if (compound == null || compound.getDouble(key) == 0d) {
                if (worldIn.getBlockState(pos).getBlock().equals(Blocks.CAKE)) {
                    worldIn.setBlockToAir(pos);
                    player.getHeldItem(hand).getOrCreateSubCompound(Main.MODID).setDouble(key, 1d);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.getSubCompound(Main.MODID) != null) {

            NBTTagCompound compound = stack.getSubCompound(Main.MODID);

            if (compound.getDouble(key) != 0d) {

                if (playerIn.canEat(false)) {
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

        entityLiving.addPotionEffect(new PotionEffect(EffectInit.REPLENISHED, 12000, 1));

        ItemStack empty = new ItemStack(ItemInit.PLATE);
        empty.getOrCreateSubCompound(Main.MODID).setDouble(key, 0d);

        return empty;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }


}
