package luxuriousmisfortunes.handlers;

import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquippedProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityHandler {

    @SubscribeEvent
    public static void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityPlayer) {

            event.addCapability(CapabilitySubarmorEquipped.KEY, new CapabilitySubarmorEquippedProvider());
        }
    }

}
