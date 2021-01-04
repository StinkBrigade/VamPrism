package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.util.IPrismaStore;
import kimera.really.works.vamprism.common.util.PrismaStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class AbstractPrismaStorerTileEntity extends TileEntity implements IPrismaStore
{
    protected final PrismaStorage prismaStorage;

    public AbstractPrismaStorerTileEntity(TileEntityType<?> tileEntityTypeIn, int valueCount, float maxValue)
    {
        super(tileEntityTypeIn);

        prismaStorage = new PrismaStorage(valueCount, maxValue);
    }

    @Override @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        this.write(nbtTagCompound);
        return new SUpdateTileEntityPacket(this.pos, this.getTileEntityId(), nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        BlockState blockState = this.world.getBlockState(pos);
        this.read(blockState, packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        this.write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        this.read(blockState, parentNBTTagCompound);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.getInternalStorage().readNBT(parentNBTTagCompound);
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        this.getInternalStorage().writeNBT(parentNBTTagCompound);

        return parentNBTTagCompound;
    }

    @Override
    public PrismaStorage getInternalStorage()
    {
        return this.prismaStorage;
    }

    @Override
    public boolean isEmpty()
    {
        return this.getInternalStorage().isEmpty();
    }

    @Override
    public boolean isFull()
    {
        return this.getInternalStorage().isFull();
    }

    @Override
    public float getValueFillRatio(int index)
    {
        return this.getInternalStorage().getValueFillRatio(index);
    }

    @Override
    public float getTotalFillRatio()
    {
        return this.getInternalStorage().getTotalFillRatio();
    }

    @Override
    public float getCurrentPrismaValue(int index)
    {
        return this.getInternalStorage().getCurrentValue(index);
    }

    @Override
    public float getMaxPrismaValue(int index)
    {
        return this.getInternalStorage().getMaxValue(index);
    }

    @Override
    public void setCurrentPrismaValue(int index, float value)
    {
        this.getInternalStorage().setCurrentValue(index, value);
    }

    @Override
    public void setMaxPrismaValue(int index, float value)
    {
        this.getInternalStorage().setMaxValue(index, value);
    }

    public abstract int getTileEntityId();
}
