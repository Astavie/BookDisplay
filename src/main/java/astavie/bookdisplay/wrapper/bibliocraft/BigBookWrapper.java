package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiBigBook;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class BigBookWrapper extends BiblioCraftWrapper<GuiBigBook> {

	BigBookWrapper(ItemStack stack) {
		super(new GuiBigBook(stack, true, 0, 0, 0, Minecraft.getMinecraft().player.getDisplayNameString()));
		ObfuscationReflectionHelper.setPrivateValue(GuiBigBook.class, book, true, "signed");
	}

	@Override
	public void left() {
		int page = ObfuscationReflectionHelper.getPrivateValue(GuiBigBook.class, book, "currentPage");
		if (page > 0) {
			ObfuscationReflectionHelper.setPrivateValue(GuiBigBook.class, book, page - 1, "currentPage");
			book.loadCurrentPageLinesFromNBT();
			book.initGui();
			book.buttonList.clear();
		}
	}

	@Override
	public void right() {
		int page = ObfuscationReflectionHelper.getPrivateValue(GuiBigBook.class, book, "currentPage");
		if (page < (int) ObfuscationReflectionHelper.getPrivateValue(GuiBigBook.class, book, "totalPages") - 1) {
			ObfuscationReflectionHelper.setPrivateValue(GuiBigBook.class, book, page + 1, "currentPage");
			book.loadCurrentPageLinesFromNBT();
			book.initGui();
			book.buttonList.clear();
		}
	}

}
