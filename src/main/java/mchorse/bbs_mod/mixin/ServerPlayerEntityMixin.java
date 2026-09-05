package mchorse.bbs_mod.mixin;

import mchorse.bbs_mod.BBSMod;
import mchorse.bbs_mod.actions.types.item.ItemDropActionClip;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mchorse.bbs_mod.camera.IBBSCameraPlayer;

import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements IBBSCameraPlayer
{
    @Unique
    private Vec3d bbs$cameraPosition;

    @Override
    public void bbs$setCameraPosition(Vec3d pos)
    {
        this.bbs$cameraPosition = pos;
    }

    @Override
    public Vec3d bbs$getCameraPosition()
    {
        return this.bbs$cameraPosition;
    }

    @Override
    public boolean bbs$hasCameraPosition()
    {
        return this.bbs$cameraPosition != null;
    }

    @Override
    public void bbs$clearCameraPosition()
    {
        this.bbs$cameraPosition = null;
    }
    @Inject(method = "dropItem", at = @At("RETURN"))
    public void onDropItem(CallbackInfoReturnable<ItemEntity> info)
    {
        ItemEntity entity = info.getReturnValue();

        if (entity != null)
        {
            ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
            BBSMod.getActions().addAction(player, () ->
            {
                ItemDropActionClip actionClip = new ItemDropActionClip();
                Vec3d velocity = entity.getVelocity();
                Vec3d pos = entity.getPos();

                actionClip.velocityX.set((float) velocity.x);
                actionClip.velocityY.set((float) velocity.y);
                actionClip.velocityZ.set((float) velocity.z);
                actionClip.posX.set(pos.x);
                actionClip.posY.set(pos.y);
                actionClip.posZ.set(pos.z);
                actionClip.itemStack.set(entity.getStack().copy());

                return actionClip;
            });
        }
    }
}