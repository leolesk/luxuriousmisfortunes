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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeBowlFill extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public static String name = "porcelain_bowl_fill";

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {

        boolean is_bowl = false;
        boolean is_soup = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            if (is_bowl && is_soup)
                return true;

            if (!is_bowl && inv.getStackInSlot(i).getItem() instanceof ItemPorcelainBowl) {

                ItemStack bowl = inv.getStackInSlot(i);
                NBTTagCompound sub = bowl.getOrCreateSubCompound(Main.MODID);

                is_bowl = (sub == null) || sub.getString("2").isEmpty();
            } else if (!is_soup && inv.getStackInSlot(i).getItem() instanceof ItemSoup) {
                is_soup = true;
            }

        }

        return false;
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack bowl = null;
        ItemStack soup = null;

        Integer bowl_slot = null;
        Integer soup_slot = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            if (bowl != null && soup != null) {
                break;
            }

            ItemStack stack = inv.getStackInSlot(i);

            if (bowl == null && !stack.isEmpty() && stack.getItem() instanceof ItemPorcelainBowl) {

                bowl = inv.getStackInSlot(i);
                bowl_slot = i;

            } else if (soup == null && !stack.isEmpty() && inv.getStackInSlot(i).getItem() instanceof ItemSoup) {

                soup = inv.getStackInSlot(i);
                soup_slot = i;
            }

        }

        if (bowl != null && soup != null) {

            Integer slot_free = null;

            for (int i = 0; i < 3; i++) {
                if (bowl.getOrCreateSubCompound(Main.MODID).getString(String.valueOf(i)).isEmpty()) {
                    slot_free = i;
                    break;
                }
            }

            if (slot_free != null) {
                for (Item type : ItemPorcelainBowl.types) {

                    String name_expected = type.getRegistryName().toString();
                    String name_actual = soup.getItem().getRegistryName().toString();

                    if (name_expected.equals(name_actual)) {

                        NBTTagCompound nbt = bowl.getOrCreateSubCompound(Main.MODID);
                        nbt.setString(String.valueOf(slot_free), name_actual);
                        nbt.setDouble(ItemPorcelainBowl.key, 1);

                        if (soup_slot != null && bowl_slot != null) {
                            inv.removeStackFromSlot(soup_slot);
                            inv.removeStackFromSlot(bowl_slot);
                        }

                        return bowl.copy();

                    }

                }
            }
        }

        return new ItemStack(Items.AIR);

    }

    @Override
    public boolean canFit(int width, int height) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        // TODO Auto-generated method stub
        return new ItemStack(ItemInit.BOWL);
    }

}
