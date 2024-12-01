package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.common.blocks.ToasterBlock;
import io.github.sfseeger.testmod.common.recipies.toaster.ToasterCookingRecipe;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import io.github.sfseeger.testmod.core.init.recipes.RecipeTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class ToasterBlockEntity extends BlockEntity {
    public static final int NUM_SLOTS = 2;
    public final ItemStackHandler itemHandler = new ItemStackHandler(NUM_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ToasterBlockEntity.this.markUpdated();
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };

    private final Lazy<Optional<ItemStackHandler>> optional = Lazy.of(() -> Optional.of(itemHandler));

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side != null) {
            return null;
        }
        return this.itemHandler;
    }

    private final int[] cookingProgress = new int[NUM_SLOTS];
    private final int[] cookingTime = new int[NUM_SLOTS];
    private final RecipeManager.CachedCheck<SingleRecipeInput, ToasterCookingRecipe> quickCheck =
            RecipeManager.createCheck(RecipeTypeInit.TOASTER_COOKING.get());

    public ToasterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.TOASTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putIntArray("CookingTimes", cookingTime);
        pTag.putIntArray("CookingTotalTimes", cookingProgress);
        pTag.put("Inventory", itemHandler.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("Inventory")) {
            itemHandler.deserializeNBT(pRegistries, pTag.getCompound("Inventory"));
        }
        if (pTag.contains("CookingTimes", 11)) {
            int[] aint = pTag.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, aint.length));
        }

        if (pTag.contains("CookingTotalTimes", 11)) {
            int[] aint1 = pTag.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, aint1.length));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        CompoundTag t = new CompoundTag();
        this.saveAdditional(t, pRegistries);
        return t;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(),
                                         ToasterBlock.UPDATE_ALL);
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

        IItemHandler items = pBlockEntity.getItemHandler(null);
        for (int i = 0; i < ToasterBlockEntity.NUM_SLOTS; i++) {
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
                        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pState));
                    }
                }
            }
        }

        pState.setValue(ToasterBlock.COOKING, flag);
        if (flag) {
            setChanged(pLevel, pPos, pState);
        }
    }

    public Optional<RecipeHolder<ToasterCookingRecipe>> getCookableRecipe(ItemStack itemstack) {
        int fullSlots = 0;
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (!this.itemHandler.getStackInSlot(i).isEmpty()) fullSlots++;
        }
        return fullSlots != NUM_SLOTS ? this.quickCheck.getRecipeFor(new SingleRecipeInput(itemstack),
                                                                     this.level) : Optional.empty();
    }
}
