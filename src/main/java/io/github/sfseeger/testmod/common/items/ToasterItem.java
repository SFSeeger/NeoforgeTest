package io.github.sfseeger.testmod.common.items;

import io.github.sfseeger.testmod.core.init.BlockInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;

public class ToasterItem extends BlockItem {
    public ToasterItem() {
        super(BlockInit.TOASTER_BLOCK.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
       if(!pIsSelected) {
           return;
       }
       if(pLevel.isClientSide()) {
           return;
       }
       if(pLevel.getFluidState(pEntity.blockPosition()).getType().isSame(Fluids.WATER)){
          pEntity.hurt(pEntity.damageSources().magic(), 9999999f);
       }
    }
}
