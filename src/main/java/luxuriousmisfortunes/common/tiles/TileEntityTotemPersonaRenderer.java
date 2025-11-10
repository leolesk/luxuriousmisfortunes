package luxuriousmisfortunes.common.tiles;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import luxuriousmisfortunes.api.Main;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityTotemPersonaRenderer extends TileEntitySkullRenderer {

    private static final ResourceLocation TOTEM_TEXTURES = new ResourceLocation(Main.MODID, "textures/blocks/totem_persona.png");

    @Override
    public void renderSkull(float x, float y, float z, EnumFacing facing, float rotationIn, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {

        this.bindTexture(TOTEM_TEXTURES);


    }

}
