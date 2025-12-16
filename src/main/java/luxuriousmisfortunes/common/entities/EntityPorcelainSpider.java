package luxuriousmisfortunes.common.entities;

import javax.annotation.Nullable;

import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPorcelainSpider extends EntityCaveSpider {

    public static String name = "porcelain_spider";

    public EntityPorcelainSpider(World worldIn) {
        super(worldIn);
        // TODO Auto-generated constructor stub
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return null;
    }

    @Override
    @Nullable
    protected Item getDropItem()
    {
        return ItemInit.MATERIAL_PORCELAIN;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item item = this.getDropItem();

        if (item != null)
        {
            int i = 1 + this.rand.nextInt(3);

            if (lootingModifier > 0)
            {
                i += this.rand.nextInt(lootingModifier + 1);
            }

            for (int j = 0; j < i; ++j)
            {
                this.dropItem(item, 1);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (super.attackEntityAsMob(entityIn))
        {
            if (entityIn instanceof EntityLivingBase)
            {
                int i = 0;

                if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    i = 7;
                }
                else if (this.world.getDifficulty() == EnumDifficulty.HARD)
                {
                    i = 15;
                }

                if (i > 0)
                {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, i * 20, 0));
                }
            }

            return true;
        } else
            return false;
    }

}
