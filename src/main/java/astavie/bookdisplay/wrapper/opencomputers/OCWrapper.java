package astavie.bookdisplay.wrapper.opencomputers;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import li.cil.oc.client.gui.Manual;
import li.cil.oc.client.renderer.markdown.Document;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OCWrapper extends BookWrapper<Manual> {

	private static final Method scrollTo = ReflectionHelper.findMethod(Manual.class, "scrollTo", null, int.class);

	private OCWrapper() {
		super(new Manual());
	}

	@SuppressWarnings("ConstantConditions")
	public static void register() {
		BookDisplay.register(item -> ArrayUtils.contains(OreDictionary.getOreIDs(item), OreDictionary.getOreID("oc:manual")), item -> new OCWrapper());
	}

	@Override
	public void left() {
		try {
			scrollTo.invoke(book, book.offset() - Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			scrollTo.invoke(book, book.offset() + Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
