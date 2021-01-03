package kimera.really.works.vamprism.common.util;

public interface IPrismaStore
{
    PrismaStorage getInternalStorage();

    boolean isEmpty();
    boolean isFull();

    float getValueFillRatio(int index);
    float getTotalFillRatio();

    float getCurrentPrismaValue(int index);
    float getMaxPrismaValue(int index);

    void setCurrentPrismaValue(int index, float value);
    void setMaxPrismaValue(int index, float value);
}
