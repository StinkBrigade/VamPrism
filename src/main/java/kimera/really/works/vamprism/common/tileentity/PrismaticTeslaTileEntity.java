package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.blocks.PrismaticTeslaBlock;
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

public class PrismaticTeslaTileEntity extends AbstractPrismaStorerTileEntity
{
    private List<BlockPos> connectedTeslaPositions;
    private List<BlockPos> connectedLinkerPositions;

    private List<PrismaticTeslaTileEntity> connectedTeslas;
    private List<ITeslaLinker> connectedLinkers;

    private float transferRate;
    private float lossRate;

    private int ticksExisted;

    public PrismaticTeslaTileEntity()
    {
        super(TileEntityRegistry.PRISMATIC_TESLA.get(), 3, 150.0F);

        this.transferRate = 15.0F;
        this.lossRate = 0.1F;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.ticksExisted = parentNBTTagCompound.getInt("ticksExisted");

        this.transferRate = parentNBTTagCompound.getFloat("transferRate");
        this.lossRate = parentNBTTagCompound.getFloat("lossRate");

        this.setConnectedTeslaPositions(this.readBlockPosListNBT(parentNBTTagCompound, "connectedTeslas"));
        this.setConnectedLinkerPositions(this.readBlockPosListNBT(parentNBTTagCompound, "connectedLinkers"));
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        parentNBTTagCompound.putInt("ticksExisted", this.ticksExisted);

        parentNBTTagCompound.putFloat("transferRate", this.transferRate);
        parentNBTTagCompound.putFloat("lossRate", this.lossRate);

        this.writeBlockPosListNBT(parentNBTTagCompound, "connectedTeslas", this.getConnectedTeslaPositions());
        this.writeBlockPosListNBT(parentNBTTagCompound, "connectedLinkers", this.getConnectedLinkerPositions());

        return parentNBTTagCompound;
    }

    @Override
    public void tick()
    {
        ticksExisted++;

        if(!world.isRemote)
        {
            processConnectedTeslas();
            processConnectedLinkers();
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

    private void processConnectedLinkers()
    {
        for(ITeslaLinker linker : this.getConnectedLinkers())
        {
            if(linker.getLinkerState().canGiveOutput)
            {
                IPrismaStorer.handlePrismaTransfer(linker, this, 5.0F, 0.0F);
            }
            else if(linker.getLinkerState().canTakeInput)
            {
                IPrismaStorer.handlePrismaTransfer(this, linker, 5.0F, 0.0F);
            }
        }
    }

    public void placeTesla()
    {
        this.setConnectedTeslaPositions(this.findTeslasInRange(6, 6));
        this.setConnectedLinkerPositions(this.findAdjacentLinkers());

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

    public void removeTesla()
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

    public void neighbourUpdated(BlockPos pos)
    {
        boolean hasLinker = this.testAdjacentLinker(pos);
        boolean existingLinkerConnected = this.hasConnectedLinkerAt(pos);

        if(hasLinker && !existingLinkerConnected)
        {
            this.addConnectedLinkerPosition(pos);
        }
        else if(!hasLinker && existingLinkerConnected)
        {
            this.removeConnectedLinkerPosition(pos);
        }
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
        if(targetTileEntity != null && targetTileEntity instanceof ITeslaLinker)
        {
            return true;
        }

        return false;
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

    public float getTransferRate()
    {
        return this.transferRate;
    }

    public void setTransferRate(float transferRate)
    {
        this.transferRate = transferRate;
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

    public List<BlockPos> getConnectedLinkerPositions()
    {
        if(this.connectedLinkerPositions == null)
        {
            this.setConnectedLinkerPositions(new ArrayList<>());
        }
        return this.connectedLinkerPositions;
    }

    public List<ITeslaLinker> getConnectedLinkers()
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

    public boolean isLinkerConnected(ITeslaLinker linker)
    {
        return this.getConnectedLinkers().contains(linker);
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

    public void setConnectedLinkerPositions(List<BlockPos> connectedLinkerPositions)
    {
        this.connectedLinkerPositions = connectedLinkerPositions;
        this.updateConnectedLinkers();
    }

    public void setConnectedLinkers(List<ITeslaLinker> connectedLinkers)
    {
        this.connectedLinkers = connectedLinkers;
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

    public void addConnectedLinkerPosition(BlockPos connectedLinkerPos)
    {
        this.getConnectedLinkerPositions().add(connectedLinkerPos);

        VamPrism.LOGGER.log(Level.INFO, "Add Connected Linker Position");

        TileEntity tileEntity = this.world.getTileEntity(connectedLinkerPos);
        VamPrism.LOGGER.log(Level.INFO, "Tile Entity: " + tileEntity.toString());
        if(tileEntity instanceof ITeslaLinker)
        {
            this.addConnectedLinker((ITeslaLinker) tileEntity);
        }
    }

    public void addConnectedLinker(ITeslaLinker linker)
    {
        this.getConnectedLinkers().add(linker);
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

    public void removeConnectedLinkerPosition(BlockPos connectedLinkerPos)
    {
        int linkerIndex = this.getConnectedLinkerPositions().indexOf(connectedLinkerPos);
        this.getConnectedLinkerPositions().remove(connectedLinkerPos);
        this.removeConnectedLinker(linkerIndex);
    }

    public void removeConnectedLinker(ITeslaLinker connectedLinker)
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

    public void updateConnectedLinkers()
    {
        List<ITeslaLinker> connectedLinkers = new ArrayList<>();

        for(BlockPos connectedLinkerPos : this.getConnectedLinkerPositions())
        {
            if(this.hasWorld())
            {
                TileEntity tileEntity = this.world.getTileEntity(connectedLinkerPos);
                if(tileEntity instanceof ITeslaLinker)
                {
                    connectedLinkers.add((ITeslaLinker) tileEntity);
                }
            }
        }

        this.setConnectedLinkers(connectedLinkers);
    }

    private List<BlockPos> readBlockPosListNBT(CompoundNBT parentNBTTagCompound, String key)
    {
        List<BlockPos> returnList = new ArrayList<>();

        INBT returnListINBT = parentNBTTagCompound.get(key);
        if(returnListINBT instanceof ListNBT)
        {
            ListNBT returnListNBT = (ListNBT) returnListINBT;

            for(int i = 0; i < returnListNBT.size(); i++)
            {
                INBT listEntryINBT = returnListNBT.get(i);
                if(listEntryINBT instanceof CompoundNBT)
                {
                    returnList.add(NBTUtil.readBlockPos((CompoundNBT) listEntryINBT));
                }
            }
        }

        return returnList;
    }

    private void writeBlockPosListNBT(CompoundNBT parentNBTTagCompound, String key, List<BlockPos> list)
    {
        ListNBT listNBT = new ListNBT();
        for(int i = 0; i < list.size(); i++)
        {
            listNBT.add(NBTUtil.writeBlockPos(list.get(i)));
        }
        parentNBTTagCompound.put(key, listNBT);
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
