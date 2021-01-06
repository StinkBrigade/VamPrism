package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.blocks.PrismaticTeslaBlock;
import kimera.really.works.vamprism.common.util.CommonUtil;
import kimera.really.works.vamprism.common.util.IPrismaStorer;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class PrismaticTeslaTileEntity extends AbstractPrismaStorerTileEntity implements IPrismaLinker
{
    private TeslaLinkerState linkerState;

    private List<BlockPos> connectedTeslaPositions;
    private List<PrismaticTeslaTileEntity> connectedTeslas;

    private float transferRate;
    private float lossRate;

    private int ticksExisted;

    public PrismaticTeslaTileEntity()
    {
        super(TileEntityRegistry.PRISMATIC_TESLA.get(), 3, 150.0F);

        this.transferRate = 15.0F;
        this.lossRate = 0.1F;

        this.linkerState = TeslaLinkerState.CHANNELING;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.ticksExisted = parentNBTTagCompound.getInt("ticksExisted");

        this.transferRate = parentNBTTagCompound.getFloat("transferRate");
        this.lossRate = parentNBTTagCompound.getFloat("lossRate");

        this.setConnectedTeslaPositions(CommonUtil.readBlockPosListNBT(parentNBTTagCompound, "connectedTeslas"));
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        parentNBTTagCompound.putInt("ticksExisted", this.ticksExisted);

        parentNBTTagCompound.putFloat("transferRate", this.transferRate);
        parentNBTTagCompound.putFloat("lossRate", this.lossRate);

        CommonUtil.writeBlockPosListNBT(parentNBTTagCompound, "connectedTeslas", this.getConnectedTeslaPositions());

        return parentNBTTagCompound;
    }

    @Override
    public void tick()
    {
        ticksExisted++;

        if(!world.isRemote)
        {
            this.processConnectedTeslas();
        }

        super.tick();
    }

    private void processConnectedTeslas()
    {
        for(PrismaticTeslaTileEntity teslaTileEntity : this.getConnectedTeslas())
        {
            // How the fuck do I do Tesla logic
        }
    }

    @Override
    public void onPlace()
    {
        this.setConnectedTeslaPositions(this.findTeslasInRange(6, 6));

        for(BlockPos connectedTesla : this.getConnectedTeslaPositions())
        {
            TileEntity tileEntity = world.getTileEntity(connectedTesla);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.addConnectedTeslaPosition(this.pos);
            }
        }
    }

    @Override
    public void onRemove()
    {
        for(BlockPos connectedTesla : this.getConnectedTeslaPositions())
        {
            TileEntity tileEntity = world.getTileEntity(connectedTesla);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.removeConnectedTeslaPosition(this.pos);
            }
        }
    }

    @Override
    public void onNeighbourUpdated(BlockPos neighbourPos)
    {

    }

    private List<BlockPos> findTeslasInRange(int xRange, int zRange)
    {
        List<BlockPos> teslaPositions = new ArrayList<>();

        this.findTeslasInDirection(xRange, 1, 0, teslaPositions);
        this.findTeslasInDirection(zRange, 0, 1, teslaPositions);

        return teslaPositions;
    }

    private void findTeslasInDirection(int range, int x, int z, List<BlockPos> teslaPositions)
    {
        if(range < 1) return;

        boolean positiveBlocked = false;
        boolean negativeBlocked = false;

        for(int i = 1; i <= range; i++)
        {
            int currentX = i * x;
            int currentZ = i * z;

            if(!positiveBlocked)
            {
                BlockPos targetPos = new BlockPos(this.pos.getX() + currentX, this.pos.getY(), this.pos.getZ() + currentZ);
                int targetStatus = this.checkBlockPosForTesla(targetPos);

                if(targetStatus == 1)
                {
                    positiveBlocked = true;
                }
                else if(targetStatus == 2)
                {
                    teslaPositions.add(targetPos);
                }
            }

            if(!negativeBlocked)
            {
                BlockPos targetPos = new BlockPos(this.pos.getX() - currentX, this.pos.getY(), this.pos.getZ() - currentZ);
                int targetStatus = this.checkBlockPosForTesla(targetPos);

                if(targetStatus == 1)
                {
                    negativeBlocked = true;
                }
                else if(targetStatus == 2)
                {
                    teslaPositions.add(targetPos);
                }
            }

            if(positiveBlocked && negativeBlocked)
            {
                break;
            }
        }
    }

    private int checkBlockPosForTesla(BlockPos targetPos)
    {
        BlockPos lineOfSightPos = targetPos.add(0, 2, 0);
        BlockState lineOfSightState = this.world.getBlockState(lineOfSightPos);

        if(lineOfSightState.getBlock().isAir(lineOfSightState, this.world, lineOfSightPos))
        {
            BlockState targetState = this.world.getBlockState(targetPos);

            if(targetState.getBlock() == BlockRegistry.PRISMATIC_TESLA.get())
            {
                return 2;
            }
        }
        else
        {
            return 1;
        }

        return 0;
    }

    public int getTicksExisted()
    {
        return this.ticksExisted;
    }

    public List<BlockPos> getConnectedTeslaPositions()
    {
        if(this.connectedTeslaPositions == null)
        {
            this.setConnectedTeslaPositions(new ArrayList<>());
        }
        return this.connectedTeslaPositions;
    }

    public List<PrismaticTeslaTileEntity> getConnectedTeslas()
    {
        if(this.connectedTeslas == null || this.connectedTeslas.size() != this.getConnectedTeslaPositions().size())
        {
            this.updateConnectedTeslas();
        }
        return this.connectedTeslas;
    }

    public boolean hasConnectedTeslaAt(BlockPos pos)
    {
        return this.getConnectedTeslaPositions().contains(pos);
    }

    public boolean isTeslaConnected(PrismaticTeslaTileEntity teslaTileEntity)
    {
        return this.getConnectedTeslas().contains(teslaTileEntity);
    }

    public void setConnectedTeslaPositions(List<BlockPos> connectedTeslaPositions)
    {
        this.connectedTeslaPositions = connectedTeslaPositions;
        this.updateConnectedTeslas();
    }

    public void setConnectedTeslas(List<PrismaticTeslaTileEntity> connectedTeslas)
    {
        this.connectedTeslas = connectedTeslas;
    }

    public void addConnectedTeslaPosition(BlockPos connectedTeslaPos)
    {
        this.getConnectedTeslaPositions().add(connectedTeslaPos);

        TileEntity tileEntity = this.world.getTileEntity(connectedTeslaPos);
        if(tileEntity instanceof PrismaticTeslaTileEntity)
        {
            this.addConnectedTesla((PrismaticTeslaTileEntity) tileEntity);
        }
    }

    public void addConnectedTesla(PrismaticTeslaTileEntity connectedTesla)
    {
        this.getConnectedTeslas().add(connectedTesla);
    }

    public void removeConnectedTeslaPosition(BlockPos connectedTeslaPos)
    {
        int teslaIndex = this.getConnectedTeslaPositions().indexOf(connectedTeslaPos);
        this.getConnectedTeslaPositions().remove(connectedTeslaPos);
        this.removeConnectedTesla(teslaIndex);
    }

    public void removeConnectedTesla(PrismaticTeslaTileEntity connectedTesla)
    {
        this.getConnectedTeslas().remove(connectedTesla);
    }

    public void removeConnectedTesla(int index)
    {
        if(this.getConnectedTeslas().size() > index)
        {
            this.getConnectedTeslas().remove(index);
        }
    }

    public void updateConnectedTeslas()
    {
        List<PrismaticTeslaTileEntity> connectedTeslas = new ArrayList<>();

        for(BlockPos connectedTeslaPos : this.getConnectedTeslaPositions())
        {
            if(this.hasWorld())
            {
                TileEntity tileEntity = this.world.getTileEntity(connectedTeslaPos);
                if(tileEntity instanceof PrismaticTeslaTileEntity)
                {
                    connectedTeslas.add((PrismaticTeslaTileEntity) tileEntity);
                }
            }
        }

        this.setConnectedTeslas(connectedTeslas);
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
    public float getTransferRate()
    {
        return this.transferRate;
    }

    @Override
    public void setTransferRate(float transferRate)
    {
        this.transferRate = transferRate;
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

    @Override
    public int getTileEntityId()
    {
        return 515;
    }
}
