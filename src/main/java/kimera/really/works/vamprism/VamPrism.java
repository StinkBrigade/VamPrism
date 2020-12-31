package kimera.really.works.vamprism;

import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.items.ItemRegistry;
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

        ItemRegistry.ITEMS.register(EVENT_BUS);
        BlockRegistry.BLOCKS.register(EVENT_BUS);
    }
}
