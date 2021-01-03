package kimera.really.works.vamprism.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INeedleInteractable
{
    void onNeedleInteract(BlockPos pos, ItemStack needle, World world, PlayerEntity player);
}
