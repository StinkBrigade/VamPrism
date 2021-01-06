package kimera.really.works.vamprism.common.network;

import kimera.really.works.vamprism.common.prisma.IPrismaStorer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPrismaBufferUpdate
{
    private final BlockPos prismaStorerPos;
    private final float[] prismaValues;

    public PacketPrismaBufferUpdate(BlockPos prismaStorerPos, float[] prismaValues)
    {
        this.prismaStorerPos = prismaStorerPos;
        this.prismaValues = prismaValues;
    }

    public static void encode(PacketPrismaBufferUpdate packet, PacketBuffer buffer)
    {
        buffer.writeLong(packet.prismaStorerPos.toLong());

        int valueCount = packet.prismaValues.length;
        buffer.writeInt(valueCount);
        for(int i = 0; i < valueCount; i++)
        {
            buffer.writeFloat(packet.prismaValues[i]);
        }
    }

    public static PacketPrismaBufferUpdate decode(PacketBuffer buffer)
    {
        BlockPos prismaStorerPos = BlockPos.fromLong(buffer.readLong());

        int valueCount = buffer.readInt();
        float[] prismaValues = new float[valueCount];
        for(int i = 0; i < valueCount; i++)
        {
            prismaValues[i] = buffer.readFloat();
        }

        return new PacketPrismaBufferUpdate(prismaStorerPos, prismaValues);
    }

    public static class Handler
    {
        public static void handle(final PacketPrismaBufferUpdate packet, final Supplier<NetworkEvent.Context> context)
        {
            if(!context.get().getDirection().getReceptionSide().isServer())
            {
                context.get().enqueueWork(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Minecraft mc = Minecraft.getInstance();
                        World world = mc.world;

                        BlockPos prismaStorerPos = packet.prismaStorerPos;
                        if(world.isAreaLoaded(prismaStorerPos, 1))
                        {
                            TileEntity tileEntity = world.getTileEntity(prismaStorerPos);
                            if(tileEntity instanceof IPrismaStorer)
                            {
                                IPrismaStorer prismaStorerTileEntity = (IPrismaStorer) tileEntity;
                                prismaStorerTileEntity.updateBufferOnClient(packet.prismaValues);
                            }
                        }
                    }
                });
            }

            context.get().setPacketHandled(true);
            return;
        }
    }
}
