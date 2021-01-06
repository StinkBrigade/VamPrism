package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.common.items.ItemRegistry;
import kimera.really.works.vamprism.common.util.INeedleInteractable;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractNeedleInteractableBlock extends BreakableBlock implements INeedleInteractable
{
    public AbstractNeedleInteractableBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack mainhandItem = player.getHeldItemMainhand();
        if(!world.isRemote && mainhandItem.getItem() == ItemRegistry.FANKRYSTAL_NEEDLE.get() && handIn == Hand.MAIN_HAND)
        {
            onNeedleInteract(pos, mainhandItem, world,player);
            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }
}
