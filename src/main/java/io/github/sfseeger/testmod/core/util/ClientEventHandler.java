package io.github.sfseeger.testmod.core.util;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.client.renderer.ToasterBlockEntityRenderer;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TestMod.MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                BlockEntityInit.TOASTER_BLOCK_ENTITY.get(),
                ToasterBlockEntityRenderer::new
        );
    }
}
