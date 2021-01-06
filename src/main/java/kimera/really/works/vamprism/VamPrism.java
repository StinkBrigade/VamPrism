package kimera.really.works.vamprism;

import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.events.EventHandler;
import kimera.really.works.vamprism.common.items.ItemRegistry;
import kimera.really.works.vamprism.common.network.PacketHandler;
import kimera.really.works.vamprism.common.tileentity.TileEntityRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

        EVENT_BUS.addListener(this::commonSetupEvent);

        ItemRegistry.ITEMS.register(EVENT_BUS);
        BlockRegistry.BLOCKS.register(EVENT_BUS);
        TileEntityRegistry.TILE_ENTITIES.register(EVENT_BUS);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    private void commonSetupEvent(FMLCommonSetupEvent event)
    {
        PacketHandler.init();
    }
}
