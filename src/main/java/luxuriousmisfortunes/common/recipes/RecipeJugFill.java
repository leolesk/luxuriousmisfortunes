package luxuriousmisfortunes.common.recipes;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.items.ItemPorcelainBowl;
import luxuriousmisfortunes.init.ItemInit;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeJugFill extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public static String name = "porcelain_jug_fill";

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {

        boolean is_jug = false;
        boolean is_potion = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            if (is_jug && is_potion)
                return true;

            if (!is_jug && inv.getStackInSlot(i).getItem() instanceof ItemPorcelainBowl) {

                ItemStack jug = inv.getStackInSlot(i);
                NBTTagCompound sub = jug.getOrCreateSubCompound(Main.MODID);

                is_jug = (sub == null) || sub.getString("2").isEmpty();
            } else if (!is_potion && inv.getStackInSlot(i).getItem() instanceof ItemSoup) {
                is_potion = true;
            }

        }

        return false;
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack jug = null;
        ItemStack potion = null;

        Integer jug_slot = null;
        Integer potion_slot = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            if (jug != null && potion != null) {
                break;
            }

            ItemStack stack = inv.getStackInSlot(i);

            if (jug == null && !stack.isEmpty() && stack.getItem() instanceof ItemPorcelainBowl) {

                jug = inv.getStackInSlot(i);
                jug_slot = i;

            } else if (potion == null && !stack.isEmpty() && inv.getStackInSlot(i).getItem() instanceof ItemSoup) {

                potion = inv.getStackInSlot(i);
                potion_slot = i;
            }

        }

        if (jug != null && potion != null) {

            Integer slot_free = null;

            for (int i = 0; i < 3; i++) {
                if (jug.getOrCreateSubCompound(Main.MODID).getString(String.valueOf(i)).isEmpty()) {
                    slot_free = i;
                    break;
                }
            }

            if (slot_free != null) {
                for (Item type : ItemPorcelainBowl.types) {

                    String name_expected = type.getRegistryName().toString();
                    String name_actual = potion.getItem().getRegistryName().toString();

                    if (name_expected.equals(name_actual)) {

                        jug.getOrCreateSubCompound(Main.MODID).setString(String.valueOf(slot_free), name_actual);

                        if (potion_slot != null && jug_slot != null) {
                            inv.removeStackFromSlot(potion_slot);
                            inv.removeStackFromSlot(jug_slot);
                        }

                        return jug.copy();

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
