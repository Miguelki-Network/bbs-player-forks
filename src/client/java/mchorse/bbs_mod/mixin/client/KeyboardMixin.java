package mchorse.bbs_mod.mixin.client;

import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.client.BBSRendering;
import mchorse.bbs_mod.client.cinematic.ThirdPersonFilmController;
import net.minecraft.client.Keyboard;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin
{
    private static final int GLFW_KEY_F5 = 294;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info)
    {
        // Block F5 perspective toggle during cinematic playback
        if (ThirdPersonFilmController.isActive() && key == GLFW_KEY_F5 && action == 1)
        {
            info.cancel();
            return;
        }

        BBSRendering.lastAction = action;
    }

    @Inject(method = "onKey", at = @At("TAIL"))
    public void onOnEndKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info)
    {
        BBSModClient.onEndKey(window, key, scancode, action, modifiers, info);
    }
}