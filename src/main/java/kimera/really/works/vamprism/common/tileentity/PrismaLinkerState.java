package kimera.really.works.vamprism.common.tileentity;

public enum PrismaLinkerState
{
    OFF("off", false, false),
    PRODUCING("producing", false, true),
    CONSUMING("consuming", true, false),
    STORING("storing", true, false),
    RELEASING("releasing", false, true),
    CHANNELING("channeling", true, true);

    public String stateId;

    public boolean canTakeInput;
    public boolean canGiveOutput;

    PrismaLinkerState(String stateId, boolean canTakeInput, boolean canGiveOutput)
    {
        this.stateId = stateId;
        this.canTakeInput = canTakeInput;
        this.canGiveOutput = canGiveOutput;
    }
}
