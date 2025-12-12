package luxuriousmisfortunes.common.effects;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.init.EffectInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class EffectMindtrick extends Potion{

    public static String name = "mindtrick";

    public static TextComponentTranslation complaint = new TextComponentTranslation("complaint" + "." + Main.MODID + "." + name);

    public static SoundEvent[] events = {
            SoundEvents.ENTITY_CREEPER_PRIMED,
            SoundEvents.AMBIENT_CAVE,
            SoundEvents.ENTITY_PLAYER_BREATH,
            SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD,
            SoundEvents.ENTITY_SILVERFISH_AMBIENT,
    };

    protected EffectMindtrick(String name) {
        super(true, 0);

        this.setRegistryName(Main.MODID, name);
        this.setPotionName("effect." + name);

        EffectInit.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {

        if (entityLivingBaseIn instanceof EntityPlayer) {

            EntityPlayer playerIn = (EntityPlayer)entityLivingBaseIn;

            World world = playerIn.world;

            if (world.rand.nextInt(10) < 9) {
                playerIn.world.playSound(
                        null,
                        playerIn.getPosition(),
                        events[world.rand.nextInt(events.length)],
                        SoundCategory.AMBIENT,
                        2.0F,
                        1.0F
                        );
            }

            int radius = 3;

            int offsetX = world.rand.nextInt(radius * 2 + 1) - radius;
            int offsetZ = world.rand.nextInt(radius * 2 + 1) - radius;

            BlockPos spawnPos = playerIn.getPosition().add(offsetX, playerIn.getYOffset(), offsetZ);

            if (world.isAirBlock(spawnPos) && world.isAirBlock(spawnPos.up())) {
                Entity mob;

                int choice = world.rand.nextInt(1);
                switch (choice) {
                    case 0:
                        mob = new EntityCreeper(world);
                        break;
                    default:
                        mob = new EntityVex(world);
                        break;
                }

                mob.setPosition(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                world.spawnEntity(mob);
            }

        }

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

}