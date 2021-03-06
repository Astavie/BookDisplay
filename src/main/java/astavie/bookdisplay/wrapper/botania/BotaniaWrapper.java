package astavie.bookdisplay.wrapper.botania;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.patchouli.PatchouliWrapper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class BotaniaWrapper {

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof ItemLexicon, item -> {
			Book book = BookRegistry.INSTANCE.books.get(ModItems.lexicon.getRegistryName());
			if (book != null) {
				if (!book.contents.getCurrentGui().canBeOpened()) {
					book.contents.currentGui = null;
					book.contents.guiStack.clear();
				}

				return new PatchouliWrapper(book.contents.getCurrentGui());
			}
			return null;
		});
	}

}
