package io.github.sfseeger.testmod.common.items;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if(!state.getBlock().equals(Blocks.GOLD_BLOCK)) {
            return InteractionResult.FAIL;
        }
        level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
        level.playLocalSound(pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS,1.0f, 1.0f, true);
        for (int i = 0; i < 8; i++) {
            level.addParticle(ParticleTypes.TOTEM_OF_UNDYING, pos.getX(), pos.getY(), pos.getZ()+0.5d, 0, 0, 0.4d);
        }

        pContext.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
