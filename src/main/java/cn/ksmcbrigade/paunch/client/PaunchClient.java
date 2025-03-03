package cn.ksmcbrigade.paunch.client;

import cn.ksmcbrigade.paunch.Paunch;
import cn.ksmcbrigade.paunch.network.PickMessage;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Paunch.MODID,value = Dist.CLIENT)
public class PaunchClient {

    public static final KeyMapping PICK_A_STONE = new KeyMapping("Pick a stone", InputConstants.KEY_F,KeyMapping.CATEGORY_GAMEPLAY);

    @SubscribeEvent
    public static void onInput(InputEvent.Key event){
        Minecraft MC = Minecraft.getInstance();
        if(PICK_A_STONE.isDown() && MC.level!=null && MC.hitResult instanceof BlockHitResult blockHitResult && Paunch.match(MC.level.getBlockState(blockHitResult.getBlockPos()).getBlock())){
            Paunch.CHANNEL.sendToServer(new PickMessage(blockHitResult.getBlockPos(),false));
            if(MC.player!=null) MC.player.displayClientMessage(Component.literal("Right click to throw the block."),true);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){
        Minecraft MC = Minecraft.getInstance();
        if(GLFW.glfwGetMouseButton(MC.getWindow().getWindow(),1)==1 && MC.player!=null && Paunch.match(Block.byItem(MC.player.getOffhandItem().getItem()))){
            Paunch.CHANNEL.sendToServer(new PickMessage(BlockPos.ZERO,true));
        }
    }

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
