package astavie.bookdisplay.wrapper.spiceoflife;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.item.ItemFoodBook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class SpiceOfLifeWrapper extends BookWrapper<GuiFoodBook> {

    private static final Field FIELD_CURRENT_PAGE_NUMBER = ObfuscationReflectionHelper.findField(GuiFoodBook.class, "currentPageNumber");
    private static final Field FIELD_PAGES = ObfuscationReflectionHelper.findField(GuiFoodBook.class, "pages");

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
            }
        } catch (IllegalAccessException e) {
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
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
