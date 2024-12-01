package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.blockentities.FunkyBlockEntity;
import io.github.sfseeger.testmod.common.blockentities.GeneratorBlockEntity;
import io.github.sfseeger.testmod.common.blockentities.ToasterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TestMod.MODID);

    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "generator_block_entity",
            () -> BlockEntityType.Builder.of(
                    GeneratorBlockEntity::new,
                    BlockInit.GENERATOR_BLOCK.get()
            ).build(null)
    );
    public static final Supplier<BlockEntityType<ToasterBlockEntity>> TOASTER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "toaster_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ToasterBlockEntity::new,
                            BlockInit.TOASTER_BLOCK.get()
                    ).build(null)
    );
    public static final Supplier<BlockEntityType<FunkyBlockEntity>> FUNKY_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "funky_block_entity",
                    () -> BlockEntityType.Builder.of(
                            FunkyBlockEntity::new,
                            BlockInit.FUNKY_BLOCK.get()
                    ).build(null)
            );
}

