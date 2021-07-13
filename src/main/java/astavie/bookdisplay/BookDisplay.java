package astavie.bookdisplay;

import astavie.bookdisplay.wrapper.IBookWrapper;
import astavie.bookdisplay.wrapper.bibliocraft.BiblioCraftWrapper;
import astavie.bookdisplay.wrapper.botania.BotaniaWrapper;
import astavie.bookdisplay.wrapper.cyclopscore.IntegratedDynamicsWrapper;
import astavie.bookdisplay.wrapper.ebwizardry.EBWizardryHandbookWrapper;
import astavie.bookdisplay.wrapper.ebwizardry.EBWizardrySpellBookWrapper;
import astavie.bookdisplay.wrapper.forestry.ForestryWrapper;
import astavie.bookdisplay.wrapper.immersiveengineering.IEWrapper;
import astavie.bookdisplay.wrapper.mantle.MantleWrapper;
import astavie.bookdisplay.wrapper.minecraft.VanillaWrapper;
import astavie.bookdisplay.wrapper.opencomputers.OCWrapper;
import astavie.bookdisplay.wrapper.patchouli.PatchouliWrapper;
import astavie.bookdisplay.wrapper.spiceoflife.SpiceOfLifeWrapper;
import astavie.bookdisplay.wrapper.tis3d.TIS3DWrapper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, clientSideOnly = true)
public class BookDisplay {

	static final KeyBinding key = new KeyBinding("key.bookdisplay.display", Keyboard.KEY_R, "key.categories.book");
	static final KeyBinding left = new KeyBinding("key.bookdisplay.left", Keyboard.KEY_LEFT, "key.categories.book");
	static final KeyBinding right = new KeyBinding("key.bookdisplay.right", Keyboard.KEY_RIGHT, "key.categories.book");

	private static final Map<Predicate<ItemStack>, Function<ItemStack, IBookWrapper>> registry = new HashMap<>();

	public static void register(Predicate<ItemStack> predicate, Function<ItemStack, IBookWrapper> factory) {
		registry.put(predicate, factory);
	}

	static boolean has(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		for (Map.Entry<Predicate<ItemStack>, Function<ItemStack, IBookWrapper>> entry : registry.entrySet())
			if (entry.getKey().test(stack))
				return true;
		return false;
	}

	static IBookWrapper find(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		for (Map.Entry<Predicate<ItemStack>, Function<ItemStack, IBookWrapper>> entry : registry.entrySet())
			if (entry.getKey().test(stack))
				return entry.getValue().apply(stack);
		return null;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		key.setKeyConflictContext(KeyConflictContext.IN_GAME);
		left.setKeyConflictContext(KeyConflictContext.IN_GAME);
		right.setKeyConflictContext(KeyConflictContext.IN_GAME);

		ClientRegistry.registerKeyBinding(key);
		ClientRegistry.registerKeyBinding(left);
		ClientRegistry.registerKeyBinding(right);

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		register(item -> item.getItem() == Items.WRITABLE_BOOK || item.getItem() == Items.WRITTEN_BOOK, VanillaWrapper::new);
		if (Loader.isModLoaded("bibliocraft"))				//bibliocraft
			BiblioCraftWrapper.register();
		if (Loader.isModLoaded("botania"))					//botania
			BotaniaWrapper.register();
		if (Loader.isModLoaded("cyclopscore")) {			//cyclops core
			if (Loader.isModLoaded("integrateddynamics"))	//integrated dynamics
				IntegratedDynamicsWrapper.register();
		}
		if (Loader.isModLoaded("ebwizardry")) {				//electrobob wizardry
			EBWizardryHandbookWrapper.register();
			EBWizardrySpellBookWrapper.register();
		}
		if (Loader.isModLoaded("forestry"))					//forestry
			ForestryWrapper.register();
		if (Loader.isModLoaded("immersiveengineering"))		//immersive engineering
			IEWrapper.register();
		if (Loader.isModLoaded("mantle"))					//mantle - tconstruct, constructs armory
			MantleWrapper.register();
		if (Loader.isModLoaded("opencomputers"))			// open computers
			OCWrapper.register();
		if (Loader.isModLoaded("solcarrot"))				//spice of life - carrot edition
			SpiceOfLifeWrapper.register();
		if (Loader.isModLoaded("tis3d"))					//tis3d
			TIS3DWrapper.register();
		if (Loader.isModLoaded("patchouli"))				//patchouli
			PatchouliWrapper.register();
	}

}
