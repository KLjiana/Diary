package com.kaleblangley.diary.client.screen;

import com.kaleblangley.diary.DiaryMod;
import com.kaleblangley.diary.diary.DiaryPaper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DiaryScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(DiaryMod.MODID, "textures/gui/book.png");
    private String[] texts;
    private int maxPage;
    private int page = 0;
    private PageButton forwardButton;
    private PageButton backButton;

    public DiaryScreen(DiaryPaper diaryPaper) {
        this(diaryPaper.texts());
    }

    public DiaryScreen(String[] texts) {
        super(CommonComponents.EMPTY);
        this.texts = texts;
    }

    @Override
    protected void init() {
        this.texts = this.split(this.texts, 102);
        this.maxPage = (texts.length - 1) / 16;
        this.addRenderableWidget(new Button.Builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(null)).pos(this.width / 2 - 100, 196).width(200).build());
        this.createPageControlButtons();
    }

    protected void createPageControlButtons() {
        int i = (this.width - 192) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 159, true, (p_98297_) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PageButton(i + 43, 159, false, (p_98287_) -> {
            this.pageBack();
        }, true));
        this.updateButtonVisibility();
    }

    protected void pageBack() {
        if (this.page > 0) {
            --this.page;
        }
        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.page < this.maxPage) {
            ++this.page;
        }
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.page < this.maxPage;
        this.backButton.visible = this.page > 0;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        int x = (this.width - 192) / 2;
        guiGraphics.blit(BOOK_LOCATION, x, 2, 0, 0, 192, 192);

        int linesPerPage = 16;
        int startLine = page * linesPerPage;
        int endLine = Math.min(startLine + linesPerPage, this.texts.length);

        for (int i = startLine; i < endLine; i++) {
            int lineOffset = i - startLine;
            guiGraphics.drawString(this.font, Component.nullToEmpty(this.texts[i]), x + 48, 18 + lineOffset * 9, 0x00000000, false);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private String[] split(String[] originalTexts, int maxWidth) {
        List<String> lines = new ArrayList<>();
        for (String originalText : originalTexts) {
            String translatableText = Component.translatable(originalText).getString();
            StringBuilder currentLine = new StringBuilder();

            for (int i = 0; i < translatableText.length(); i++) {
                char c = translatableText.charAt(i);
                if (c == '\n') {
                    lines.add(currentLine.toString());
                    currentLine.setLength(0);
                    continue;
                }
                currentLine.append(c);
                if (font.width(currentLine.toString()) > maxWidth) {
                    currentLine.setLength(currentLine.length() - 1);
                    lines.add(currentLine.toString());
                    currentLine.setLength(0);
                    currentLine.append(c);
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
        }

        return lines.toArray(String[]::new);
    }
}
