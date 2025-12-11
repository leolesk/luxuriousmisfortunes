package luxuriousmisfortunes.client.render.entity;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityVelvetSlime;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderVelvetSlime extends RenderSlime{

    public RenderVelvetSlime(RenderManager p_i47193_1_) {
        super(p_i47193_1_);
        // TODO Auto-generated constructor stub
    }

    private static final ResourceLocation VELVET_SLIME_TEXTURES = new ResourceLocation(Main.MODID, "textures/entity/" + EntityVelvetSlime.name + ".png");

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entity)
    {
        return VELVET_SLIME_TEXTURES;
    }

}
