package cn.ksmcbrigade.paunch.client;

import cn.ksmcbrigade.paunch.Paunch;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.ksmcbrigade.paunch.client.PaunchClient.PICK_A_STONE;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Paunch.MODID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaunchClientRegister {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event){
        event.register(PICK_A_STONE);
        Paunch.LOGGER.info("Key mappings registered.");
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Paunch.STONE_PROJECTILE.get(), ThrownItemRenderer::new);
        Paunch.LOGGER.info("Entity Renderer registered.");
    }
}
