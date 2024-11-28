package io.github.sfseeger.testmod.common.items;

import io.github.sfseeger.testmod.core.init.BlockInit;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class ToasterItem extends BlockItem implements Equipable {
    public ToasterItem() {
        super(BlockInit.TOASTER_BLOCK.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        //Check if item is in the mainhand, offhand or head slot
        if (!(pIsSelected || pSlotId == 40 || pSlotId == 39)) {
            return;
        }
        if (pLevel.isClientSide()) {
            return;
        }
        if (pLevel.getFluidState(pEntity.blockPosition()).getType().isSame(Fluids.WATER)) {
            pEntity.hurt(pEntity.damageSources().magic(), 9999999f);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return armorType == EquipmentSlot.HEAD || armorType == EquipmentSlot.OFFHAND || armorType == EquipmentSlot.MAINHAND;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }


    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public @NotNull Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_CHAIN;
    }
}
