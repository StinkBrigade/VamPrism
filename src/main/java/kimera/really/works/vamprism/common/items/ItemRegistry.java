package kimera.really.works.vamprism.common.items;

import kimera.really.works.vamprism.VamPrism;
import kimera.really.works.vamprism.common.blocks.BlockRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VamPrism.MODID);

    public static final RegistryObject<Item> FANKRYSTAL = ITEMS.register("fankrystal", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    public static final RegistryObject<Item> FANKRYSTAL_NEEDLE = ITEMS.register("fankrystal_needle", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    public static final RegistryObject<Item> FANKRYSTAL_ORE = ITEMS.register("fankrystal_ore", () -> new BlockItem(BlockRegistry.FANKRYSTAL_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> FANKRYSTAL_BLOCK = ITEMS.register("fankrystal_block", () -> new BlockItem(BlockRegistry.FANKRYSTAL_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SUNLIGHT_POOL = ITEMS.register("sunlight_pool", () -> new BlockItem(BlockRegistry.SUNLIGHT_POOL.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
}
