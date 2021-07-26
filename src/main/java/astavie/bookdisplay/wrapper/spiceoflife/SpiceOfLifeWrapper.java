package astavie.bookdisplay.wrapper.spiceoflife;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;

import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.client.gui.elements.UILabel;
import com.cazsius.solcarrot.item.ItemFoodBook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SpiceOfLifeWrapper extends BookWrapper<GuiFoodBook> {

    private static final Field FIELD_CURRENT_PAGE_NUMBER = ObfuscationReflectionHelper.findField(GuiFoodBook.class, "currentPageNumber");
    private static final Field FIELD_PAGES = ObfuscationReflectionHelper.findField(GuiFoodBook.class, "pages");
    private static final Method updateButtonVisibility = ObfuscationReflectionHelper.findMethod(GuiFoodBook.class, "updateButtonVisibility", void.class);
    private static final Field pageNumberLabel = ObfuscationReflectionHelper.findField(GuiFoodBook.class, "pageNumberLabel");

    private SpiceOfLifeWrapper(EntityPlayer player) {
        super(new GuiFoodBook(player), true);
    }

    public static void register() {
        BookDisplay.register(item -> item.getItem() instanceof ItemFoodBook, item -> new SpiceOfLifeWrapper(Minecraft.getMinecraft().player));
    }

    @Override
    public void left() {
        try {
            int currentPageNumber = (int) FIELD_CURRENT_PAGE_NUMBER.get(this.book);

            if (currentPageNumber > 0) {
                FIELD_CURRENT_PAGE_NUMBER.set(this.book, --currentPageNumber);
                updateButtonVisibility.invoke(this.book);

                UILabel label = (UILabel) pageNumberLabel.get(this.book);
                label.text = "" + (currentPageNumber + 1);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void right() {
        try {
            int currentPageNumber = (int) FIELD_CURRENT_PAGE_NUMBER.get(this.book);
            List<?> pages = (List<?>) FIELD_PAGES.get(this.book);

            if (currentPageNumber < pages.size() - 1) {
                FIELD_CURRENT_PAGE_NUMBER.set(this.book, ++currentPageNumber);
                updateButtonVisibility.invoke(this.book);

                UILabel label = (UILabel) pageNumberLabel.get(this.book);
                label.text = "" + (currentPageNumber + 1);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
