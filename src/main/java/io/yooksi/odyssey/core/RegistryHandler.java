package io.yooksi.odyssey.core;

import io.yooksi.odyssey.block.ModBlocks;
import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.entity.ModEntityTypes;
import io.yooksi.odyssey.ODLogger;
import io.yooksi.odyssey.item.ModItemGroup;
import io.yooksi.odyssey.item.ModItems;
import io.yooksi.odyssey.util.RLHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@ObjectHolder(Defines.MODID)
@Mod.EventBusSubscriber(modid = Defines.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {


	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

		final IForgeRegistry<Item> registry = event.getRegistry();
		AtomicInteger blockItemCount = new AtomicInteger();

		ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)
				.filter(ModBlocks::needsItemBlock).forEach(block -> {

					final Item.Properties properties = new Item.Properties().group(ModItemGroup.MAIN);
					final BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
					registry.register(blockItem);
					blockItemCount.getAndIncrement();
				});

		ODLogger.debug("Registered %d BlockItems", blockItemCount.get());
	}

	public static void registerDeferred(IEventBus iEventBus) {

		ModBlocks.BLOCKS.register(iEventBus);
		ModItems.ITEMS.register(iEventBus);
		ModEntityTypes.ENTITY_TYPES.register(iEventBus);
	}

	/**
	 * Prepare given registry entry for registration process.
	 *
	 * @param entry registry entry to process.
	 * @param name path to use for registry name creation.
	 * @param <T> Forge registry entry type
	 *
	 * @return given registry entry
	 */
	private static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {

		entry.setRegistryName(RLHelper.getModResourceLocation(name));
		return entry;
	}
}