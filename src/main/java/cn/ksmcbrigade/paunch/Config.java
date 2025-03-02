package cn.ksmcbrigade.paunch;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue HURT = BUILDER.defineInRange("hurt", 100, 10, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue WRESTLING_HURT = BUILDER.defineInRange("wrestling_hurt", 8, 0, Integer.MAX_VALUE);
    static final ForgeConfigSpec SPEC = BUILDER.build();

}
