package kimera.really.works.vamprism.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.AbstractPrismaStorerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public final class ClientUtil
{
    public static final void drawBeam(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float x, float y, float z, float width, float height, float depth, float segmentHeight, int currentFrame, int totalFrames, int red, int blue, int green, int alpha, boolean fade)
    {
        int segments = MathHelper.ceil(height / segmentHeight);
        drawBeam(vertexBuilder, matrixStack, x, y, z, width, depth, segments, segmentHeight, currentFrame, totalFrames, red, blue, green, alpha, fade);
    }

    public static final void drawBeam(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float x, float y, float z, float width, float depth, int segments, float segmentHeight, int currentFrame, int totalFrames, int red, int blue, int green, int alpha, boolean fade)
    {
        MatrixStack.Entry matrixStackEntry = matrixStack.getLast();
        Matrix4f matrix4f = matrixStackEntry.getMatrix();
        Matrix3f matrix3f = matrixStackEntry.getNormal();

        float frameUVHeight = 1.0F / ((float) totalFrames);

        int fadeInteger = fade ? 1 : 0;

        for(int beamSegment = 0; beamSegment < segments; beamSegment++)
        {
            float segmentProportion = ((float) beamSegment) / ((float) segments);
            int alphaProportion = ((int) (((float) alpha) * segmentProportion) * fadeInteger) + (1 * (1 - fadeInteger));

            int segmentAlpha = alpha - alphaProportion;

            float segmentY = y + (segmentHeight * beamSegment);

            int currentSegmentFrame = (currentFrame + beamSegment) % totalFrames;

            float xUV1 = 0.0F;
            float xUV2 = 1.0F;
            float yUV1 = frameUVHeight * currentSegmentFrame;
            float yUV2 = frameUVHeight * (currentSegmentFrame + 1);

            drawBeamSegment(vertexBuilder, matrix4f, matrix3f, x, segmentY, z, width, segmentHeight, depth, xUV1, xUV2, yUV1, yUV2, red, blue, green, segmentAlpha);
        }
    }

    public static final void drawBeamSegment(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, float width, float height, float depth, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        float xOffset = width / 2.0F;
        float zOffset = depth / 2.0F;

        float left = x - xOffset;
        float right = x + xOffset;
        float top = y + height;
        float front = z - zOffset;
        float back = z + zOffset;

        drawVertQuad(vertexBuilder, matrix4f, matrix3f, right, left, y, top, front, front, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, left, right, y, top, back, back, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, left, left, y, top, front, back, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, right, right, y, top, back, front, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
    }

    public static final void drawVertQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x1, float x2, float y1, float y2, float z1, float z2, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        drawQuad(vertexBuilder, matrix4f, matrix3f, x2, x2, x1, x1, y1, y2, y2, y1, z2, z2, z1, z1, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
    }

    public static final void drawHorizQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x1, float x2, float y1, float y2, float z1, float z2, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        drawQuad(vertexBuilder, matrix4f, matrix3f, x2, x2, x1, x1, y2, y2, y1, y1, z1, z2, z2, z1, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
    }
    public static final void drawQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4, float z1, float z2, float z3, float z4, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y1, z1, xUV2, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y2, z2, xUV2, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x3, y3, z3, xUV1, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x4, y4, z4, xUV1, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
    }

    public static final void drawVertex(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, float xUV, float yUV, int red, int green, int blue, int alpha, float normalX, float normalY, float normalZ)
    {
        vertexBuilder.pos(matrix4f, x, y, z).color(red, green, blue, alpha).tex(xUV, yUV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static final void renderSunlightCube(AbstractPrismaStorerTileEntity tileEntity, double x, double startY, double endY, double z, float width, float height, float depth, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos();
        BlockState blockState = BlockRegistry.SUNLIGHT_BLOCK.get().getDefaultState();

        float scalePercentage = tileEntity.getTotalFillRatio();
        scalePercentage = MathHelper.clamp(scalePercentage, 0.001F, 1F);

        matrixStack.push();
        matrixStack.translate(x, (scalePercentage * (endY - startY)) + startY, z);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(0.0F));
        matrixStack.scale(width, scalePercentage * height, depth);

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
