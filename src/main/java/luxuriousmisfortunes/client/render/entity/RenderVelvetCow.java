package luxuriousmisfortunes.client.render.entity;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityVelvetCow;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class RenderVelvetCow extends RenderCow{

    public RenderVelvetCow(RenderManager p_i47210_1_) {
        super(p_i47210_1_);
        // TODO Auto-generated constructor stub
    }

    private static final ResourceLocation VELVET_COW_TEXTURES = new ResourceLocation(Main.MODID, "textures/entity/" + EntityVelvetCow.name + ".png");

    @Override
    protected ResourceLocation getEntityTexture(EntityCow entity)
    {
        return VELVET_COW_TEXTURES;
    }

}
