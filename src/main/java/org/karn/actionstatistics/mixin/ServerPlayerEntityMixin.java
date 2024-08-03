package org.karn.actionstatistics.mixin;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.karn.actionstatistics.ActionStatistics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "updateInput", at = @At("HEAD"))
    private void karn$updateInput(float sidewaysSpeed, float forwardSpeed, boolean jumping, boolean sneaking, CallbackInfo ci) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println("Player input: " + sidewaysSpeed + " " + forwardSpeed + " " + jumping + " " + sneaking);
        if(forwardSpeed > 0)
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_FORWARD_STAT);
        if(forwardSpeed < 0)
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_BACKWARD_STAT);
        if(sidewaysSpeed > 0)
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_RIGHT_STAT);
        if(sidewaysSpeed < 0)
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_LEFT_STAT);
    }

    @Inject(method = "dropSelectedItem", at = @At("TAIL"))
    private void karn$onDropItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if(entireStack)
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_DROP_ALL);
        else
            ((ServerPlayerEntity) (Object) this).incrementStat(ActionStatistics.PRESS_DROP);
    }
}
