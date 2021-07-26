package astavie.bookdisplay.wrapper.tis3d;

import li.cil.tis3d.client.gui.GuiManual;
import li.cil.tis3d.client.manual.Document;
import li.cil.tis3d.common.api.ManualAPIImpl;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManualWrapper extends TIS3DWrapper<GuiManual> {

	private static final Method scrollTo = ObfuscationReflectionHelper.findMethod(GuiManual.class, "scrollTo", void.class, int.class);

	ManualWrapper() {
		super(new GuiManual());
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		try {
			scrollTo.invoke(book, ManualAPIImpl.peekOffset() - Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			scrollTo.invoke(book, ManualAPIImpl.peekOffset() + Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
