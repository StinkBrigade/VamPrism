package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.common.VamPrism;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockRegistry
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VamPrism.MODID);

    public static final RegistryObject<Block> FANKRYSTAL_ORE = BLOCKS.register("fankrystal_ore", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> FANKRYSTAL_BLOCK = BLOCKS.register("fankrystal_block", () -> new BreakableBlock(AbstractBlock.Properties.create(Material.ROCK).notSolid().setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F)));
    public static final RegistryObject<Block> IRON_COIL = BLOCKS.register("iron_coil", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.IRON).notSolid().setOpaque(BlockRegistry::isntSolid)));

    public static final RegistryObject<Block> SUNLIGHT_POOL = BLOCKS.register("sunlight_pool", () -> new SunlightPoolBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F).notSolid().setOpaque(BlockRegistry::isntSolid)));
    public static final RegistryObject<Block> PRISMATIC_TESLA = BLOCKS.register("prismatic_tesla", () -> new PrismaticTeslaBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F).notSolid().setOpaque(BlockRegistry::isntSolid)));
    public static final RegistryObject<Block> PRISMA_STORE = BLOCKS.register("prisma_store", () -> new PrismaStoreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F).notSolid().setOpaque(BlockRegistry::isntSolid)));

    // Fake Blocks for renderers, multiblocks, etc
    public static final RegistryObject<Block> SUNLIGHT_BLOCK = BLOCKS.register("sunlight_block", () -> new Block(AbstractBlock.Properties.create(Material.GLASS)));
    public static final RegistryObject<Block> PRISMATIC_CUBE = BLOCKS.register("prismatic_cube", () -> new Block(AbstractBlock.Properties.create(Material.GLASS).notSolid().setOpaque(BlockRegistry::isntSolid)));
    public static final RegistryObject<Block> PRISMATIC_CUBE_IRON_CORNERS = BLOCKS.register("prismatic_cube_iron_corners", () -> new Block(AbstractBlock.Properties.create(Material.GLASS).notSolid().setOpaque(BlockRegistry::isntSolid)));

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return false;
    }
}
