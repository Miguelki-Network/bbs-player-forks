package mchorse.bbs_mod.mixin.client;

import mchorse.bbs_mod.client.cinematic.ThirdPersonFilmController;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererToggleMixin {

    @Inject(method = "togglePerspective", at = @At("HEAD"), cancellable = true)
    private void bbs$blockTogglePerspective(CallbackInfo ci) {
        if (ThirdPersonFilmController.isActive()) {
            ci.cancel();
        }
    }
}
