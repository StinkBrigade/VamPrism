package kimera.really.works.vamprism.common.tileentity;

import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractTeslaLinkerTileEntity extends AbstractPrismaStorerTileEntity implements ITeslaLinker
{
    private TeslaLinkerState linkerState;

    public AbstractTeslaLinkerTileEntity(TileEntityType<?> tileEntityTypeIn, int valueCount, float maxValue, TeslaLinkerState defaultState)
    {
        super(tileEntityTypeIn, valueCount, maxValue);

        this.linkerState = defaultState;
    }

    @Override
    public TeslaLinkerState getLinkerState()
    {
        return this.linkerState;
    }

    @Override
    public void setLinkerState(TeslaLinkerState state)
    {
        this.linkerState = state;
    }
}
