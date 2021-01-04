package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.blocks.PrismaticTeslaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class PrismaticTeslaTileEntity extends AbstractPrismaStorerTileEntity implements ITickableTileEntity
{
    private List<BlockPos> connectedTeslas;
    private List<BlockPos> connectedLinkers;

    private int ticksExisted;

    public PrismaticTeslaTileEntity()
    {
        super(TileEntityRegistry.PRISMATIC_TESLA.get(), 3, 100.0F);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.ticksExisted = parentNBTTagCompound.getInt("ticksExisted");

        this.connectedTeslas = this.readBlockPosListNBT(parentNBTTagCompound, "connectedTeslas");
        this.connectedLinkers = this.readBlockPosListNBT(parentNBTTagCompound, "connectedLinkers");
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        parentNBTTagCompound.putInt("ticksExisted", this.ticksExisted);

        this.writeBlockPosListNBT(parentNBTTagCompound, "connectedTeslas", this.connectedTeslas);
        this.writeBlockPosListNBT(parentNBTTagCompound, "connectedLinkers", this.connectedLinkers);

        return parentNBTTagCompound;
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

    public void placeTesla()
    {
        this.setConnectedTeslas(this.findTeslasInRange(6, 6));
        this.setConnectedLinkers(this.findAdjacentLinkers());

        for(BlockPos connectedTesla : this.connectedTeslas)
        {
            TileEntity tileEntity = world.getTileEntity(connectedTesla);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.addConnectedTesla(this.pos);
            }
        }
    }

    public void removeTesla()
    {
        for(BlockPos connectedTesla : this.connectedTeslas)
        {
            TileEntity tileEntity = world.getTileEntity(connectedTesla);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.removeConnectedTesla(this.pos);
            }
        }
    }

    public void neighbourUpdated(BlockPos pos)
    {
        boolean hasLinker = this.testAdjacentLinker(pos);
        boolean existingLinkerConnected = this.hasConnectedLinker(pos);

        if(hasLinker && !existingLinkerConnected)
        {
            this.addConnectedLinker(pos);
        }
        else if(!hasLinker && existingLinkerConnected)
        {
            this.removeConnectedLinker(pos);
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

    public List<BlockPos> getConnectedTeslas()
    {
        return this.connectedTeslas;
    }

    public boolean hasConnectedTesla(BlockPos pos)
    {
        return this.getConnectedTeslas().contains(pos);
    }

    public List<BlockPos> getConnectedLinkers()
    {
        return this.connectedLinkers;
    }

    public boolean hasConnectedLinker(BlockPos pos)
    {
        return this.getConnectedLinkers().contains(pos);
    }

    public void setConnectedTeslas(List<BlockPos> connectedTeslas)
    {
        this.connectedTeslas = connectedTeslas;
    }

    public void setConnectedLinkers(List<BlockPos> connectedLinkers)
    {
        this.connectedLinkers = connectedLinkers;
    }

    public void addConnectedTesla(BlockPos connectedTesla)
    {
        this.getConnectedTeslas().add(connectedTesla);
    }

    public void addConnectedLinker(BlockPos connectedLinker)
    {
        this.getConnectedLinkers().add(connectedLinker);
    }

    public void removeConnectedTesla(BlockPos connectedTesla)
    {
        this.getConnectedTeslas().remove(connectedTesla);
    }

    public void removeConnectedLinker(BlockPos connectedLinker)
    {
        this.getConnectedLinkers().remove(connectedLinker);
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
    public int getTileEntityId()
    {
        return 515;
    }
}
