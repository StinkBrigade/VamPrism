package kimera.really.works.vamprism.client.renderer.tileentitty;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import kimera.really.works.vamprism.common.tileentity.PrismaticTeslaTileEntity;
import kimera.really.works.vamprism.common.tileentity.SunlightPoolTileEntity;
import kimera.really.works.vamprism.common.util.MathUtils;
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

    private static final int BEAM_SEGMENTS = 16;

    private static final float TOP_CENTER_X = 0.5F;
    private static final float TOP_CENTER_Y = 0.5F;

    public PrismaticTeslaTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PrismaticTeslaTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        float rotation = tileEntity.getTicksExisted() + partialTicks;

        renderTop(tileEntity, blockRendererDispatcher, rotation, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        renderLight(tileEntity, blockRendererDispatcher, rotation, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderTop(PrismaticTeslaTileEntity tileEntity, BlockRendererDispatcher blockRendererDispatcher, float rotation, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos();
        BlockState blockState = BlockRegistry.PRISMATIC_CUBE_IRON_CORNERS.get().getDefaultState();

        float scaledRotation = -(rotation / 5.0F);
        float xTranslate = 0.5F;
        float zTranslate = 0.5F;

        matrixStack.push();

        matrixStack.translate(xTranslate,2.0D, zTranslate);

        matrixStack.rotate(Vector3f.YP.rotationDegrees(scaledRotation));

        matrixStack.translate(-xTranslate,0.0D, -zTranslate);

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

    private void renderLight(PrismaticTeslaTileEntity tileEntity, BlockRendererDispatcher blockRendererDispatcher, float rotation, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockPos blockPos = tileEntity.getPos().add(0, 1, 0);
        BlockState blockState = BlockRegistry.SUNLIGHT_BLOCK.get().getDefaultState();

        float xScale = 0.4F;
        float yScale = 0.4F;
        float zScale = 0.4F;

        matrixStack.push();

        matrixStack.translate(0.5F, 2.5F, 0.5F);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(rotation));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation * 2.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(rotation / 1.5F));

        matrixStack.translate(-(xScale / 2.0F), -(yScale / 2.0F), -(zScale / 2.0F));
        matrixStack.scale(xScale, yScale, zScale);

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
