package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.common.tileentity.PrismaStoreTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PrismaStoreBlock extends AbstractPrismaLinkerBlock
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D);

    public PrismaStoreBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        PrismaStoreTileEntity prismaStoreTileEntity = new PrismaStoreTileEntity();

        return prismaStoreTileEntity;
    }

    @Override
    public void onNeedleInteract(BlockPos pos, ItemStack needle, World world, PlayerEntity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof PrismaStoreTileEntity)
        {
            PrismaStoreTileEntity prismaStoreTileEntity = (PrismaStoreTileEntity) tileEntity;

            String message = ", Prisma R: " + prismaStoreTileEntity.getCurrentPrismaValue(0);
            message += ", G: " + prismaStoreTileEntity.getCurrentPrismaValue(1);
            message += ", B: " + prismaStoreTileEntity.getCurrentPrismaValue(2);

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
