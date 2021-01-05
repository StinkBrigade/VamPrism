package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.common.util.IPrismaStorer;

public interface ITeslaLinker extends IPrismaStorer
{
    TeslaLinkerState getLinkerState();
    void setLinkerState(TeslaLinkerState state);
}
