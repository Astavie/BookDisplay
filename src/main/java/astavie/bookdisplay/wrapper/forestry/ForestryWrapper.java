package astavie.bookdisplay.wrapper.forestry;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import forestry.api.gui.IGuiElement;
import forestry.book.ModuleBook;
import forestry.book.gui.GuiForesterBook;
import forestry.book.gui.GuiForestryBookPages;
import forestry.book.items.ItemForesterBook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ForestryWrapper extends BookWrapper<GuiForesterBook> {

    private static final Method METHOD_SET_PAGES = ObfuscationReflectionHelper.findMethod(GuiForestryBookPages.class, "setPages", void.class, int.class);
    private static final Field FIELD_PAGE_INDEX = ObfuscationReflectionHelper.findField(GuiForestryBookPages.class, "pageIndex");
    private static final Field FIELD_LAST_PAGE = ObfuscationReflectionHelper.findField(GuiForestryBookPages.class, "lastPage");
    private static final Field FIELD_PAGES = ObfuscationReflectionHelper.findField(GuiForestryBookPages.class, "pages");

    private ForestryWrapper(GuiForesterBook gui) { super(gui); }

    public static void register() {
        BookDisplay.register(item -> item.getItem() instanceof ItemForesterBook, bookStack ->
                new ForestryWrapper((GuiForesterBook) ModuleBook.getItems().book.getGui(Minecraft.getMinecraft().player, bookStack, 0)));

    }

    @Override
    public void left() {
        if (book instanceof GuiForestryBookPages) {
            try {
                int pageIndex = (int) FIELD_PAGE_INDEX.get(this.book);
                int lastPage = (int) FIELD_LAST_PAGE.get(this.book);

                if (pageIndex > 0) {
                    METHOD_SET_PAGES.invoke(this.book, pageIndex - 2);
                    book.initGui();
                    if (lastPage >= 0) {
                        FIELD_PAGES.set(this.book, -1);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else return;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void right() {
        if (book instanceof GuiForestryBookPages) {
            try {
                int pageIndex = (int) FIELD_PAGE_INDEX.get(this.book);
                int lastPage = (int) FIELD_LAST_PAGE.get(this.book);
                List<IGuiElement> pages = (List<IGuiElement>) FIELD_PAGES.get(this.book);

                if (pageIndex < pages.size() - 2) {
                    METHOD_SET_PAGES.invoke(this.book, pageIndex + 2);
                    book.initGui();
                    if (lastPage >= 0) {
                        FIELD_PAGES.set(this.book, -1);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else return;
    }
}