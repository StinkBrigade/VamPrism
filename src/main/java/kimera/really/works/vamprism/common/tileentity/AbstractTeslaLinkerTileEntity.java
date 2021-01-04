package kimera.really.works.vamprism.common.tileentity;

import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractTeslaLinkerTileEntity extends AbstractPrismaStorerTileEntity implements ITeslaLinker
{
    public AbstractTeslaLinkerTileEntity(TileEntityType<?> tileEntityTypeIn, int valueCount, float maxValue)
    {
        super(tileEntityTypeIn, valueCount, maxValue);
    }
}
