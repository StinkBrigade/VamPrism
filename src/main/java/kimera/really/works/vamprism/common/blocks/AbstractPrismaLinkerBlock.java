package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.common.tileentity.IPrismaLinker;
import kimera.really.works.vamprism.common.tileentity.PrismaticTeslaTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractPrismaLinkerBlock extends AbstractNeedleInteractableBlock
{
    public AbstractPrismaLinkerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof IPrismaLinker)
            {
                IPrismaLinker linkerTileEntity = (IPrismaLinker) tileEntity;
                linkerTileEntity.onPlace();
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof IPrismaLinker)
            {
                IPrismaLinker linkerTileEntity = (IPrismaLinker) tileEntity;
                linkerTileEntity.onRemove();
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor, boolean isMoving)
    {
        if(!world.isRemote())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof IPrismaLinker)
            {
                IPrismaLinker linkerTileEntity = (IPrismaLinker) tileEntity;
                linkerTileEntity.onNeighbourUpdated(neighbor);
            }
        }
        super.neighborChanged(state, world, pos, blockIn, neighbor, isMoving);
    }
}
