package luxuriousmisfortunes.handlers;

import java.util.UUID;

import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.items.ItemNacreSubarmor;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.init.KeybindInit;
import luxuriousmisfortunes.network.packets.Network;
import luxuriousmisfortunes.network.packets.PacketSubarmorEquipped;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod.EventBusSubscriber
public class SubarmorEquippedHandler {

    @SubscribeEvent
    public static void onPickupXP(PlayerPickupXpEvent event) {

        EntityPlayer player = event.getEntityPlayer();

        if (player.getCapability(CapabilitySubarmorEquipped.CAP, null) != null) {

            ISubarmorEquipped cap = player.getCapability(CapabilitySubarmorEquipped.CAP, null);

            if (cap.isArmorOn()) {
                int armorValue = event.getEntityPlayer().experienceLevel / 3;

                IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);

                UUID uuid = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

                if (armor.getModifier(uuid) != null) {
                    armor.removeModifier(uuid);
                }

                AttributeModifier mod = new AttributeModifier(uuid, ItemNacreSubarmor.name, armorValue, 0);
                armor.applyModifier(mod);
            }
        }

    }

    @SubscribeEvent
    public static void onSubarmorKeyInput(InputEvent event) {
        if (KeybindInit.PUT_SUBARMOR_ON.isPressed()) {

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ISubarmorEquipped armor = player.getCapability(CapabilitySubarmorEquipped.CAP, null);

            if (armor != null) {
                if (!armor.isArmorOn()) {
                    if (player.getHeldItemMainhand().getItem().equals(ItemInit.SUBARMOR)) {
                        armor.setArmorOn(true);
                        Network.sendToPlayerSP(new PacketSubarmorEquipped(true));
                    }
                } else {
                    armor.setArmorOn(false);
                    Network.sendToPlayerSP(new PacketSubarmorEquipped(false));
                }
            }

        }
    }

}
