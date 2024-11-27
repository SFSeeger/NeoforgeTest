package io.github.sfseeger.testmod.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;

public class ToasterBlock extends Block implements SimpleWaterloggedBlock {
    public ToasterBlock() {
        super(Properties.of().destroyTime(2.0f).sound(SoundType.METAL));
    }

}
