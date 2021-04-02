package astavie.bookdisplay.wrapper.tis3d;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import li.cil.tis3d.common.init.Items;
import net.minecraft.client.gui.GuiScreen;

public abstract class TIS3DWrapper<T extends GuiScreen> extends BookWrapper<T> {

	public TIS3DWrapper(T book) {
		super(book);
	}

	public static void register() {
		BookDisplay.register(Items::isBookCode, CodeBookWrapper::new);
		BookDisplay.register(Items::isBookManual, item -> new ManualWrapper());
	}

}
