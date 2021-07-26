package astavie.bookdisplay.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BookWrapper<T extends GuiScreen> implements IBookWrapper {

	private static final Method keyTyped = ObfuscationReflectionHelper.findMethod(GuiScreen.class, "func_73869_a", void.class, char.class, int.class);

	private static BookWrapper<?> drawing = null;

	protected final T book;
	protected final boolean drawsBackground;

	protected int width;
	protected int height;

	public BookWrapper(T book, boolean drawsBackground) {
		this.book = book;
		this.drawsBackground = drawsBackground;
	}

	public BookWrapper(T book) {
		this(book, false);
	}

	public static void onDrawBackground() {
		if (drawing != null) {
			GlStateManager.translate(0, -drawing.height, 0);
		}
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(width / (side == EnumHandSide.RIGHT ? 4 : -4), 0, 0);

		if (drawsBackground) {
			// We translate down so the grey background gets lost
			drawing = this;

			GlStateManager.translate(0, height, 0);
			book.drawScreen(-1, -1, partialTicks);

			drawing = null;
		} else {
			// We don't do anything special
			book.drawScreen(-1, -1, partialTicks);
		}
	}

	@Override
	public void left() {
	}

	@Override
	public void right() {
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onTick() {
		book.updateScreen();
	}

	@Override
	public void onClose() {
		try {
			keyTyped.invoke(book, '\033', 1);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		book.onGuiClosed();
	}

	@Override
	public void setSize(int width, int height, EnumHandSide side) {
		this.width = width;
		this.height = height;

		book.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
		for (GuiButton button : book.buttonList)
			if (makeButtonInvisible(button))
				button.visible = false;
	}

	protected boolean makeButtonInvisible(GuiButton button) {
		return false;
	}

}
