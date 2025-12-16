package luxuriousmisfortunes.common.items;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.basic.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNacreScalpel extends ItemBase {

    public static String name = "nacre_scalpel";

    TextComponentTranslation description = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + name + "." + "description");

    public ItemNacreScalpel(String name) {
        super(name);

        this.setMaxDamage(100);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {

        ItemStack toSpawn = null;
        World world = playerIn.getEntityWorld();
        NBTTagCompound compound = target.getEntityData();

        if (!world.isRemote) {

            if (!compound.hasKey("lastShearedTime") || world.getTotalWorldTime() - compound.getLong("lastShearedTime") >= 2400L) {
                if (target instanceof EntityCow) {
                    toSpawn = new ItemStack(Items.LEATHER, 1);
                } else if (target instanceof EntityChicken) {
                    toSpawn = new ItemStack(Items.FEATHER, 1);
                } else if (target instanceof EntitySquid) {
                    toSpawn = new ItemStack(Items.DYE, 1);
                }
            }

            if (toSpawn != null) {

                world.spawnEntity(new EntityItem(world, target.posX, target.posY, target.posZ, toSpawn));
                world.createExplosion(null, target.posX, (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0, target.posZ, 0, false);

                if (!playerIn.isCreative()) {
                    stack.damageItem(1, playerIn);
                }

                compound.setLong("lastShearedTime", world.getTotalWorldTime());
                target.attackEntityAsMob(playerIn);
                return true;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(this.description.getFormattedText());
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

}
