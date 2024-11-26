package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.blockentities.GeneratorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TestMod.MODID);

    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "generator_block_entity",
            // The block entity type.
            () -> BlockEntityType.Builder.of(
                    GeneratorBlockEntity::new,
                    BlockInit.GENERATOR_BLOCK.get()
            ).build(null)
    );
}
