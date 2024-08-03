package org.karn.actionstatistics.mixin;

import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.karn.actionstatistics.ActionStatistics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.karn.actionstatistics.ActionStatistics.LEFT_CLICK_STAT;

@Mixin(net.minecraft.server.network.ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandler {
    @Shadow
    public ServerPlayerEntity player;
    public boolean isDropItem = false;
    /*
    @Inject(method = "onPlayerInput", at = @At("HEAD"))
    private void karn$onPlayerInput(PlayerInputC2SPacket packet, CallbackInfo ci) {
        if(packet.isJumping())
            player.incrementStat(ActionStatistics.PRESS_JUMP_STAT);
        //if(packet.isSneaking())
            //player.incrementStat(ActionStatistics.PRESS_SNEAK_STAT);
        if(packet.getForward() > 0)
            player.incrementStat(ActionStatistics.PRESS_FORWARD_STAT);
        if(packet.getForward() < 0)
            player.incrementStat(ActionStatistics.PRESS_BACKWARD_STAT);
        if(packet.getSideways() > 0)
            player.incrementStat(ActionStatistics.PRESS_RIGHT_STAT);
        if(packet.getSideways() < 0)
            player.incrementStat(ActionStatistics.PRESS_LEFT_STAT);
        System.out.println(packet);
    }*/

    @Inject(method = "onClientCommand", at = @At("TAIL"))
    private void karn$onClientCommand(ClientCommandC2SPacket packet, CallbackInfo ci) {
        if(packet.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY)
            player.incrementStat(ActionStatistics.PRESS_SNEAK_STAT);
        if(packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING)
            player.incrementStat(ActionStatistics.PRESS_SPRINT_STAT);
        if (packet.getMode() == ClientCommandC2SPacket.Mode.START_FALL_FLYING)
            player.incrementStat(ActionStatistics.PRESS_FALL_FLYING);
        if (packet.getMode() == ClientCommandC2SPacket.Mode.START_RIDING_JUMP)
            player.incrementStat(ActionStatistics.PRESS_RIDING_JUMP);
    }

    @Inject(method = "onPlayerAction", at = @At("HEAD"))
    private void karn$onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (packet.getAction() == PlayerActionC2SPacket.Action.DROP_ITEM || packet.getAction() == PlayerActionC2SPacket.Action.DROP_ALL_ITEMS)
            isDropItem = true;
        if (packet.getAction() == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND)
            player.incrementStat(ActionStatistics.HAND_SWAP_STAT);
        //if (packet.getAction() == PlayerActionC2SPacket.Action.DROP_ITEM)
            //player.incrementStat(ActionStatistics.PRESS_DROP);
        //if (packet.getAction() == PlayerActionC2SPacket.Action.DROP_ALL_ITEMS)
            //player.incrementStat(ActionStatistics.PRESS_DROP_ALL);
        if (packet.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM)
            player.incrementStat(ActionStatistics.RELEASE_USE);
    }

    @Inject(method = "onHandSwing", at = @At("TAIL"), cancellable = true)
    private void karn$onHandSwing(HandSwingC2SPacket packet, CallbackInfo ci) {
        Vec3d eyePos = player.getEyePos();

        double blockreachDistance = this.player.getBlockInteractionRange();
        Vec3d viewVector = player.getRotationVector().multiply(blockreachDistance);
        Vec3d vec32 = eyePos.add(viewVector);
        BlockHitResult result = player.getWorld().raycast(new RaycastContext(eyePos, vec32, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));

        if (result != null && result.getType() != HitResult.Type.MISS) {
            return;
        }

        double entityreachDistance = this.player.getEntityInteractionRange();
        viewVector = player.getRotationVector().multiply(entityreachDistance);
        Box aabb = player.getBoundingBox().expand(viewVector.x,viewVector.y,viewVector.z).expand(1);
        EntityHitResult result2 = ProjectileUtil.raycast(player, eyePos, vec32, aabb, (entity) -> !entity.isSpectator(),entityreachDistance*entityreachDistance);

        if (result2 != null && result2.getType() != HitResult.Type.MISS) {
            return;
        }

        if (!isDropItem) {
            player.incrementStat(LEFT_CLICK_STAT);
        }
        isDropItem = false;
    }
}
