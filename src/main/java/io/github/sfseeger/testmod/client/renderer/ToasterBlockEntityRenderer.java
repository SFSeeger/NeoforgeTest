package io.github.sfseeger.testmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.testmod.common.blockentities.ToasterBlockEntity;
import io.github.sfseeger.testmod.common.blocks.ToasterBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class ToasterBlockEntityRenderer implements BlockEntityRenderer<ToasterBlockEntity> {
    private static final float SIZE = 0.375F;
    private final ItemRenderer itemRenderer;

    public ToasterBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    @Override
    public void render(ToasterBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction direction = pBlockEntity.getBlockState().getValue(ToasterBlock.FACING);
        IItemHandler itemHandler = pBlockEntity.getItemHandler(null);
        int pos = (int)pBlockEntity.getBlockPos().asLong();

        for (int i=0; i < itemHandler.getSlots(); i++){
            ItemStack item = itemHandler.getStackInSlot(i);
            if(item != ItemStack.EMPTY) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5F, 0.5F, 0.5F);
                //Direction direction1 = Direction.from2DDataValue((i + direction.get2DDataValue()) % 4);
                pPoseStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, pBlockEntity.getLevel(), pos+i);
                pPoseStack.popPose();
            }
        }
    }
}
