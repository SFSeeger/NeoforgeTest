package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import io.github.sfseeger.testmod.core.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TestMultiBlockEntity extends BlockEntity {
    public TestMultiBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.TEST_MULTI_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, TestMultiBlockEntity pBlockEntity) {
        if (pLevel.isClientSide) {
            return;
        }
        long i = pLevel.getGameTime();
        if (i % 20 == 0) {
            boolean active = updateShape(pLevel, pPos);
            if (active != pBlockEntity.isActive()) {
                pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                pLevel.playSound(null, pPos, BlockInit.TEST_MULTI_BLOCK.get()
                        .getSoundType(pState, pLevel, pPos, null)
                        .getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            pBlockEntity.setActive(active);
        }
    }

    public static void animateTick(Level pLevel, BlockPos pPos, BlockState pState, TestMultiBlockEntity pBlockEntity) {
        if (pBlockEntity.isActive()) {
            double randomX = pLevel.random.nextDouble();
            double randomZ = pLevel.random.nextDouble();
            pLevel.addParticle(ParticleTypes.SMOKE, pPos.getX() + 0.5D + randomX, pPos.getY() + 0.5D,
                               pPos.getZ() + 0.5D + randomZ,
                               0.0D, 0.0D, 0.0D);
        }
    }

    private static boolean updateShape(Level pLevel, BlockPos pPos) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (pLevel.getBlockState(pPos.offset(i, -1, j)).getBlock() != Blocks.EMERALD_BLOCK) {
                    return false;
                }
            }
        }
        return true;
    }
}