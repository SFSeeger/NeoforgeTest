package io.github.sfseeger.testmod.client.screens;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.guis.GeneratorMenu;
import io.github.sfseeger.testmod.core.util.GUIUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TestMod.MODID, "textures/gui/container/generator/generator.png");

    private int uiX;
    private int uiY;

    public GeneratorScreen(GeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.uiX = (this.width - this.imageWidth) / 2;
        this.uiY = (this.height - this.imageHeight) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(GUI_TEXTURE, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight);

        // Render energy bar
        int energyHeight = (int) (this.menu.getEnergyProgress() * 49f);
        pGuiGraphics.blit(GUI_TEXTURE, uiX + 8, uiY + 17 + (49 - energyHeight), 176, 48 - energyHeight, 16,
                          energyHeight);

        // Render burn time
        int burnTimeHeight = (int) (this.menu.getBurnProgress() * 14f);
        if (this.menu.isLit()) {
            pGuiGraphics.blit(GUI_TEXTURE, uiX + 81, uiY + 23 + burnTimeHeight, 176, 49 + burnTimeHeight, 14,
                              14 - burnTimeHeight);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        if (GUIUtil.isMouseInBounds(uiX + 8, uiY + 17, 16, 49, pX, pY)) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(
                    this.menu.getEnergy() + " / " + this.menu.getMaxEnergy() + " RF"), pX, pY);
        }
    }
}
