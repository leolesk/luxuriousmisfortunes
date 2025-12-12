package luxuriousmisfortunes.common.entities;

import javax.annotation.Nullable;

import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityVelvetSlime extends EntitySlime{

    public static String name = "velvet_slime";
    static String key = "drop_chance";

    public EntityVelvetSlime(World worldIn) {
        super(worldIn);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return null;
    }

    @Override
    public void setSlimeSize(int size, boolean resetHealth)
    {
        super.setSlimeSize(size, resetHealth);
    }

    @Override
    public void setDead()
    {
        this.isDead = true;
    }

    @Override
    public void onUpdate() {

        int size = this.getSlimeSize();
        World world = this.getEntityWorld();

        NBTTagCompound nbt = this.getEntityData();
        if (nbt.getInteger(key) == 0) {
            nbt.setInteger(key, size);
        }

        if (!world.isRemote && this.ticksExisted % 40 == 0) {
            if (size > 1) {
                this.setSlimeSize(size - 1, false);
            } else {

                double chance = 0;

                switch (nbt.getInteger(key)) {
                    case 1: chance = 0.2;
                    break;
                    case 2: chance = 0.5;
                    break;
                    case 3: chance = 0.8;
                    break;
                }

                if (this.rand.nextDouble() < chance) {
                    world.spawnEntity(new EntityItem(world, this.posX, this.posY, this.posZ, new ItemStack(ItemInit.CUSTARD)));
                }

                world.createExplosion(null, this.posX, this.posY, this.posZ, 0, false);
                this.setDead();
            }
        }

        super.onUpdate();
    }



}
