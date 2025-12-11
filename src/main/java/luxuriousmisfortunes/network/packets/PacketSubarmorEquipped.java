package luxuriousmisfortunes.network.packets;

import io.netty.buffer.ByteBuf;
import luxuriousmisfortunes.common.capabilities.CapabilitySubarmorEquipped;
import luxuriousmisfortunes.init.ItemInit;
import luxuriousmisfortunes.util.ISubarmorEquipped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

                if (!message.isArmorOn) {

                    ItemStack subarmor = new ItemStack(ItemInit.SUBARMOR);

                    if (!player.addItemStackToInventory(subarmor)) {
                        player.dropItem(subarmor, false);
                    }
                } else {
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                }

            }

            return null;
        }
    }

}
