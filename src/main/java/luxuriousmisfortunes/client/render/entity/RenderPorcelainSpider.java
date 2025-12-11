package luxuriousmisfortunes.client.render.entity;

import luxuriousmisfortunes.api.Main;
import luxuriousmisfortunes.common.entities.EntityPorcelainSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.util.ResourceLocation;

public class RenderPorcelainSpider extends RenderSpider<EntityCaveSpider>{

    public RenderPorcelainSpider(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    private static final ResourceLocation PORCELAIN_SPIDER_TEXTURES = new ResourceLocation(Main.MODID, "textures/entity/" + EntityPorcelainSpider.name + ".png");

    @Override
    protected ResourceLocation getEntityTexture(EntityCaveSpider entity)
    {
        return PORCELAIN_SPIDER_TEXTURES;
    }

}
