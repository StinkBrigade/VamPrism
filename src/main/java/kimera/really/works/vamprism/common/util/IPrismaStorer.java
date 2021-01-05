package kimera.really.works.vamprism.common.util;

public interface IPrismaStorer
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

    boolean transferPrismaTo(IPrismaStorer target, int index, float amount, float loss);

    static boolean handlePrismaTransfer(IPrismaStorer from, IPrismaStorer to, float amount, float loss)
    {
        boolean transferSuccessful = false;
        for(int i = 0; i < 3; i++)
        {
            if(from.transferPrismaTo(to, i, amount, loss))
            {
                transferSuccessful = true;
            }
        }
        return transferSuccessful;
    }
}
