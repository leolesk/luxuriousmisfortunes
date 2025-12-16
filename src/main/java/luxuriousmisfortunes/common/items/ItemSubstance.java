package luxuriousmisfortunes.common.items;

import java.lang.reflect.Constructor;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemBase;
import luxuriousmisfortunes.common.entities.EntityVelvetSlime;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSubstance extends ItemBase {

    public static String[] variant = {
            "pyrite",
            "porcelain",
            "velvet",
            "nacre"
    };

    public static String times_used = "times_used";
    public static String is_active = "is_active";
    public static String time_left = "time_left";
    public static String required_mob = "required_mob";
    public static String mob_name = "mob_name";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + "matter" + "." + "description");
    TextComponentTranslation tooltip_used = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + "matter" + "." + times_used);
    TextComponentTranslation tooltip_time = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + "matter" + "." + time_left);
    TextComponentTranslation tooltip_mob = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + "matter" + "." + required_mob);

    Class<? extends EntityLivingBase> entityFromTransform;
    Class<? extends EntityLivingBase> entityToTransform;
    Class<? extends EntityLivingBase> entityFromConditional;
    Class<? extends EntityLivingBase> entityToConditional;
    boolean condition;

    public ItemSubstance(String name, Class<? extends EntityLivingBase> entityFromTransform,
            Class<? extends EntityLivingBase> entityToTransform,
            boolean condition,
            @Nullable Class<? extends EntityLivingBase> entityFromConditional,
            @Nullable Class<? extends EntityLivingBase> entityToConditional) {

        super(name);

        this.entityFromTransform = entityFromTransform;
        this.entityToTransform = entityToTransform;
        this.entityFromConditional = entityFromConditional;
        this.entityToConditional = entityToConditional;

        this.condition = condition;

        this.setMaxStackSize(1);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {

        if (stack.getSubCompound(Main.MODID) != null) {
            if ((target.getClass()).equals(this.entityFromTransform) || this.condition && (target.getClass()).equals(this.entityFromConditional)) {
                World worldIn = target.getEntityWorld();
                Integer size = null;

                try {

                    Constructor<? extends EntityLivingBase> constructor = this.entityToTransform.getConstructor(World.class);

                    if (this.condition && (target.getClass()).equals(this.entityFromConditional)) {
                        constructor = this.entityToConditional.getConstructor(World.class);

                        if (target.getClass().equals(EntitySlime.class)) {
                            EntitySlime slime = (EntitySlime)target;
                            size = slime.getSlimeSize();
                        }
                    }

                    EntityLivingBase result = constructor.newInstance(worldIn);
                    result.setPosition(target.posX, target.posY, target.posZ);

                    if (size != null) {
                        ((EntityVelvetSlime)result).setSlimeSize(size, false);

                    }

                    if (!worldIn.isRemote) {
                        worldIn.removeEntity(target);
                        worldIn.spawnEntity(result);

                        NBTTagCompound nbt = stack.getSubCompound(Main.MODID);
                        nbt.setInteger(times_used, nbt.getInteger(times_used) + 1);

                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (stack.getSubCompound(Main.MODID) == null ||
                !stack.getSubCompound(Main.MODID).getBoolean(is_active)) {
            tooltip.add(this.description.getFormattedText());

        } else {

            for (String element : variant) {
                if (stack.getItem().getRegistryName().getPath().equals(element)) {
                    TextComponentTranslation key = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + element + "." + mob_name);
                    tooltip.add(this.tooltip_mob.getFormattedText() + " " + key.getFormattedText());
                    break;
                }
            }

            tooltip.add(this.tooltip_used.getFormattedText() + " " + stack.getSubCompound(Main.MODID).getInteger(times_used));

            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
            UUID id = player.getUniqueID();

            if (id != null && PayTimeHash.payment_record.containsKey(id)) {
                LocalTime timer = PayTimeHash.payment_record.get(id);

                tooltip.add(this.tooltip_time.getFormattedText() + " " + timer.toString());
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (playerIn.isSneaking()) {
            if (stack.getSubCompound(Main.MODID) == null) {
                NBTTagCompound nbt = stack.getOrCreateSubCompound(Main.MODID);

                nbt.setBoolean(is_active, true);

                PayTimeHash.payment_record.put(playerIn.getUniqueID(), LocalTime.of(2, 0, 0));

                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab != Main.tabMod) return;

        items.clear();

        items.add(new ItemStack (ItemInit.TOTEM_BODY));
        items.add(new ItemStack (ItemInit.TOTEM_STONE));
        items.add(new ItemStack (ItemInit.TOTEM_MOUTH));
        items.add(new ItemStack (ItemInit.TOTEM_TOOTH));
        items.add(new ItemStack (ItemInit.TOTEM_HEAD_INACTIVE));
        items.add(new ItemStack (ItemInit.TOTEM_BRAZIER));

        items.add(new ItemStack (ItemInit.MATTER_PYRITE));
        items.add(new ItemStack (ItemInit.MATERIAL_PYRITE));
        items.add(new ItemStack (ItemInit.FISHING_ROD));
        items.add(new ItemStack (ItemInit.GUM));

        items.add(new ItemStack (ItemInit.MATTER_PORCELAIN));
        items.add(new ItemStack (ItemInit.MATERIAL_PORCELAIN));
        items.add(new ItemStack (ItemInit.BOWL));
        items.add(new ItemStack (ItemInit.PLATE));
        items.add(new ItemStack (ItemInit.JUG));

        items.add(new ItemStack (ItemInit.MATTER_VELVET));
        items.add(new ItemStack (ItemInit.MATERIAL_VELVET));
        items.add(new ItemStack (ItemInit.PATCH));
        items.add(new ItemStack (ItemInit.COOKIE, 1, 0));
        items.add(new ItemStack (ItemInit.COOKIE, 1, 1));
        items.add(new ItemStack (ItemInit.CUSTARD));
        items.add(new ItemStack (ItemInit.RUM));

        items.add(new ItemStack (ItemInit.MATTER_NACRE));
        items.add(new ItemStack (ItemInit.MATERIAL_NACRE));
        items.add(new ItemStack (ItemInit.SCALPEL));
        items.add(new ItemStack (ItemInit.CROWN));
        items.add(new ItemStack (ItemInit.MEAL));

    }

}
