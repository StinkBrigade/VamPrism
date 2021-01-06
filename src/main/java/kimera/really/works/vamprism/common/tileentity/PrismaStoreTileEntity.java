package kimera.really.works.vamprism.common.tileentity;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class PrismaStoreTileEntity extends AbstractPrismaLinkerTileEntity
{
    public PrismaStoreTileEntity()
    {
        super(TileEntityRegistry.PRISMA_STORE.get(), 3, 10000.0F, TeslaLinkerState.STORING);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        BlockPos blockPos = this.getPos();
        return new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(1, 2, 1));
    }

    @Override
    public int getTileEntityId()
    {
        return 516;
    }
}
