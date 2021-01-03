package kimera.really.works.vamprism.common.items;

import kimera.really.works.vamprism.common.util.INeedleInteractable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class FankrystalNeedleItem extends Item
{
    public FankrystalNeedleItem(Properties properties)
    {
        super(properties);
    }
}
