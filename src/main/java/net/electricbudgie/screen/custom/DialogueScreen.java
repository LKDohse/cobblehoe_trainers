package net.electricbudgie.screen.custom;

import net.electricbudgie.CobblehoeTrainers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DialogueScreen extends Screen {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(CobblehoeTrainers.MOD_ID, "textures/gui/dialogue/dialogue_gui.png");
    private int pageIndex = 0;
    private List<List<OrderedText>> pages;

    private Text dialogueText;

    public DialogueScreen(Text title, Text dialog) {
        super(title);
        this.dialogueText = dialog;
        setDialog(dialogueText);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
       return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 1) return true;
        if (pages != null && pageIndex < pages.size() - 1) {
            pageIndex++;
        } else {
            close(); // Or set visible=false
        }
        return true;
    }

    public void setDialog(Text dialogueText) {
        List<OrderedText> wrappedLines = MinecraftClient.getInstance().textRenderer.wrapLines(dialogueText, 230);

        int linesPerPage = 3;
        pages = new ArrayList<>();
        for (int i = 0; i < wrappedLines.size(); i += linesPerPage) {
            pages.add(wrappedLines.subList(i, Math.min(i + linesPerPage, wrappedLines.size())));
        }
        pageIndex = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;

        // draw your background texture
        context.drawTexture(GUI_TEXTURE,
                centerX - 128, centerY + (centerY - 48),     // position
                0, 0,
                255, 48,
                255, 48);

        // draw your text
        if (!pages.isEmpty()) {
            List<OrderedText> currentPage = pages.get(pageIndex);
            for (int i = 0; i < currentPage.size(); i++ ) {
                context.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        currentPage.get(i),
                        centerX - 115,
                        centerY + (centerY - 38 + (i*10)),
                        0x000000,
                        false
                );
            }
        }
    }
}
