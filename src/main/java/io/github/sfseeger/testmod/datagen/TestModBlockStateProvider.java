package io.github.sfseeger.testmod.datagen;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.core.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class TestModBlockStateProvider extends BlockStateProvider {
    public TestModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TestMod.MODID, existingFileHelper);
    }

    private void registerSimpleBlock(DeferredBlock<? extends Block> deferredBlock) {
        Block block = deferredBlock.get();
        this.simpleBlockWithItem(block, this.models().cubeAll(deferredBlock.getRegisteredName(), blockTexture(block)));
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerSimpleBlock(BlockInit.TEST_MULTI_BLOCK);
    }
}
