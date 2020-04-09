package astavie.bookdisplay.wrapper.botania;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.item.ItemLexicon;

public class BotaniaWrapper extends BookWrapper<GuiLexicon> {

	private BotaniaWrapper() {
		super(GuiLexicon.currentOpenLexicon);
	}

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof ItemLexicon, item -> new BotaniaWrapper());
	}

	@Override
	public void left() {
		if (book instanceof GuiLexiconEntry) {
			GuiLexiconEntry entry = (GuiLexiconEntry) book;
			if (entry.page > 0) {
				entry.getEntry().pages.get(entry.page).onClosed(entry);
				entry.page--;
				entry.getEntry().pages.get(entry.page).onOpened(entry);
			}
		}
	}

	@Override
	public void right() {
		if (book instanceof GuiLexiconEntry) {
			GuiLexiconEntry entry = (GuiLexiconEntry) book;
			if (entry.page < entry.getEntry().pages.size() - 1) {
				entry.getEntry().pages.get(entry.page).onClosed(entry);
				entry.page++;
				entry.getEntry().pages.get(entry.page).onOpened(entry);
			}
		}
	}

}
