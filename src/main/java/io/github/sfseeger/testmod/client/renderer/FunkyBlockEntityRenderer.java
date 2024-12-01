package io.github.sfseeger.testmod.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import foundry.veil.Veil;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import io.github.sfseeger.testmod.common.blockentities.FunkyBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FunkyBlockEntityRenderer implements BlockEntityRenderer<FunkyBlockEntity> {
    private final BlockEntityRendererProvider.Context context;
    private static final ResourceLocation CUSTOM_SHADER = Veil.veilPath("funky");

    public FunkyBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.context = pContext;
    }

    @Override
    public void render(FunkyBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
            MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ShaderProgram shader = VeilRenderSystem.setShader(CUSTOM_SHADER);
        if (shader == null) {
            return;
        }

        shader.setFloat("time", (float) (System.currentTimeMillis() % 1000000) / 1000.0f);
        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath("testmod",
                                                                               "textures/block/generator_top.png"));
        shader.bind();

        ShaderProgram.unbind();
    }
}
