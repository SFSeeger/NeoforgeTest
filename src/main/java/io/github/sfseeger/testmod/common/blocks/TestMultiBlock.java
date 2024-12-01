package io.github.sfseeger.testmod.common.blocks;

import io.github.sfseeger.testmod.common.blockentities.TestMultiBlockEntity;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestMultiBlock extends Block implements EntityBlock {
    public TestMultiBlock() {
        super(Properties.of().sound(SoundType.COPPER));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TestMultiBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
            BlockEntityType<T> type) {
        return type == BlockEntityInit.TEST_MULTI_BLOCK_ENTITY.get() ? (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof TestMultiBlockEntity testMultiBlockEntity) {
                if (pLevel.isClientSide()) {
                    TestMultiBlockEntity.animateTick(level, pos, state, testMultiBlockEntity);
                } else {
                    TestMultiBlockEntity.serverTick(level, pos, state, testMultiBlockEntity);
                }
            }
        } : null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
            BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof TestMultiBlockEntity testMultiBlockEntity) {
                PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(pPlayer.getUUID(),
                                                                           "Is Active: " + (testMultiBlockEntity.isActive() ? "Yes" : "No"));
                pPlayer.createCommandSourceStack()
                        .sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false,
                                         ChatType.bind(ChatType.CHAT, pPlayer));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
