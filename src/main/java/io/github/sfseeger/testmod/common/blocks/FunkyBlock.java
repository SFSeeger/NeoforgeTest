package io.github.sfseeger.testmod.common.blocks;

import io.github.sfseeger.testmod.common.blockentities.FunkyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FunkyBlock extends Block implements EntityBlock {
    public FunkyBlock() {
        super(Properties.of().destroyTime(2.0f).sound(SoundType.AMETHYST).friction(0.1f).jumpFactor(2));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FunkyBlockEntity(pPos, pState);
    }
}
