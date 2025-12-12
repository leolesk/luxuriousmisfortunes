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
import luxuriousmisfortunes.util.PayTimeHash;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
            tooltip.add("Activate with [SHIFT + RMB] while in hand");

        } else {
            tooltip.add("Times used: " + stack.getSubCompound(Main.MODID).getInteger(times_used));

            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
            UUID id = player.getUniqueID();

            if (id != null && PayTimeHash.payment_record.containsKey(id)) {
                LocalTime timer = PayTimeHash.payment_record.get(id);

                tooltip.add("Time left: " + timer.toString());
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.getSubCompound(Main.MODID) == null) {
            NBTTagCompound nbt = stack.getOrCreateSubCompound(Main.MODID);

            nbt.setBoolean(is_active, true);

            PayTimeHash.payment_record.put(playerIn.getUniqueID(), LocalTime.of(2, 0, 0));

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

}
