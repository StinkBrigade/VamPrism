package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.prisma.IPrismaStorer;
import net.minecraft.util.math.BlockPos;

public interface IPrismaLinker extends IPrismaStorer
{
    public static float DEFAULT_TRANSFER_SPEED = 10.0F;

    void onPlace();
    void onRemove();
    void onNeighbourUpdated(BlockPos neighbourPos);

    float getTransferRate();
    void setTransferRate(float transferRate);

    PrismaLinkerState getLinkerState();
    void setLinkerState(PrismaLinkerState state);
}
