package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.common.items.ItemRegistry;
import kimera.really.works.vamprism.common.tileentity.SunlightPoolTileEntity;
import kimera.really.works.vamprism.common.util.INeedleInteractable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SunlightPoolBlock extends BreakableBlock implements INeedleInteractable
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D);

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public SunlightPoolBlock(Properties properties)
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
        SunlightPoolTileEntity sunlightPoolTileEntity = new SunlightPoolTileEntity();

        sunlightPoolTileEntity.setPrismaIncrement(1.0F);

        return sunlightPoolTileEntity;
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
    public void onNeedleInteract(BlockPos pos, ItemStack needle, World world, PlayerEntity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof SunlightPoolTileEntity)
        {
            SunlightPoolTileEntity sunlightPoolTileEntity = (SunlightPoolTileEntity) tileEntity;

            String message = "";

            boolean hasLight = sunlightPoolTileEntity.hasLight();
            message += "Has Light: " + hasLight;
            if(hasLight)
            {
                message += " (" + sunlightPoolTileEntity.getLightLevel() + ")";
            }
            message += ", Prisma R: " + sunlightPoolTileEntity.getCurrentPrismaValue(0);
            message += ", G: " + sunlightPoolTileEntity.getCurrentPrismaValue(1);
            message += ", B: " + sunlightPoolTileEntity.getCurrentPrismaValue(2);

            player.sendMessage(new StringTextComponent(message), Util.DUMMY_UUID);
        }
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
