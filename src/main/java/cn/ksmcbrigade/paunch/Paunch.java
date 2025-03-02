package cn.ksmcbrigade.paunch;

import cn.ksmcbrigade.paunch.entity.StoneProjectile;
import cn.ksmcbrigade.paunch.network.PickMessage;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Paunch.MODID)
public class Paunch {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "paunch";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Entity types which will all be registered under the "paunch" namespace
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<StoneProjectile>> STONE_PROJECTILE = ENTITY_TYPES.register("stone_projectile", () -> EntityType.Builder.<StoneProjectile>of(StoneProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation(MODID,"stone_projectile").toString()));

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID,"pick"),()->"340",(s)->true,(s)->true);

    public Paunch() {
        MinecraftForge.EVENT_BUS.register(this);
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CHANNEL.registerMessage(0, PickMessage.class,PickMessage::encode,PickMessage::decode,(msg,context)->{
            ServerPlayer player = context.get().getSender();
            if(player!=null){
                if (msg.shoot() && Block.byItem(player.getOffhandItem().getItem()) != Blocks.AIR) {
                    StoneProjectile projectile = new StoneProjectile(player.serverLevel(),player,player.getOffhandItem().getItem());
                    projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.01F, 4.5F, 0.0F);
                    player.serverLevel().addFreshEntity(projectile);
                    player.getOffhandItem().shrink(1);
                }
                else if(!msg.shoot()){
                    player.getInventory().add(BlockItem.byBlock(player.serverLevel().getBlockState(msg.pos()).getBlock()).getDefaultInstance());
                }
            }
           context.get().setPacketHandled(true);
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        LOGGER.info("Paunch Mod Loaded.");
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event){
        if(event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof Player player1 && player1.distanceTo(player)<=1){
            event.setAmount(event.getAmount()/2f+Config.WRESTLING_HURT.get());
        }
    }
}
