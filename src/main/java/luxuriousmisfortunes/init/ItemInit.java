package luxuriousmisfortunes.init;

import java.util.ArrayList;
import java.util.List;

import luxuriousmisfortunes.common.basic.ItemBase;
import luxuriousmisfortunes.common.basic.ItemBlockBase;
import luxuriousmisfortunes.common.entities.EntityGolemPyrite;
import luxuriousmisfortunes.common.entities.EntityNacreGhast;
import luxuriousmisfortunes.common.entities.EntityPorcelainSpider;
import luxuriousmisfortunes.common.entities.EntityVelvetCow;
import luxuriousmisfortunes.common.entities.EntityVelvetSlime;
import luxuriousmisfortunes.common.items.ItemPorcelainBowl;
import luxuriousmisfortunes.common.items.ItemPorcelainJug;
import luxuriousmisfortunes.common.items.ItemPorcelainMeal;
import luxuriousmisfortunes.common.items.ItemPorcelainPlate;
import luxuriousmisfortunes.common.items.ItemPorcelainScalpel;
import luxuriousmisfortunes.common.items.ItemPorcelainTissue;
import luxuriousmisfortunes.common.items.ItemPyriteFishingRod;
import luxuriousmisfortunes.common.items.ItemPyriteGum;
import luxuriousmisfortunes.common.items.ItemSubstance;
import luxuriousmisfortunes.common.items.ItemVelvetCookie;
import luxuriousmisfortunes.common.items.ItemVelvetSubarmor;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.Item;

public class ItemInit {

    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item TOTEM_BODY = new ItemBlockBase(BlockInit.TOTEM_BODY);
    public static final Item TOTEM_STONE = new ItemBlockBase(BlockInit.TOTEM_STONE);
    public static final Item TOTEM_POLE = new ItemBlockBase(BlockInit.TOTEM_POLE);
    public static final Item TOTEM_HEAD_INACTIVE = new ItemBlockBase(BlockInit.TOTEM_HEAD_INACTIVE);
    public static final Item TOTEM_MOUTH = new ItemBlockBase(BlockInit.TOTEM_MOUTH);
    public static final Item TOTEM_TOOTH = new ItemBlockBase(BlockInit.TOTEM_TOOTH);

    public static final Item MATTER_PYRITE = new ItemSubstance(ItemSubstance.variant[0],
            EntityIronGolem.class,
            EntityGolemPyrite.class,
            false,
            null,
            null);

    public static final Item MATTER_PORCELAIN = new ItemSubstance(ItemSubstance.variant[1],
            EntitySpider.class,
            EntityPorcelainSpider.class,
            false,
            null,
            null);

    public static final Item MATTER_VELVET = new ItemSubstance(ItemSubstance.variant[2],
            EntityCow.class,
            EntityVelvetCow.class,
            true,
            EntitySlime.class,
            EntityVelvetSlime.class);

    public static final Item MATTER_NACRE = new ItemSubstance(ItemSubstance.variant[3],
            EntityGhast.class,
            EntityNacreGhast.class,
            false,
            null,
            null);

    public static final Item MATERIAL_PYRITE = new ItemBase("pyrite_crystal");
    public static final Item MATERIAL_VELVET = new ItemBase("velvet_leather");
    public static final Item MATERIAL_PORCELAIN = new ItemBase("porcelain_chitin");
    public static final Item MATERIAL_NACRE = new ItemBase("nacre_feather");

    public static final Item BOWL = new ItemPorcelainBowl(ItemPorcelainBowl.name, 10, false);
    public static final Item PLATE = new ItemPorcelainPlate(ItemPorcelainPlate.name, 20, false);
    public static final Item JUG = new ItemPorcelainJug(ItemPorcelainJug.name, 0, false);
    public static final Item SCALPEL = new ItemPorcelainScalpel(ItemPorcelainScalpel.name);
    public static final Item MEAL = new ItemPorcelainMeal(ItemPorcelainMeal.name);
    public static final Item PSEUDO_MEAT = new ItemPorcelainTissue(ItemPorcelainTissue.name, 10, false);

    public static final Item CUSTARD = new ItemBase("animated_custard");
    public static final Item RUM = new ItemBase("rum_bottle");
    public static final Item COOKIE = new ItemVelvetCookie(ItemVelvetCookie.name, 10, false);
    public static final Item SUBARMOR = new ItemVelvetSubarmor(ItemVelvetSubarmor.name);
    public static final Item PATCH = new ItemBase("velvet_patch");

    public static final Item GUM = new ItemPyriteGum(ItemPyriteGum.name, 4, false);
    public static final Item FISHING_ROD = new ItemPyriteFishingRod(ItemPyriteFishingRod.name);
    //        public static final Item FOOLS_PUMPKIN = new ItemBlockBase(BlockInit.FOOLS_PUMPKIN);


    public static Item[] acceptables = {
            ItemInit.MATTER_PYRITE,
            ItemInit.MATTER_PORCELAIN,
            ItemInit.MATTER_VELVET,
            ItemInit.MATTER_NACRE
    };
}
