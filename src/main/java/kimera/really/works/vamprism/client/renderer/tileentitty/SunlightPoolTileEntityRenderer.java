package kimera.really.works.vamprism.client.renderer.tileentitty;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.SunlightPoolTileEntity;
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

    private static final int BEAM_TEXTURE_WIDTH = 8;
    private static final int BEAM_TEXTURE_HEIGHT = 1024;

    private static final int BEAM_FRAME_WIDTH = 8;
    private static final int BEAM_FRAME_HEIGHT = 16;

    private static final int BEAM_SEGMENTS = 32;

    private static final float BEAM_WIDTH_PROPORTION = ((float) BEAM_FRAME_WIDTH) / ((float) BEAM_TEXTURE_WIDTH);
    private static final float BEAM_HEIGHT_PROPORTION = ((float) BEAM_FRAME_HEIGHT) / ((float) BEAM_TEXTURE_HEIGHT);

    public SunlightPoolTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if(tileEntity.isEnabled())
        {
            renderBeams(tileEntity, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        renderLight(tileEntity, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderBeams(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos pos = tileEntity.getPos();

        float sourceX = (float) pos.getX() + 0.5F;
        float sourceY = (float) pos.getY() + 0.5F;
        float sourceZ = (float) pos.getZ() + 0.5F;

        float distanceX = 0.0F;
        float distanceY = 1.0F;
        float distanceZ = 0.0F;

        float rotation = tileEntity.getTicksExisted() + partialTicks;

        IVertexBuilder beamVertexBuilder = bufferIn.getBuffer(BEAM_RENDER_TYPE);
        this.renderBeam(tileEntity, partialTicks, matrixStack, beamVertexBuilder, BEAM_SEGMENTS, 0.5F, 1.0F, 0.5F, (rotation / 2.0F) % 360, combinedLightIn);
        this.renderBeam(tileEntity, partialTicks, matrixStack, beamVertexBuilder, BEAM_SEGMENTS, 0.5F, 1.0F, 0.5F, ((rotation / 2.0f) % 360) + 90F, combinedLightIn);

        IVertexBuilder beam2VertexBuilder = bufferIn.getBuffer(BEAM2_RENDER_TYPE);
        this.renderBeam(tileEntity, partialTicks, matrixStack, beam2VertexBuilder, BEAM_SEGMENTS, 0.5F, 1.0F, 0.5F, (rotation % 360) + 45F, combinedLightIn);
        this.renderBeam(tileEntity, partialTicks, matrixStack, beam2VertexBuilder, BEAM_SEGMENTS, 0.5F, 1.0F, 0.5F, (rotation % 360) + 135F, combinedLightIn);
    }

    private void renderBeam(SunlightPoolTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IVertexBuilder beamVertexBuilder, int beamSegments, float widthX, float widthY, float widthZ, float rotation, int combinedLightIn)
    {
        matrixStack.push();

        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation));

        MatrixStack.Entry matrixStackEntry = matrixStack.getLast();
        Matrix3f matrix3f = matrixStackEntry.getNormal();
        Matrix4f matrix4f = matrixStackEntry.getMatrix();

        float xStart = widthX / 2.0F;

        for(int beamSegment = 0; beamSegment < beamSegments; beamSegment++)
        {
            float beamProportion = ((float)beamSegment) / ((float)beamSegments);

            int alpha = 255 - (int)(255.0F * beamProportion);

            int currentFrame = (tileEntity.getTicksExisted() + (beamSegments - beamSegment - 1)) % BEAM_TEXTURE_FRAMES;

            float uvX0 = 0;
            float uvX1 = 1;
            float uvY0 = BEAM_HEIGHT_PROPORTION * currentFrame;
            float uvY1 = BEAM_HEIGHT_PROPORTION * (currentFrame + 1);

            beamVertexBuilder.pos(matrix4f, xStart, (widthY * beamSegment) + widthY, 0.0F).color(255, 255, 255, alpha).tex(uvX1, uvY0).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            beamVertexBuilder.pos(matrix4f, xStart, widthY * beamSegment, 0.0F).color(255, 255, 255, alpha).tex(uvX1, uvY1).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            beamVertexBuilder.pos(matrix4f, -xStart, widthY * beamSegment, 0.0F).color(255, 255, 255, alpha).tex(uvX0, uvY1).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            beamVertexBuilder.pos(matrix4f, -xStart, (widthY * beamSegment) + widthY, 0.0F).color(255, 255, 255, alpha).tex(uvX0, uvY0).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        }

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
