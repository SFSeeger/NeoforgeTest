package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.common.blockentities.GeneratorBlockEntity;
import io.github.sfseeger.testmod.common.blockentities.ToasterBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInit {
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        // GeneratorBlockEntity
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BlockEntityInit.GENERATOR_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity instanceof GeneratorBlockEntity be ? be.getEnergyStorage(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityInit.GENERATOR_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity instanceof GeneratorBlockEntity be ? be.getItemHandler(side) : null
        );

        // ToasterBlockEntity
//        event.registerBlockEntity(
//                Capabilities.ItemHandler.BLOCK,
//                BlockEntityInit.TOASTER_BLOCK_ENTITY.get(),
//                (blockEntity, side) -> blockEntity instanceof ToasterBlockEntity be ? be.getItemHandler(side) : null
//        );
    }
}
