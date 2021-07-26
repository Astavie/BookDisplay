package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiClipboard;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ClipboardWrapper extends BiblioCraftWrapper<GuiClipboard> {

	private static final Method PREV = ObfuscationReflectionHelper.findMethod(GuiClipboard.class, "prevPage", void.class);
	private static final Method NEXT = ObfuscationReflectionHelper.findMethod(GuiClipboard.class, "nextPage", void.class);

	private final ItemStack item;
	private final ItemStack copy;

	ClipboardWrapper(ItemStack item) {
		super(new GuiClipboard(item.copy(), true, 0, 0, 0));
		this.item = item;
		this.copy = ObfuscationReflectionHelper.getPrivateValue(GuiClipboard.class, book, "clipStack");
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(0, (height - 192) / 2 - 2, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		NBTTagCompound tag = Objects.requireNonNull(copy.getTagCompound());
		if (tag.getInteger("currentPage") > 1) {
			try {
				PREV.invoke(book);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void right() {
		NBTTagCompound tag = Objects.requireNonNull(copy.getTagCompound());
		if (tag.getInteger("currentPage") < tag.getInteger("totalPages")) {
			try {
				NEXT.invoke(book);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		item.setTagCompound(copy.getTagCompound());
	}

}
