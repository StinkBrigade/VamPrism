package kimera.really.works.vamprism.client.renderer.tileentitty;

import com.mojang.blaze3d.matrix.MatrixStack;
import kimera.really.works.vamprism.client.util.ClientUtil;
import kimera.really.works.vamprism.common.tileentity.PrismaStoreTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class PrismaStoreTileEntityRenderer extends TileEntityRenderer<PrismaStoreTileEntity>
{
    public PrismaStoreTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PrismaStoreTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        ClientUtil.renderSunlightCube(tileEntity, 0.25D, 0.5D, 0.75D, 0.25D, 0.5F, 0.5F, 0.5F, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
