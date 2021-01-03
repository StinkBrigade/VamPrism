package kimera.really.works.vamprism.common.util;

import com.mojang.datafixers.optics.Prism;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

public class PrismaStorage
{
    private final int valueCount;
    private final PrismaValue[] prismaValues;

    public PrismaStorage(int valueCount)
    {
        this.valueCount = valueCount;
        this.prismaValues = new PrismaValue[valueCount];

        for(int i = 0; i < valueCount; i++)
        {
            prismaValues[i] = new PrismaValue(i);
        }
    }

    public PrismaStorage(int valueCount, float maxValue)
    {
        this(valueCount);

        this.setAllMaxValues(maxValue);
    }

    public void readNBT(CompoundNBT parentNBTCompound)
    {
        for(PrismaValue prismaValue : this.prismaValues)
        {
            prismaValue.readNBT(parentNBTCompound);
        }
    }

    public void writeNBT(CompoundNBT parentNBTCompound)
    {
        for(PrismaValue prismaValue : this.prismaValues)
        {
            prismaValue.writeNBT(parentNBTCompound);
        }
    }

    public boolean isEmpty()
    {
        boolean empty = true;

        for(PrismaValue prismaValue : this.prismaValues)
        {
            if(!prismaValue.isEmpty())
            {
                empty = false;
                break;
            }
        }

        return empty;
    }

    public boolean isFull()
    {
        boolean full = true;

        for(PrismaValue prismaValue : this.prismaValues)
        {
            if(!prismaValue.isFull())
            {
                full = false;
                break;
            }
        }

        return full;
    }

    public float getValueFillRatio(int index)
    {
        return this.prismaValues[index].getFillRatio();
    }

    public float getTotalFillRatio()
    {
        float accumulatedRatio = 0.0F;
        float maxAccumulatedRatio = 0.0F;

        for(int i = 0; i < this.valueCount; i++)
        {
            accumulatedRatio += this.getValueFillRatio(i);
            maxAccumulatedRatio += 1.0F;
        }

        float ratio = accumulatedRatio / maxAccumulatedRatio;
        return MathHelper.clamp(ratio, 0.0F, 1.0F);
    }

    public float getCurrentValue(int index)
    {
        return this.prismaValues[index].getCurrentValue();
    }

    public float getMaxValue(int index)
    {
        return this.prismaValues[index].getMaxValue();
    }

    public void setCurrentValue(int index, float value)
    {
        this.prismaValues[index].setCurrentValue(value);
    }

    public void setMaxValue(int index, float value)
    {
        this.prismaValues[index].setMaxValue(value);
    }

    public void setAllCurrentValues(float value)
    {
        for(PrismaValue prismaValue : this.prismaValues)
        {
            prismaValue.setCurrentValue(value);
        }
    }

    public void setAllMaxValues(float value)
    {
        for(PrismaValue prismaValue : this.prismaValues)
        {
            prismaValue.setMaxValue(value);
        }
    }

    public void incrementCurrentValue(int index, float amount)
    {
        this.setCurrentValue(index, this.getCurrentValue(index) + amount);
    }

    public void incrementAllCurrentValues(float amount)
    {
        for(PrismaValue prismaValue : this.prismaValues)
        {
            prismaValue.setCurrentValue(prismaValue.getCurrentValue() + amount);
        }
    }

    public int getValueCount()
    {
        return this.valueCount;
    }

    public class PrismaValue
    {
        private final int id;

        public float currentValue;
        public float maxValue;

        public PrismaValue(int id)
        {
            this.id = id;
        }

        public void readNBT(CompoundNBT parentNBTCompound)
        {
            this.setMaxValue(parentNBTCompound.getFloat("maxPrismaValue_" + id));
            this.setCurrentValue(parentNBTCompound.getFloat("currentPrismaValue_" + id));
        }

        public void writeNBT(CompoundNBT parentNBTCompound)
        {
            parentNBTCompound.putFloat("maxPrismaValue_" + id, this.getMaxValue());
            parentNBTCompound.putFloat("currentPrismaValue_" + id, this.getCurrentValue());
        }

        public boolean isEmpty()
        {
            return !(this.getCurrentValue() > 0.0F);
        }

        public boolean isFull()
        {
            return !(this.getCurrentValue() < this.getMaxValue());
        }

        public float getFillRatio()
        {
            float ratio = this.currentValue / this.maxValue;
            return MathHelper.clamp(ratio, 0.0F, 1.0F);
        }

        public float getCurrentValue()
        {
            return this.currentValue;
        }

        public float getMaxValue()
        {
            return this.maxValue;
        }

        public void setCurrentValue(float value)
        {
            this.currentValue = value;
            this.clampCurrentValue();
        }

        public void setMaxValue(float value)
        {
            this.maxValue = value;
            this.clampCurrentValue();
        }

        public void clampCurrentValue()
        {
            this.currentValue = MathHelper.clamp(this.currentValue, 0, this.getMaxValue());
        }
    }
}
