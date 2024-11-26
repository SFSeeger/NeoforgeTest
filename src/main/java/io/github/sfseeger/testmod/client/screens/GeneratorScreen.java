package io.github.sfseeger.testmod.client.screens;

import com.mojang.logging.LogUtils;
import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.guis.GeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(TestMod.MODID, "textures/gui/container/generator/generator.png");

    private static final Logger LOGGER = LogUtils.getLogger();

    public GeneratorScreen(GeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }


    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Render energy bar
        int energyHeight = (int) (this.menu.getEnergyProgress() * 49f);
        pGuiGraphics.blit(GUI_TEXTURE, x + 8, y + 17 + (48 -energyHeight), 176, 48 - energyHeight, 16, energyHeight);

        // Render burn time
        int burnTimeHeight = (int) (this.menu.getBurnProgress() * 14f);
        //LOGGER.info("Burn time height: " + burnTimeHeight);
        if(this.menu.isLit()){
            pGuiGraphics.blit(GUI_TEXTURE, x + 81, y + 23 + burnTimeHeight, 176, 49 + burnTimeHeight, 14, 14 - burnTimeHeight);
        }
        pGuiGraphics.drawString(this.font, "Energy: " + energyHeight, x + 8, y + 12, Color.WHITE.getRGB());
        pGuiGraphics.drawString(this.font, "Max Burn Time: " + burnTimeHeight, x + 8, y + 18, Color.WHITE.getRGB());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }
}
