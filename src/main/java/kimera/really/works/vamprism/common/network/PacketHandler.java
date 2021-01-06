package kimera.really.works.vamprism.common.network;

import kimera.really.works.vamprism.common.VamPrism;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler
{
    // Change this whenever a major change is made to any packets, just to prevent weird crash reports :o)
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VamPrism.MODID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init()
    {
        int id = 0;
        INSTANCE.registerMessage(id++, PacketPrismaBufferUpdate.class, PacketPrismaBufferUpdate::encode, PacketPrismaBufferUpdate::decode, PacketPrismaBufferUpdate.Handler::handle);
    }
}
