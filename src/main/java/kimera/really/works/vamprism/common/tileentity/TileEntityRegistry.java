package kimera.really.works.vamprism.common.tileentity;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, VamPrism.MODID);

    public static final RegistryObject<TileEntityType<SunlightPoolTileEntity>> SUNLIGHT_POOL = TILE_ENTITIES.register("sunlight_pool", () -> TileEntityType.Builder.create(SunlightPoolTileEntity::new, BlockRegistry.SUNLIGHT_POOL.get()).build(null));
    public static final RegistryObject<TileEntityType<PrismaticTeslaTileEntity>> PRISMATIC_TESLA = TILE_ENTITIES.register("prismatic_tesla", () -> TileEntityType.Builder.create(PrismaticTeslaTileEntity::new, BlockRegistry.PRISMATIC_TESLA.get()).build(null));
    public static final RegistryObject<TileEntityType<PrismaStoreTileEntity>> PRISMA_STORE = TILE_ENTITIES.register("prisma_store", () -> TileEntityType.Builder.create(PrismaStoreTileEntity::new, BlockRegistry.PRISMA_STORE.get()).build(null));
}
