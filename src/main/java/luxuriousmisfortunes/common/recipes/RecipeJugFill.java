package luxuriousmisfortunes.common.recipes;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.items.ItemPorcelainBowl;
import luxuriousmisfortunes.common.items.ItemPorcelainJug;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeJugFill extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public static String name = "porcelain_jug_fill";

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {

        boolean is_jug = false;
        boolean is_potion = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            if (!inv.getStackInSlot(i).isEmpty()) {
                if (!is_jug && inv.getStackInSlot(i).getItem() instanceof ItemPorcelainJug) {

                    ItemStack jug = inv.getStackInSlot(i);
                    NBTTagCompound sub = jug.getSubCompound(Main.MODID);

                    is_jug = (sub == null) || sub.getString("2").isEmpty();
                } else if (!is_potion && inv.getStackInSlot(i).getItem() instanceof ItemPotion) {
                    is_potion = true;
                } else {
                    is_jug = false;
                    is_potion = false;
                }

            }
        }

        return is_jug && is_potion;
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack jug = null;
        ItemStack potion = null;

        Integer jug_slot = null;
        Integer potion_slot = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            ItemStack stack = inv.getStackInSlot(i);

            if (jug == null && !stack.isEmpty() && stack.getItem() instanceof ItemPorcelainJug) {

                jug = inv.getStackInSlot(i);
                jug_slot = i;

            } else if (potion == null && !stack.isEmpty() && inv.getStackInSlot(i).getItem() instanceof ItemPotion) {

                potion = inv.getStackInSlot(i);
                potion_slot = i;
            }

        }

        if ((jug != null && potion != null) && (jug_slot != null && potion_slot != null)) {

            Integer slot_free = null;

            for (int i = 0; i < 3; i++) {

                NBTTagCompound nbt = jug.getSubCompound(Main.MODID);

                if (nbt == null || nbt.getString(String.valueOf(i)).isEmpty()) {
                    slot_free = i;
                    break;
                }
            }

            if (slot_free != null) {

                for (PotionType type : PotionType.REGISTRY) {

                    String name_expected = type.getRegistryName().toString();
                    String name_actual = PotionUtils.getPotionFromItem(potion).getRegistryName().toString();

                    if (name_expected.equals(name_actual)) {

                        ItemStack result = jug.copy();
                        NBTTagCompound nbt = result.getOrCreateSubCompound(Main.MODID);
                        nbt.setString(String.valueOf(slot_free), name_expected);
                        nbt.setDouble(ItemPorcelainJug.key, 1);

                        return result;

                    }
                }
            }
        }

        return new ItemStack(Items.AIR);

    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ItemInit.JUG);
    }

}
