package luxuriousmisfortunes.client.render.entity;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityNacreGhast;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;

public class RenderNacreGhast extends RenderGhast{

    public RenderNacreGhast(RenderManager renderManagerIn) {
        super(renderManagerIn);
        // TODO Auto-generated constructor stub
    }

    private static final ResourceLocation NACRE_GHAST_TEXTURES = new ResourceLocation(Main.MODID, "textures/entity/" + EntityNacreGhast.name + ".png");

    @Override
    protected ResourceLocation getEntityTexture(EntityGhast entity)
    {
        return NACRE_GHAST_TEXTURES;
    }

}
