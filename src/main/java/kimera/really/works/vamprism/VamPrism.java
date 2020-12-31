package kimera.really.works.vamprism;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VamPrism.MODID)
public class VamPrism
{
    public static final String MODID = "vamprism";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IEventBus EVENT_BUS;

    public VamPrism()
    {
        EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
