package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.blocks.PrismaticTeslaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeTileEntity;

public class PrismaStoreTileEntity extends AbstractTeslaLinkerTileEntity
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
