package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class ToasterBlockEntity extends BlockEntity {
    public static final int NUM_SLOTS = 2;
    public final ItemStackHandler itemHandler = new ItemStackHandler(NUM_SLOTS) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return this.itemHandler;
    }

    private final int[] cookingProgress = new int[4];
    private final int[] cookingTime = new int[4];
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck =
            RecipeManager.createCheck(
                    RecipeType.CAMPFIRE_COOKING);

    public ToasterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.TOASTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public boolean placeFood(@Nullable LivingEntity pEntity, ItemStack pFood, int pCookTime) {
        for (int i = 0; i < NUM_SLOTS; i++) {
            ItemStack itemstack = itemHandler.getStackInSlot(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = pCookTime;
                this.cookingProgress[i] = 0;
                itemHandler.insertItem(i, pFood.consumeAndReturn(1, pEntity), false);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(),
                                     GameEvent.Context.of(pEntity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    public void removeFood(int pSlot) {
        this.itemHandler.setStackInSlot(pSlot, ItemStack.EMPTY);
        this.markUpdated();
    }

    public static void cookTick(Level pLevel, BlockPos pPos, BlockState pState, ToasterBlockEntity pBlockEntity) {
        boolean flag = false;

        for (int i = 0; i < ToasterBlockEntity.NUM_SLOTS; i++) {
            IItemHandler items = pBlockEntity.getItemHandler(null);
            ItemStack itemstack = items.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                flag = true;
                pBlockEntity.cookingProgress[i]++;
                if (pBlockEntity.cookingProgress[i] >= pBlockEntity.cookingTime[i]) {
                    SingleRecipeInput singlerecipeinput = new SingleRecipeInput(itemstack);
                    ItemStack itemstack1 = pBlockEntity.quickCheck
                            .getRecipeFor(singlerecipeinput, pLevel)
                            .map(cookingRecipeRecipeHolder -> cookingRecipeRecipeHolder.value()
                                    .assemble(singlerecipeinput, pLevel.registryAccess()))
                            .orElse(itemstack);
                    if (itemstack1.isItemEnabled(pLevel.enabledFeatures())) {
                        Containers.dropItemStack(pLevel, (double) pPos.getX(), (double) pPos.getY(),
                                                 (double) pPos.getZ(), itemstack1);
                        pBlockEntity.removeFood(i);
                        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pState));
                    }
                }
            }
        }

        if (flag) {
            setChanged(pLevel, pPos, pState);
        }
    }

    public Optional<RecipeHolder<CampfireCookingRecipe>> getCookableRecipe(ItemStack itemstack) {
        int fullSlots = 0;
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (!this.itemHandler.getStackInSlot(i).isEmpty()) fullSlots++;
        }
        return fullSlots != NUM_SLOTS ? this.quickCheck.getRecipeFor(new SingleRecipeInput(itemstack),
                                                                     this.level) : Optional.empty();
    }
}
