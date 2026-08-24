package mchorse.bbs_mod.mixin;

import mchorse.bbs_mod.morphing.IMorphProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin
{
    @Inject(method = "shouldRender(D)Z", at = @At("HEAD"), cancellable = true)
    public void bbs$onShouldRender(double distance, CallbackInfoReturnable<Boolean> info)
    {
        if ((Object) this instanceof PlayerEntity || (Object) this instanceof IMorphProvider)
        {
            info.setReturnValue(distance < (512D * 512D));
        }
    }
}
