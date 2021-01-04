package kimera.really.works.vamprism.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

public final class ClientUtil
{
    public static final void drawCube(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float x, float y, float z, float width, float height, float depth, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        matrixStack.push();

        MatrixStack.Entry matrixStackEntry = matrixStack.getLast();
        Matrix4f matrix4f = matrixStackEntry.getMatrix();
        Matrix3f matrix3f = matrixStackEntry.getNormal();

        float xOffset = width / 2.0F;
        float yOffset = height / 2.0F;
        float zOffset = depth / 2.0F;

        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, y - yOffset, y + yOffset, z - zOffset, 0, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, y - yOffset, y + yOffset, z + zOffset, 0, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, z - zOffset, z + zOffset, y - yOffset, y + yOffset, x - xOffset, 1, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, z - zOffset, z + zOffset, y - yOffset, y + yOffset, x + xOffset, 1, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, z - zOffset, z + zOffset, y - yOffset, 2, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, z - zOffset, z + zOffset, y + yOffset, 2, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);

        matrixStack.pop();
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

        drawVertQuad(vertexBuilder, matrix4f, matrix3f, left, right, y, top, front, front, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, right, left, y, top, back, back, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, left, left, y, top, front, back, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawVertQuad(vertexBuilder, matrix4f, matrix3f, right, right, y, top, back, front, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
    }

    public static final void drawQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float a1, float a2, float b1, float b2, float c, int axis, boolean flipped, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        float x1 = axis == 1 ? c : axis == 0 && flipped ? a1 : a2;
        float x2 = axis == 1 ? c : axis == 0 && flipped ? a2 : a1;
        float y1 = axis == 2 ? c : axis == 1 && flipped ? b2 : b1;
        float y2 = axis == 2 ? c : axis == 1 && flipped ? b1 : b2;
        float z1 = axis == 0 ? c : axis == 2 ? flipped ? b1 : b2 : a1;
        float z2 = axis == 0 ? c : axis == 2 ? flipped ? b2 : b1 : a2;
        float z3 = axis == 2 ? z1 : z2;
        float z4 = axis == 2 ? z2 : z1;

        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y1, z3, xUV2, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y2, z2, xUV2, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y2, z4, xUV1, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y1, z1, xUV1, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
    }

    public static final void drawHorizQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x1, float x2, float z1, float z2, float y, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y, z1, xUV2, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y, z2, xUV2, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y, z2, xUV1, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y, z1, xUV1, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
    }

    public static final void drawVertQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x1, float x2, float y1, float y2, float z1, float z2, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y1, z2, xUV2, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x2, y2, z2, xUV2, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y2, z1, xUV1, yUV1, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
        drawVertex(vertexBuilder, matrix4f, matrix3f, x1, y1, z1, xUV1, yUV2, red, blue, green, alpha, 0.0F, 1.0F, 0.0F);
    }

    public static final void drawVertex(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, float xUV, float yUV, int red, int green, int blue, int alpha, float normalX, float normalY, float normalZ)
    {
        vertexBuilder.pos(matrix4f, x, y, z).color(red, green, blue, alpha).tex(xUV, yUV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
