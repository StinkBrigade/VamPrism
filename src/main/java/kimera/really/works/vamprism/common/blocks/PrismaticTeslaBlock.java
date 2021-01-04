package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.tileentity.PrismaticTeslaTileEntity;
import kimera.really.works.vamprism.common.tileentity.SunlightPoolTileEntity;
import kimera.really.works.vamprism.common.util.PrismaStorage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticTeslaBlock extends Block
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public PrismaticTeslaBlock(AbstractBlock.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ENABLED, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        PrismaticTeslaTileEntity prismaticTeslaTileEntity = new PrismaticTeslaTileEntity();

        return prismaticTeslaTileEntity;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!world.isRemote && handIn == Hand.MAIN_HAND)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;

                VamPrism.LOGGER.log(Level.INFO, "Connected Teslas: " + teslaTileEntity.getConnectedTeslas().size());
                for(int i = 0; i < teslaTileEntity.getConnectedTeslas().size(); i++)
                {
                    VamPrism.LOGGER.log(Level.INFO, i + ": " + teslaTileEntity.getConnectedTeslas().get(i).toString());
                }

                VamPrism.LOGGER.log(Level.INFO, "Connected Linkers: " + teslaTileEntity.getConnectedLinkers().size());
                for(int i = 0; i < teslaTileEntity.getConnectedLinkers().size(); i++)
                {
                    VamPrism.LOGGER.log(Level.INFO, i + ": " + teslaTileEntity.getConnectedLinkers().get(i).toString());
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.placeTesla();
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.removeTesla();
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        if(!world.isRemote())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof PrismaticTeslaTileEntity)
            {
                PrismaticTeslaTileEntity teslaTileEntity = (PrismaticTeslaTileEntity) tileEntity;
                teslaTileEntity.neighbourUpdated(neighbor);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(ENABLED, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
        builder.add(ENABLED);
    }

    @Override
    public boolean isTransparent(BlockState state)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }
}
