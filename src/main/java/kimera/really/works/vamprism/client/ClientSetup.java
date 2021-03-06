package kimera.really.works.vamprism.client;

import kimera.really.works.vamprism.common.VamPrism;
import kimera.really.works.vamprism.client.renderer.tileentitty.PrismaStoreTileEntityRenderer;
import kimera.really.works.vamprism.client.renderer.tileentitty.PrismaticTeslaTileEntityRenderer;
import kimera.really.works.vamprism.client.renderer.tileentitty.SunlightPoolTileEntityRenderer;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.TileEntityRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = VamPrism.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientSetup
{
    @SubscribeEvent
    public static void clientSetupEvent(final FMLClientSetupEvent event)
    {
        //ScreenManager.registerFactory(ContainerRegistry.container.get(), Screen Class::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.SUNLIGHT_POOL.get(), SunlightPoolTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.PRISMATIC_TESLA.get(), PrismaticTeslaTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.PRISMA_STORE.get(), PrismaStoreTileEntityRenderer::new);

        event.enqueueWork(() ->
        {
            RenderTypeLookup.setRenderLayer(BlockRegistry.FANKRYSTAL_BLOCK.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockRegistry.SUNLIGHT_POOL.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockRegistry.PRISMA_STORE.get(), RenderType.getTranslucent());

            RenderTypeLookup.setRenderLayer(BlockRegistry.SUNLIGHT_BLOCK.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockRegistry.PRISMATIC_CUBE.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockRegistry.PRISMATIC_CUBE_IRON_CORNERS.get(), RenderType.getTranslucent());
        });
    }
}
