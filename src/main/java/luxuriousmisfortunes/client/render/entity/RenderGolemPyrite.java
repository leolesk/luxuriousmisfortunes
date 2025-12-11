package luxuriousmisfortunes.client.render.entity;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityGolemPyrite;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderGolemPyrite extends RenderIronGolem{

    public RenderGolemPyrite(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    private static final ResourceLocation PYRITE_GOLEM_TEXTURES = new ResourceLocation(Main.MODID, "textures/entity/" + EntityGolemPyrite.name + ".png");

    @Override
    protected ResourceLocation getEntityTexture(EntityIronGolem entity)
    {
        return PYRITE_GOLEM_TEXTURES;
    }

}
