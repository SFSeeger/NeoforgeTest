package io.github.sfseeger.testmod.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class FunkyBlock extends Block {
    public FunkyBlock() {
        super(Properties.of().destroyTime(2.0f).sound(SoundType.AMETHYST).friction(0.1f).jumpFactor(2));
    }
}
