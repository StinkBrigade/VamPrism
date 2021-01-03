package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.blocks.SunlightPoolBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraftforge.common.extensions.IForgeTileEntity;

public class SunlightPoolTileEntity extends AbstractPrismaStoreTileEntity implements ITickableTileEntity
{
    private float prismaIncrement;

    private boolean enabled;
    private int ticksExisted;

    private int alphaValue;

    public SunlightPoolTileEntity()
    {
        super(TileEntityRegistry.SUNLIGHT_POOL.get(), 3, 1000.0F);

        this.prismaIncrement = 2.5F;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.prismaIncrement = parentNBTTagCompound.getFloat("prismaIncrement");

        this.enabled = parentNBTTagCompound.getBoolean("enabled");
        this.ticksExisted = parentNBTTagCompound.getInt("ticksExisted");
        this.alphaValue = parentNBTTagCompound.getInt("alphaValue");
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        parentNBTTagCompound.putFloat("prismaIncrement", this.prismaIncrement);

        parentNBTTagCompound.putBoolean("enabled", this.enabled);
        parentNBTTagCompound.putInt("ticksExisted", this.ticksExisted);
        parentNBTTagCompound.putInt("alphaValue", this.alphaValue);

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

            if(alphaValue < 255)
            {
                alphaValue += 8;
                if(alphaValue > 255)
                {
                    alphaValue = 255;
                }
            }
        }
        else if(alphaValue > 0)
        {
            alphaValue -= 15;
            if(alphaValue < 0)
            {
                alphaValue = 0;
            }
        }
    }

    public void collectPrisma()
    {
        long timeFromMidday = this.world.getDayTime() - 6000L;
        int currentLightLevel = this.getLightLevel();
        float lightProportion = 1.0F - MathHelper.clamp(MathHelper.abs(timeFromMidday) / 6000F, 0.0F, 1.0F);

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

    public int getAlphaValue()
    {
        return this.alphaValue;
    }

    public float getAlphaProportion()
    {
        return ((float) this.getAlphaValue()) / 255.0F;
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
