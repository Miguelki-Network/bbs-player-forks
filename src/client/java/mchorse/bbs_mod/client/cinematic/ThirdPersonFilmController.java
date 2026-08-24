package mchorse.bbs_mod.client.cinematic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

/**
 * Forces third-person camera during film playback and restores afterwards.
 * Avoids ghost player rendering by leveraging existing third-person model rendering.
 */
public class ThirdPersonFilmController {
    private static Perspective previous;
    private static boolean active;

    public static void begin() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.options == null) return;
        if (active) return;
        previous = mc.options.getPerspective();
        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        active = true;
    }

    public static void end() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.options == null) return;
        if (!active) return;
        if (previous != null) {
            mc.options.setPerspective(previous);
        }
        previous = null;
        active = false;
    }

    public static boolean isActive() {
        return active;
    }
}
