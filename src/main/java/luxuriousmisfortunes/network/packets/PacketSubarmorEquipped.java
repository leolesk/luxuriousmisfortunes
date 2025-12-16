package luxuriousmisfortunes.network.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.common.items.ItemNacreSubarmor;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSubarmorEquipped implements IMessage{

    private boolean isArmorOn;

    public PacketSubarmorEquipped() {};

    public PacketSubarmorEquipped (boolean value) {
        this.isArmorOn = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isArmorOn = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeBoolean(this.isArmorOn);

    }

    public boolean isArmorOn() {
        return this.isArmorOn;
    }

    public static class Handler implements IMessageHandler<PacketSubarmorEquipped, IMessage> {

        @Override
        public IMessage onMessage(PacketSubarmorEquipped message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            ISubarmorEquipped armor = player.getCapability(CapabilitySubarmorEquipped.CAP, null);

            if (armor != null) {

                armor.setArmorOn(message.isArmorOn);

                IAttributeInstance armorAttribute = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
                UUID uuid = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

                if (!message.isArmorOn) {

                    ItemStack armorStack = new ItemStack(ItemInit.CROWN);

                    if (armorAttribute.getModifier(uuid) != null) {
                        armorAttribute.removeModifier(uuid);
                    }

                    if (!player.addItemStackToInventory(armorStack)) {
                        player.dropItem(armorStack, false);
                    }
                } else {
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);

                    int armorValue = player.experienceLevel / 3;

                    if (armorAttribute.getModifier(uuid) != null) {
                        armorAttribute.removeModifier(uuid);
                    }

                    AttributeModifier mod = new AttributeModifier(uuid, ItemNacreSubarmor.name, armorValue, 0);
                    armorAttribute.applyModifier(mod);
                }
            }

            return null;
        }
    }

}
