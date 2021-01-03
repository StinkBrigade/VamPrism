package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.blocks.PrismaticTeslaBlock;
import kimera.really.works.vamprism.common.blocks.SunlightPoolBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeTileEntity;

public class PrismaticTeslaTileEntity extends AbstractPrismaStoreTileEntity implements ITickableTileEntity
{
    private int ticksExisted;

    public PrismaticTeslaTileEntity()
    {
        super(TileEntityRegistry.PRISMATIC_TESLA.get(), 3, 100.0F);
    }

    @Override
    public void tick()
    {
        ticksExisted++;
    }

    public int getTicksExisted()
    {
        return this.ticksExisted;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        BlockState state = this.getBlockState();
        BlockPos blockPos = this.getPos();
        if(state.get(PrismaticTeslaBlock.ENABLED) == true)
        {
            return IForgeTileEntity.INFINITE_EXTENT_AABB;
        }
        return new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(1, 3, 1));
    }

    @Override
    public int getTileEntityId()
    {
        return 515;
    }
}
