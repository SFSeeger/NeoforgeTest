package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.common.blocks.FunkyBlock;
import io.github.sfseeger.testmod.common.blocks.GeneratorBlock;
import io.github.sfseeger.testmod.common.blocks.ToasterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.sfseeger.testmod.TestMod.MODID;
import static net.minecraft.world.level.block.state.BlockBehaviour.*;

public class BlockInit {
    // Create a Deferred Register to hold Blocks which will all be registered under the "testmod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // Creates a new Block with the id "testmod:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", Properties.of().mapColor(MapColor.STONE));

    public static final DeferredBlock<GeneratorBlock> GENERATOR_BLOCK =
            BLOCKS.register("generator", GeneratorBlock::new);

    public static final DeferredBlock<FunkyBlock> FUNKY_BLOCK = BLOCKS.register("funky_block", FunkyBlock::new);
    public static final DeferredBlock<Block> TOASTER_BLOCK = BLOCKS.register("toaster", ToasterBlock::new);
}
