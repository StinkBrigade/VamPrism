package kimera.really.works.vamprism.client.renderer.tileentitty;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.SunlightPoolTileEntity;
import kimera.really.works.vamprism.common.util.ClientUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SunlightPoolTileEntityRenderer extends TileEntityRenderer<SunlightPoolTileEntity>
{
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(VamPrism.MODID, "textures/entity/light_beam.png");
    public static final ResourceLocation BEAM2_TEXTURE = new ResourceLocation(VamPrism.MODID, "textures/entity/light_flow.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.getEntityTranslucent(BEAM_TEXTURE);
    private static final RenderType BEAM2_RENDER_TYPE = RenderType.getEntityTranslucent(BEAM2_TEXTURE);

    private static final int BEAM_TEXTURE_FRAMES = 64;

    private static final int BEAM_SEGMENTS = 16;

    public SunlightPoolTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if(tileEntity.getAlphaValue() > 0)
        {
            renderBeams(tileEntity, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        renderLight(tileEntity, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderBeams(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        float rotation = tileEntity.getTicksExisted() + partialTicks;

        float scaleProportion = tileEntity.getAlphaProportion();

        IVertexBuilder beamVertexBuilder = bufferIn.getBuffer(BEAM_RENDER_TYPE);
        renderBeam(beamVertexBuilder, matrixStack, scaleProportion * 0.3F, 0.6F, scaleProportion * 0.3F, 255, 255, 255, tileEntity.getAlphaValue(), tileEntity.getTicksExisted(), rotation);

        beamVertexBuilder = bufferIn.getBuffer(BEAM2_RENDER_TYPE);
        renderBeam(beamVertexBuilder, matrixStack, scaleProportion * 0.35F, 0.6F, scaleProportion * 0.35F, 255, 255, 255, tileEntity.getAlphaValue() / 2, tileEntity.getTicksExisted(), rotation * 1.5F);
    }

    private void renderBeam(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float width, float segmentHeight, float depth, int red, int green, int blue, int alpha, int currentFrame, float rotation)
    {
        matrixStack.push();

        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation % 360));

        ClientUtil.drawBeam(vertexBuilder, matrixStack, 0.0F, 0.0F, 0.0F, width, depth, BEAM_SEGMENTS, segmentHeight, currentFrame, BEAM_TEXTURE_FRAMES, red, green, blue, alpha, true);

        matrixStack.pop();
    }

    private void renderLight(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos();
        BlockState blockState = BlockRegistry.SUNLIGHT_BLOCK.get().getDefaultState();

        float scalePercentage = tileEntity.getTotalFillRatio();
        scalePercentage = MathHelper.clamp(scalePercentage, 0.001F, 1F);

        matrixStack.push();
        matrixStack.translate(0.25D, (scalePercentage * 0.25D) + 0.5D, 0.25D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(0.0F));
        matrixStack.scale(0.5F, scalePercentage * 0.5F, 0.5F);

        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        World world = tileEntity.getWorld();
        blockRendererDispatcher.getBlockModelRenderer().renderModel(
                world,
                blockRendererDispatcher.getModelForState(blockState),
                blockState,
                blockPos,
                matrixStack,
                bufferIn.getBuffer(RenderTypeLookup.func_239221_b_(blockState)),
                false,
                world.rand,
                blockState.getPositionRandom(blockPos),
                OverlayTexture.NO_OVERLAY,
                EmptyModelData.INSTANCE);

        matrixStack.pop();
    }
}
