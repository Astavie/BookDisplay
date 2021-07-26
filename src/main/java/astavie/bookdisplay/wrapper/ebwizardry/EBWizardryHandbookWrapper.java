package astavie.bookdisplay.wrapper.ebwizardry;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import electroblob.wizardry.client.gui.handbook.GuiWizardHandbook;
import electroblob.wizardry.item.ItemWizardHandbook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class EBWizardryHandbookWrapper extends BookWrapper<GuiWizardHandbook> {

    private static final Field FIELD_CURRENT_PAGE = ObfuscationReflectionHelper.findField(GuiWizardHandbook.class, "currentPage");
    private static final Field FIELD_PAGE_COUNT = ObfuscationReflectionHelper.findField(GuiWizardHandbook.class, "pageCount");
    private static final Field FIELD_BOOKMARK_PAGE = ObfuscationReflectionHelper.findField(GuiWizardHandbook.class, "bookmarkPage");
    private static final Field FIELD_BOOKMARK_SECTION = ObfuscationReflectionHelper.findField(GuiWizardHandbook.class, "bookmarkSection");
    private static final Field FIELD_SECTIONS = ObfuscationReflectionHelper.findField(GuiWizardHandbook.class, "sections");
    private static final Field FIELD_START_PAGE;
    static {
        try {
            FIELD_START_PAGE = ObfuscationReflectionHelper.findField(Class.forName("electroblob.wizardry.client.gui.handbook.Section"), "startPage");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static int singleToDoublePage(int singlePageIndex) {
        return singlePageIndex / 2;
    }

    private EBWizardryHandbookWrapper() {
        super(new GuiWizardHandbook(), true);
    }

    public static void register() {
        BookDisplay.register(item -> item.getItem() instanceof ItemWizardHandbook, item -> new EBWizardryHandbookWrapper());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onOpen() {
        try {
            int bookmarkPage = (int) FIELD_BOOKMARK_PAGE.get(this.book);
            Object bookmarkSection = FIELD_BOOKMARK_SECTION.get(null);
            Map<String, ?> sections = (Map<String, ?>) FIELD_SECTIONS.get(null);
            final int startPage = (int) FIELD_START_PAGE.get(sections.get(bookmarkSection));

            if (bookmarkSection != null) {
                FIELD_CURRENT_PAGE.set(this.book, singleToDoublePage(startPage) + bookmarkPage);
                if (bookmarkPage > 0) {
                    Minecraft.getMinecraft().ingameGUI.setOverlayMessage("\u00a7bOpening book to bookmarked page...", false);
                }
                else {
                    Minecraft.getMinecraft().ingameGUI.setOverlayMessage("\u00a7cOpening book to Introduction since no page has been bookmarked.", false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void left() {
        try {
            int currentPage = (int) FIELD_CURRENT_PAGE.get(this.book);

            if (currentPage > 0)
                FIELD_CURRENT_PAGE.set(this.book, --currentPage);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void right() {
        try {
            int currentPage = (int) FIELD_CURRENT_PAGE.get(this.book);
            int pageCount = (int) FIELD_PAGE_COUNT.get(this.book);

            if (currentPage < singleToDoublePage(pageCount))
                FIELD_CURRENT_PAGE.set(this.book, ++currentPage);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
