package astavie.bookdisplay.wrapper.immersiveengineering;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.gui.GuiManual;

public class IEWrapper extends BookWrapper<GuiManual> {

	private IEWrapper() {
		super(ManualHelper.getManual().getGui());
	}

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof IEItemInterfaces.IGuiItem && ((IEItemInterfaces.IGuiItem) item.getItem()).getGuiID(item) == Lib.GUIID_Manual, item -> new IEWrapper());
	}

	@Override
	public void left() {
		if (book.page > 0) {
			book.page--;
			book.initGui();
		}
	}

	@Override
	public void right() {
		ManualInstance.ManualEntry entry = book.getManual().getEntry(book.getSelectedEntry());
		if (entry != null && book.page < entry.getPages().length - 1) {
			book.page++;
			book.initGui();
		}
	}

}
