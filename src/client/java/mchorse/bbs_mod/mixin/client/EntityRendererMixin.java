package mchorse.bbs_mod.mixin.client;

import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.entity.ActorEntity;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.forms.forms.MobForm;
import mchorse.bbs_mod.morphing.IMorphProvider;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity>
{
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public void onRenderLabelIfPresent(CallbackInfo info)
    {
        if (FormUtilsClient.getCurrentForm() instanceof MobForm form && form.isPlayer())
        {
            info.cancel();
        }
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public void bbs$onShouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir)
    {
        if (BBSModClient.getCameraController().getCurrent() != null)
        {
            if (entity instanceof PlayerEntity || entity instanceof ActorEntity || entity instanceof IMorphProvider)
            {
                Box box = entity.getBoundingBox().expand(2D).offset(-x, -y, -z);

                cir.setReturnValue(frustum.isVisible(box));
            }
        }
    }
}