package luxuriousmisfortunes.client.render.armor;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.items.ItemVelvetSubarmor;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerSubarmor implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderPlayer;

    private static ResourceLocation subarmor = new ResourceLocation(Main.MODID, "textures/models/armor/" + ItemVelvetSubarmor.name + ".png");
    private static ResourceLocation subarmor_switched = new ResourceLocation(Main.MODID, "textures/models/armor/" + ItemVelvetSubarmor.name + "_" + "switched" + ".png");

    final ModelBiped modelBody = new ModelBiped(1.0F);
    final ModelBiped modelLegs = new ModelBiped(0.5F);

    public LayerSubarmor(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        ISubarmorEquipped cap = player.getCapability(CapabilitySubarmorEquipped.CAP, null);

        if (cap != null && cap.isArmorOn()) {

            boolean armorEquipped = true;
            ResourceLocation renderType = null;

            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack.isEmpty()) {
                    armorEquipped = false;
                    break;
                }
            }

            ResourceLocation texture = !armorEquipped ? subarmor : subarmor_switched;

            Minecraft.getMinecraft().getTextureManager().bindTexture(
                    renderType
                    );

            GlStateManager.pushMatrix();

            ModelBiped model = this.modelBody;

            model.setModelAttributes(this.renderPlayer.getMainModel());
            model.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);

            model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.popMatrix();

        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
