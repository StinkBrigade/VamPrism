package kimera.really.works.vamprism.common.tileentity;

public enum TeslaLinkerState
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

    TeslaLinkerState(String stateId, boolean acceptsInput, boolean acceptsOutput)
    {
        this.stateId = stateId;
        this.canTakeInput = acceptsInput;
        this.canGiveOutput = acceptsOutput;
    }
}
