package io.github.sfseeger.testmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.testmod.common.blockentities.TestMultiBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class TestMultiBlockEntityRenderer implements BlockEntityRenderer<TestMultiBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public TestMultiBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.context = pContext;
    }

    @Override
    public void render(TestMultiBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.isActive()) {
            int pos = (int) pBlockEntity.getBlockPos().asLong();
            Level level = pBlockEntity.getLevel();
            if (level == null) {
                return;
            }

            pPoseStack.pushPose();
            pPoseStack.translate(0.5f, 1.1f, 0.5f);
            //pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Math.sin(level.getGameTime() / 10.0) * 30.0f));
            pPoseStack.scale(0.8f, 0.8f, 0.8f);
            this.context.getItemRenderer().renderStatic(new ItemStack(Items.DIAMOND), ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, level, pos);
            pPoseStack.popPose();
        }
    }
}
