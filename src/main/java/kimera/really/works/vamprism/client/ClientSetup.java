package kimera.really.works.vamprism.client;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.client.renderer.tileentitty.SunlightPoolTileEntityRenderer;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.TileEntityRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = VamPrism.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    @SubscribeEvent
    public static void clientSetupEvent(final FMLClientSetupEvent event)
    {
        //ScreenManager.registerFactory(ContainerRegistry.container.get(), Screen Class::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.SUNLIGHT_POOL.get(), SunlightPoolTileEntityRenderer::new);

        event.enqueueWork(() ->
        {
            RenderTypeLookup.setRenderLayer(BlockRegistry.FANKRYSTAL_BLOCK.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockRegistry.SUNLIGHT_POOL.get(), RenderType.getTranslucent());

            RenderTypeLookup.setRenderLayer(BlockRegistry.SUNLIGHT_BLOCK.get(), RenderType.getTranslucent());
        });
    }
}
