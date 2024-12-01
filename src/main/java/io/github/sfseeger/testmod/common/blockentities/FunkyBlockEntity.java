package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FunkyBlockEntity extends BlockEntity {
    public FunkyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.FUNKY_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
