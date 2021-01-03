package kimera.really.works.vamprism.client.renderer.tileentitty;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.PrismaticTeslaTileEntity;
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
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Vector;

public class PrismaticTeslaTileEntityRenderer extends TileEntityRenderer<PrismaticTeslaTileEntity>
{
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(VamPrism.MODID, "textures/entity/light_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.getEntityTranslucent(BEAM_TEXTURE);

    private static final int BEAM_TEXTURE_FRAMES = 64;

    private static final int BEAM_TEXTURE_WIDTH = 8;
    private static final int BEAM_TEXTURE_HEIGHT = 1024;

    private static final int BEAM_FRAME_WIDTH = 8;
    private static final int BEAM_FRAME_HEIGHT = 16;

    private static final int BEAM_SEGMENTS = 32;

    private static final float BEAM_WIDTH_PROPORTION = ((float) BEAM_FRAME_WIDTH) / ((float) BEAM_TEXTURE_WIDTH);
    private static final float BEAM_HEIGHT_PROPORTION = ((float) BEAM_FRAME_HEIGHT) / ((float) BEAM_TEXTURE_HEIGHT);

    public PrismaticTeslaTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PrismaticTeslaTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        renderTop(tileEntity, blockRendererDispatcher, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        renderLight(tileEntity, blockRendererDispatcher, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderTop(PrismaticTeslaTileEntity tileEntity, BlockRendererDispatcher blockRendererDispatcher, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos();
        BlockState blockState = BlockRegistry.PRISMATIC_CUBE_IRON_CORNERS.get().getDefaultState();

        matrixStack.push();
        matrixStack.translate(0.0D, 2.0D, 0.0D);

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

    private void renderLight(PrismaticTeslaTileEntity tileEntity, BlockRendererDispatcher blockRendererDispatcher, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos();
        BlockState blockState = BlockRegistry.SUNLIGHT_BLOCK.get().getDefaultState();

        float rotation = tileEntity.getTicksExisted() + partialTicks;

        IVertexBuilder vertexBuilder = bufferIn.getBuffer(BEAM_RENDER_TYPE);

        matrixStack.push();

        matrixStack.translate(0.5F, 2.5F, 0.5F);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(rotation));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation * 2.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(rotation / 1.5F));

        int currentFrame = tileEntity.getTicksExisted() % BEAM_TEXTURE_FRAMES;

        drawCube(vertexBuilder, matrixStack, 0.0F, 0.0F, 0.0F, 0.4F, 0.4F, 0.4F, 0.0F, BEAM_WIDTH_PROPORTION, BEAM_HEIGHT_PROPORTION * currentFrame, BEAM_HEIGHT_PROPORTION * (currentFrame + 1), 255, 255, 255, 200);

        matrixStack.pop();
    }

    private static void drawCube(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float x, float y, float z, float width, float height, float depth, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
    {
        MatrixStack.Entry matrixStackEntry = matrixStack.getLast();
        Matrix3f matrix3f = matrixStackEntry.getNormal();
        Matrix4f matrix4f = matrixStackEntry.getMatrix();

        float xOffset = width / 2.0F;
        float yOffset = height / 2.0F;
        float zOffset = depth / 2.0F;

        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, y - yOffset, y + yOffset, z - zOffset, 0, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, y - yOffset, y + yOffset, z + zOffset, 0, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, z - zOffset, z + zOffset, y - yOffset, y + yOffset, x - xOffset, 1, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, z - zOffset, z + zOffset, y - yOffset, y + yOffset, x + xOffset, 1, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, z - zOffset, z + zOffset, y - yOffset, 2, false, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
        drawQuad(vertexBuilder, matrix4f, matrix3f, x - xOffset, x + xOffset, z - zOffset, z + zOffset, y + yOffset, 2, true, xUV1, xUV2, yUV1, yUV2, red, blue, green, alpha);
    }

    private static void drawQuad(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float a1, float a2, float b1, float b2, float c, int axis, boolean flipped, float xUV1, float xUV2, float yUV1, float yUV2, int red, int blue, int green, int alpha)
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

    private static void drawVertex(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, float xUV, float yUV, int red, int green, int blue, int alpha, float normalX, float normalY, float normalZ)
    {
        vertexBuilder.pos(matrix4f, x, y, z).color(red, green, blue, alpha).tex(xUV, yUV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
