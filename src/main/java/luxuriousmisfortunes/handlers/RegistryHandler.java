package luxuriousmisfortunes.handlers;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityGolemPyrite;
import luxuriousmisfortunes.common.entities.EntityNacreGhast;
import luxuriousmisfortunes.common.entities.EntityPorcelainSpider;
import luxuriousmisfortunes.common.entities.EntityVelvetCow;
import luxuriousmisfortunes.common.entities.EntityVelvetSlime;
import luxuriousmisfortunes.common.recipes.RecipeBowlFill;
import luxuriousmisfortunes.init.BlockInit;
import luxuriousmisfortunes.init.EffectInit;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.SoundInit;
import luxuriousmisfortunes.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeBowlFill().setRegistryName(new ResourceLocation(Main.MODID, RecipeBowlFill.name)));
    }

    @SubscribeEvent
    public static void onPotionRegister(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(EffectInit.POTIONS.toArray(new Potion[0]));
    }

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {

        event.getRegistry().registerAll(SoundInit.SOUNDS.toArray(new SoundEvent[0]));
    }

    public static void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, EntityGolemPyrite.name),
                EntityGolemPyrite.class,
                EntityGolemPyrite.name,
                1,
                Main.instance,
                64, 1, true,
                0xb8a374,
                0xe0a010
                );
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, EntityNacreGhast.name),
                EntityNacreGhast.class,
                EntityNacreGhast.name,
                2,
                Main.instance,
                64, 1, true,
                0xe6b3ce,
                0x7d9fb5
                );
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, EntityPorcelainSpider.name),
                EntityPorcelainSpider.class,
                EntityPorcelainSpider.name,
                3,
                Main.instance,
                64, 1, true,
                0xb4b7bf,
                0x8ea1d1
                );
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, EntityVelvetCow.name),
                EntityVelvetCow.class,
                EntityVelvetCow.name,
                4,
                Main.instance,
                64, 1, true,
                0x420f27,
                0x9e0940
                );
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, EntityVelvetSlime.name),
                EntityVelvetSlime.class,
                EntityVelvetSlime.name,
                5,
                Main.instance,
                64, 1, true,
                0x210e47,
                0x4f2299
                );
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {

        for (Item item : ItemInit.ITEMS) {
            if(item instanceof IHasModel) {
                ((IHasModel)item).registerModels();
            }
        }
    }

}
