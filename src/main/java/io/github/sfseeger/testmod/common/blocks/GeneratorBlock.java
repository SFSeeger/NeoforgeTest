package io.github.sfseeger.testmod.common.blocks;

import io.github.sfseeger.testmod.common.blockentities.GeneratorBlockEntity;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlock extends Block implements EntityBlock {
    public GeneratorBlock() {
        super(Properties.of().destroyTime(2.0f).sound(SoundType.METAL));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GeneratorBlockEntity(pPos, pState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> type) {
        return type == BlockEntityInit.GENERATOR_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof GeneratorBlockEntity generatorBlockEntity) {
                GeneratorBlockEntity.tick(level, pos, state, generatorBlockEntity);
            }
        } : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GeneratorBlockEntity generatorBlockEntity) {
                if(pStack.is(Items.COAL)) {
                    if (generatorBlockEntity.getEnergyStorage(null).canReceive()) {
                        generatorBlockEntity.getEnergyStorage(null).receiveEnergy(1000, false);
                        pStack.shrink(1);
                        generatorBlockEntity.invalidCapability();
                        return ItemInteractionResult.SUCCESS;
                    }
                } else if (pStack.is(Items.APPLE)) {
                    PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(pPlayer.getUUID(), "Current Energy: " + generatorBlockEntity.getEnergyStorage(null).getEnergyStored());
                    pPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false, ChatType.bind(ChatType.CHAT, pPlayer));
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.FAIL;
    }
}
