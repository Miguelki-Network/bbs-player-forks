package mchorse.bbs_mod.mixin.client;

import mchorse.bbs_mod.client.cinematic.ThirdPersonFilmController;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "cyclePerspective", at = @At("HEAD"), cancellable = true)
    private void bbs$blockPerspectiveCycle(CallbackInfo ci) {
        if (ThirdPersonFilmController.isActive()) {
            // Block changing perspective via F5 during cinematic playback
            ci.cancel();
        }
    }
}
