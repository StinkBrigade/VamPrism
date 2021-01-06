package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.VamPrism;
import kimera.really.works.vamprism.common.util.CommonUtil;
import kimera.really.works.vamprism.common.prisma.IPrismaStorer;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPrismaLinkerTileEntity extends AbstractPrismaStorerTileEntity implements IPrismaLinker
{
    private PrismaLinkerState linkerState;

    private List<BlockPos> connectedLinkerPositions;
    private List<IPrismaLinker> connectedLinkers;

    private float transferRate;

    public AbstractPrismaLinkerTileEntity(TileEntityType<?> tileEntityTypeIn, int valueCount, float maxValue, PrismaLinkerState defaultState, float transferRate)
    {
        super(tileEntityTypeIn, valueCount, maxValue);

        this.linkerState = defaultState;
        this.transferRate = transferRate;
    }

    public AbstractPrismaLinkerTileEntity(TileEntityType<?> tileEntityTypeIn, int valueCount, float maxValue, PrismaLinkerState defaultState)
    {
        this(tileEntityTypeIn, valueCount, maxValue, defaultState, IPrismaLinker.DEFAULT_TRANSFER_SPEED);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.transferRate = parentNBTTagCompound.getFloat("transferRate");

        this.setConnectedLinkerPositions(CommonUtil.readBlockPosListNBT(parentNBTTagCompound, "connectedLinkers"));
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        parentNBTTagCompound.putFloat("transferRate", this.transferRate);

        CommonUtil.writeBlockPosListNBT(parentNBTTagCompound, "connectedLinkers", this.getConnectedLinkerPositions());

        return super.write(parentNBTTagCompound);
    }

    @Override
    public void tick()
    {
        if(!world.isRemote)
        {
            this.processConnectedLinkers();
        }

        super.tick();
    }

    private void processConnectedLinkers()
    {
        for(IPrismaLinker linker : this.getConnectedLinkers())
        {
            // If this Linker can output and the target Linker can accept input
            if(this.getLinkerState().canGiveOutput && linker.getLinkerState().canTakeInput)
            {
                IPrismaStorer.handlePrismaTransfer(this, linker, this.getTransferRate(), 0.0F);
            }
        }
    }

    @Override
    public void onPlace()
    {
        this.setConnectedLinkerPositions(this.findAdjacentLinkers());
    }

    @Override
    public void onNeighbourUpdated(BlockPos neighbourPos)
    {
        boolean hasLinker = this.testAdjacentLinker(neighbourPos);
        boolean existingLinkerConnected = this.hasConnectedLinkerAt(neighbourPos);

        if(hasLinker && !existingLinkerConnected)
        {
            this.addConnectedLinkerPosition(neighbourPos);
        }
        else if(!hasLinker && existingLinkerConnected)
        {
            this.removeConnectedLinkerPosition(neighbourPos);
        }
    }

    @Override
    public void onRemove()
    {

    }

    private List<BlockPos> findAdjacentLinkers()
    {
        List<BlockPos> linkerPositions = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            int x = 1 - MathHelper.abs(i - 1);
            int z = MathHelper.abs(2 - i) -  1;

            BlockPos targetPos = new BlockPos(this.pos.getX() + x, this.pos.getY(), this.pos.getZ() + z);
            if(testAdjacentLinker(targetPos))
            {
                linkerPositions.add(targetPos);
            }
        }

        return linkerPositions;
    }

    private boolean testAdjacentLinker(BlockPos targetPos)
    {
        TileEntity targetTileEntity = this.world.getTileEntity(targetPos);
        if(targetTileEntity != null && targetTileEntity instanceof IPrismaLinker)
        {
            return true;
        }

        return false;
    }

    public List<BlockPos> getConnectedLinkerPositions()
    {
        if(this.connectedLinkerPositions == null)
        {
            this.setConnectedLinkerPositions(new ArrayList<>());
        }
        return this.connectedLinkerPositions;
    }

    public List<IPrismaLinker> getConnectedLinkers()
    {
        if(this.connectedLinkers == null || this.connectedLinkers.size() != this.getConnectedLinkerPositions().size())
        {
            this.updateConnectedLinkers();
        }
        return this.connectedLinkers;
    }

    public boolean hasConnectedLinkerAt(BlockPos pos)
    {
        return this.getConnectedLinkerPositions().contains(pos);
    }

    public boolean isLinkerConnected(IPrismaLinker linker)
    {
        return this.getConnectedLinkers().contains(linker);
    }

    public void setConnectedLinkerPositions(List<BlockPos> connectedLinkerPositions)
    {
        this.connectedLinkerPositions = connectedLinkerPositions;
        this.updateConnectedLinkers();
    }

    public void setConnectedLinkers(List<IPrismaLinker> connectedLinkers)
    {
        this.connectedLinkers = connectedLinkers;
    }

    public void addConnectedLinkerPosition(BlockPos connectedLinkerPos)
    {
        this.getConnectedLinkerPositions().add(connectedLinkerPos);

        VamPrism.LOGGER.log(Level.INFO, "Add Connected Linker Position");

        TileEntity tileEntity = this.world.getTileEntity(connectedLinkerPos);
        if(tileEntity instanceof IPrismaLinker)
        {
            this.addConnectedLinker((IPrismaLinker) tileEntity);
        }
    }

    public void addConnectedLinker(IPrismaLinker linker)
    {
        this.getConnectedLinkers().add(linker);
    }

    public void removeConnectedLinkerPosition(BlockPos connectedLinkerPos)
    {
        int linkerIndex = this.getConnectedLinkerPositions().indexOf(connectedLinkerPos);
        this.getConnectedLinkerPositions().remove(connectedLinkerPos);
        this.removeConnectedLinker(linkerIndex);
    }

    public void removeConnectedLinker(IPrismaLinker connectedLinker)
    {
        this.getConnectedLinkers().remove(connectedLinker);
    }

    public void removeConnectedLinker(int index)
    {
        if(this.getConnectedLinkers().size() > index)
        {
            this.getConnectedLinkers().remove(index);
        }
    }

    public void updateConnectedLinkers()
    {
        List<IPrismaLinker> connectedLinkers = new ArrayList<>();

        for(BlockPos connectedLinkerPos : this.getConnectedLinkerPositions())
        {
            if(this.hasWorld())
            {
                TileEntity tileEntity = this.world.getTileEntity(connectedLinkerPos);
                if(tileEntity instanceof IPrismaLinker)
                {
                    connectedLinkers.add((IPrismaLinker) tileEntity);
                }
            }
        }

        this.setConnectedLinkers(connectedLinkers);
    }

    @Override
    public PrismaLinkerState getLinkerState()
    {
        return this.linkerState;
    }

    @Override
    public void setLinkerState(PrismaLinkerState state)
    {
        this.linkerState = state;
    }

    @Override
    public float getTransferRate()
    {
        return this.transferRate;
    }

    @Override
    public void setTransferRate(float transferRate)
    {
        this.transferRate = transferRate;
    }
}
