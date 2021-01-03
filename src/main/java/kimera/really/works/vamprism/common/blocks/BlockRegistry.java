package kimera.really.works.vamprism.common.blocks;

import kimera.really.works.vamprism.VamPrism;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VamPrism.MODID);

    public static final RegistryObject<Block> FANKRYSTAL_ORE = BLOCKS.register("fankrystal_ore", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> FANKRYSTAL_BLOCK = BLOCKS.register("fankrystal_block", () -> new BreakableBlock(AbstractBlock.Properties.create(Material.ROCK).notSolid().setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F)));

    public static final RegistryObject<Block> SUNLIGHT_POOL = BLOCKS.register("sunlight_pool", () -> new SunlightPoolBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 4.0F).notSolid().setOpaque(BlockRegistry::isntSolid)));
    // Fake Blocks for renderers, multiblocks, etc
    public static final RegistryObject<Block> SUNLIGHT_BLOCK = BLOCKS.register("sunlight_block", () -> new Block(AbstractBlock.Properties.create(Material.GLASS)));
    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return false;
    }
}
