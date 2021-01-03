package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.client.renderer.tileentitty.SunlightPoolTileEntityRenderer;
import kimera.really.works.vamprism.common.blocks.SunlightPoolBlock;
import kimera.really.works.vamprism.common.util.PrismaStorage;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraftforge.common.extensions.IForgeTileEntity;

import javax.annotation.Nullable;

public class SunlightPoolTileEntity extends AbstractPrismaStoreTileEntity implements ITickableTileEntity
{
    private float prismaIncrement;

    private boolean enabled;
    private int ticksExisted;

    public SunlightPoolTileEntity()
    {
        super(TileEntityRegistry.SUNLIGHT_POOL.get(), 3, 1000.0F);

        this.prismaIncrement = 1.0F;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.prismaIncrement = parentNBTTagCompound.getFloat("prismaIncrement");

        this.enabled = parentNBTTagCompound.getBoolean("enabled");
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        parentNBTTagCompound.putFloat("prismaIncrement", this.prismaIncrement);

        parentNBTTagCompound.putBoolean("enabled", this.enabled);

        return parentNBTTagCompound;
    }

    @Override
    public void tick()
    {
        this.ticksExisted++;

        if(!this.hasWorld()) return;
        if(!this.world.isRemote)
        {
            boolean changedFlag = false;

            if(this.hasLight() && !this.isFull())
            {
                if(!this.enabled)
                {
                    this.enabled = true;
                    changedFlag = true;
                }
            }
            else if(this.enabled)
            {
                this.enabled = false;
                changedFlag = true;
            }

            if(changedFlag)
            {
                this.world.setBlockState(this.getPos(), this.getBlockState().with(SunlightPoolBlock.ENABLED, this.enabled), 3);

                this.markDirty();
            }
        }

        if(this.enabled)
        {
            collectPrisma();
        }
    }

    public void collectPrisma()
    {
        int currentLightLevel = this.getLightLevel();
        float lightProportion = ((float) currentLightLevel) / 15F;

        this.getInternalStorage().incrementAllCurrentValues(lightProportion * this.getPrismaIncrement());
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public boolean hasLight()
    {
        // If the light level is less than 15, then there's at least one block in the way.
        return this.world.isDaytime() && this.getLightLevel() >= 15;
    }

    public float getPrismaIncrement()
    {
        return this.prismaIncrement;
    }

    public int getTicksExisted()
    {
        return this.ticksExisted;
    }

    public int getLightLevel()
    {
        return this.world.getLightFor(LightType.SKY, this.pos) - this.world.getSkylightSubtracted();
    }

    public void setPrismaIncrement(float value)
    {
        this.prismaIncrement = value;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        BlockState state = this.getBlockState();
        BlockPos blockPos = this.getPos();
        if(state.get(SunlightPoolBlock.ENABLED) == true)
        {
            return IForgeTileEntity.INFINITE_EXTENT_AABB;
        }
        return super.getRenderBoundingBox();
    }

    @Override
    public int getTileEntityId()
    {
        return 514;
    }
}
