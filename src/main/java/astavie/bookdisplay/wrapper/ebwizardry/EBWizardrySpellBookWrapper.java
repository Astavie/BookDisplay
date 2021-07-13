package astavie.bookdisplay.wrapper.ebwizardry;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import electroblob.wizardry.client.gui.GuiSpellBook;
import electroblob.wizardry.client.gui.GuiSpellInfo;
import electroblob.wizardry.client.gui.handbook.GuiWizardHandbook;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.item.ItemWizardHandbook;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class EBWizardrySpellBookWrapper extends BookWrapper<GuiSpellBook> {

    private EBWizardrySpellBookWrapper(ItemStack stack) {
        super(new GuiSpellBook(stack), true);
    }

    public static void register() {
        BookDisplay.register(item -> item.getItem() instanceof ItemSpellBook, item -> new EBWizardrySpellBookWrapper(item));
    }

}
