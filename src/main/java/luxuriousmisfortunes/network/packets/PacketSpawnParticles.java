package luxuriousmisfortunes.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSpawnParticles implements IMessage{

    BlockPos pos;
    EnumParticleTypes type;

    public PacketSpawnParticles() {};

    public PacketSpawnParticles (BlockPos pos, EnumParticleTypes type) {
        this.pos = pos;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.type = EnumParticleTypes.getParticleFromId(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.type.getParticleID());
    }

    public static class Handler implements IMessageHandler<PacketSpawnParticles, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSpawnParticles message, MessageContext ctx) {
            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

            spawnAABBParticles(player.getEntityWorld(), message.pos, message.type);

            return null;
        }
    }

    public static void spawnAABBParticles(World worldIn, BlockPos pos, EnumParticleTypes type)
    {
        int amount = 15;

        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getMaterial() != Material.AIR)
        {
            for (int i = 0; i < amount; ++i)
            {
                double d0 = worldIn.rand.nextGaussian() * 0.02D;
                double d1 = worldIn.rand.nextGaussian() * 0.02D;
                double d2 = worldIn.rand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(type, pos.getX() + worldIn.rand.nextFloat(), pos.getY() + worldIn.rand.nextFloat() * iblockstate.getBoundingBox(worldIn, pos).maxY, pos.getZ() + worldIn.rand.nextFloat(), d0, d1, d2);
            }
        }
        else
        {
            for (int i1 = 0; i1 < amount; ++i1)
            {
                double d0 = worldIn.rand.nextGaussian() * 0.02D;
                double d1 = worldIn.rand.nextGaussian() * 0.02D;
                double d2 = worldIn.rand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(type, pos.getX() + worldIn.rand.nextFloat(), pos.getY() + (double)worldIn.rand.nextFloat() * 1.0f, pos.getZ() + worldIn.rand.nextFloat(), d0, d1, d2, new int[0]);
            }
        }
    }

}
