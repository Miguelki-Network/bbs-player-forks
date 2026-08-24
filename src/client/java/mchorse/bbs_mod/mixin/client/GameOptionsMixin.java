package mchorse.bbs_mod.mixin.client;

import mchorse.bbs_mod.client.cinematic.ThirdPersonFilmController;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin
{
    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    private void bbs$blockSetPerspective(Perspective perspective, CallbackInfo ci)
    {
        if (ThirdPersonFilmController.isActive() && !ThirdPersonFilmController.isAllowPerspectiveChange())
        {
            ci.cancel();
        }
    }
}
