package luxuriousmisfortunes.common.basic;

import java.util.List;

import javax.annotation.Nullable;

import luxuriousmisfortunes.api.Main;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTotemStructure extends BlockBase{

    public static TextComponentTranslation unbreakable = new TextComponentTranslation("tooltip" + "." + Main.MODID + "." + "element_unbreakable");

    public BlockTotemStructure(String name, Material materialIn) {
        super(name, materialIn);

        this.setBlockUnbreakable();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(unbreakable.getFormattedText());
    }
}
