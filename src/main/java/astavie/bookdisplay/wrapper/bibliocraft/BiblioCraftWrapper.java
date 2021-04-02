package astavie.bookdisplay.wrapper.bibliocraft;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import jds.bibliocraft.gui.GuiRecipeBook;
import jds.bibliocraft.gui.GuiRedstoneBook;
import jds.bibliocraft.items.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class BiblioCraftWrapper<T extends GuiScreen> extends BookWrapper<T> {

	public BiblioCraftWrapper(T book) {
		super(book);
	}

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof ItemBigBook, BigBookWrapper::new);
		BookDisplay.register(item -> item.getItem() instanceof ItemClipboard, ClipboardWrapper::new);
		BookDisplay.register(item -> item.getItem() instanceof ItemRedstoneBook, item -> new BookWrapper<>(new GuiRedstoneBook(item)));
		BookDisplay.register(item -> item.getItem() instanceof ItemSlottedBook, item -> new BookWrapper<>(new SlottedBookWrapper(item)));
		BookDisplay.register(item -> item.getItem() instanceof ItemRecipeBook, item -> new BookWrapper<>(new GuiRecipeBook(item, false, Minecraft.getMinecraft().player.inventory.currentItem, 0, 0, 0, false)));
	}

}
