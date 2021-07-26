package astavie.bookdisplay.wrapper.cyclopscore;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.integrateddynamics.IntegratedDynamicsSoundEvents;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.infobook.OnTheDynamicsOfIntegrationBook;
import org.cyclops.integrateddynamics.item.ItemOnTheDynamicsOfIntegration;

import java.lang.reflect.Field;

public class IntegratedDynamicsWrapper extends BookWrapper<GuiInfoBook> {

    protected final IInfoBook infoBook;

    private static final Field FIELD_NEXT_PAGE = ObfuscationReflectionHelper.findField(GuiInfoBook.class, "nextPage");
    private static final Field FIELD_NEXT_SECTION = ObfuscationReflectionHelper.findField(GuiInfoBook.class, "nextSection");

    protected static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID,
            Reference.TEXTURE_PATH_GUI + "on_the_dynamics_of_integration_gui.png");

    private IntegratedDynamicsWrapper(EntityPlayer player, int itemIndex, OnTheDynamicsOfIntegrationBook infoBook) {
        super(new GuiInfoBook(player, itemIndex, OnTheDynamicsOfIntegrationBook.getInstance(), texture) {

            @Override
            protected int getGuiWidth() {
                return 283;
            }

            @Override
            protected int getGuiHeight() { return 180; }

            @Override
            protected int getPageWidth() {
                return 142;
            }

            @Override
            protected int getPageYOffset() {
                return 9;
            }

            @Override
            protected int getFootnoteOffsetX() {
                return -2;
            }

            @Override
            protected int getFootnoteOffsetY() {
                return -8;
            }

            @Override
            protected int getPrevNextOffsetY() {
                return 7;
            }

            @Override
            protected int getPrevNextOffsetX() {
                return 16;
            }

            @Override
            protected int getOffsetXForPageBase(int page) {
                return page == 0 ? 20 : 10;
            }

            @Override
            public int getTitleColor() {
                return Helpers.RGBToInt(70, 70, 150);
            }

            @Override
            public void playPageFlipSound(SoundHandler soundHandler) {
                soundHandler.playSound(PositionedSoundRecord.getMasterRecord(IntegratedDynamicsSoundEvents.effect_page_flipsingle, 1.0F));
            }

            @Override
            public void playPagesFlipSound(SoundHandler soundHandler) {
                soundHandler.playSound(PositionedSoundRecord.getMasterRecord(IntegratedDynamicsSoundEvents.effect_page_flipmultiple, 1.0F));
            }
        });

        this.infoBook = infoBook;
        if(infoBook.getCurrentSection() == null) {
            InfoSection root = infoBook.getMod().getRegistryManager().getRegistry(IInfoBookRegistry.class).getRoot(infoBook);
            if (root == null) {
                throw new IllegalStateException("Could not find the root of infobook " + infoBook);
            }
            infoBook.setCurrentSection(root);
            infoBook.setCurrentPage(0);
        }
    }

    public static void register() {
            BookDisplay.register(item -> item.getItem() instanceof ItemOnTheDynamicsOfIntegration, item -> new IntegratedDynamicsWrapper(Minecraft.getMinecraft().player, 1, OnTheDynamicsOfIntegrationBook.getInstance()));
        }

    protected int getPages() {
        return infoBook.getPagesPerView();
    }

    @Override
    public void left() {
        try {
            int nextPage = (int) FIELD_NEXT_PAGE.get(this.book);
            InfoSection nextSection = (InfoSection) FIELD_NEXT_SECTION.get(this.book);

            nextSection = infoBook.getCurrentSection();
            nextPage = infoBook.getCurrentPage();

            InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));

            if(nextSection != null && (nextSection != infoBook.getCurrentSection() || infoBook.getCurrentPage() != nextPage)) {
                infoBook.setCurrentSection(nextSection);
                nextSection = null;
                infoBook.setCurrentPage(nextPage);
                book.initGui();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void right() {
        try {
            int nextPage = (int) FIELD_NEXT_PAGE.get(this.book);
            InfoSection nextSection = (InfoSection) FIELD_NEXT_SECTION.get(this.book);

            nextSection = infoBook.getCurrentSection();
            nextPage = infoBook.getCurrentPage();

            InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));

            if(nextSection != null && (nextSection != infoBook.getCurrentSection() || infoBook.getCurrentPage() != nextPage)) {
                infoBook.setCurrentSection(nextSection);
                nextSection = null;
                infoBook.setCurrentPage(nextPage);
                book.initGui();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
