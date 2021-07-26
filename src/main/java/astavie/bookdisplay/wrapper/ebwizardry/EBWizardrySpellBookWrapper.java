package astavie.bookdisplay.wrapper.ebwizardry;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import electroblob.wizardry.client.gui.GuiSpellBook;
import electroblob.wizardry.item.ItemSpellBook;
import net.minecraft.item.ItemStack;

public class EBWizardrySpellBookWrapper extends BookWrapper<GuiSpellBook> {

    private EBWizardrySpellBookWrapper(ItemStack stack) {
        super(new GuiSpellBook(stack), true);
    }

    public static void register() {
        BookDisplay.register(item -> item.getItem() instanceof ItemSpellBook, item -> new EBWizardrySpellBookWrapper(item));
    }

}
