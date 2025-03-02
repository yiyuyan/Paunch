package cn.ksmcbrigade.paunch.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record PickMessage(BlockPos pos,boolean shoot) {
    public static void encode(PickMessage msg,FriendlyByteBuf buf){
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.shoot);
    }

    public static PickMessage decode(FriendlyByteBuf buf){
        return new PickMessage(buf.readBlockPos(),buf.readBoolean());
    }
}
