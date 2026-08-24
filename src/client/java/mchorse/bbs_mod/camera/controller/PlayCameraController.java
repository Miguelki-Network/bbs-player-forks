package mchorse.bbs_mod.camera.controller;

import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.camera.Camera;
import mchorse.bbs_mod.camera.data.Position;
import mchorse.bbs_mod.utils.clips.Clip;
import mchorse.bbs_mod.utils.clips.Clips;

import net.minecraft.client.MinecraftClient;

public class PlayCameraController extends CameraWorkCameraController
{
    private int ticks;
    private int duration;
    private int waitTicks;
    private boolean ready;
    private Position initialPosition = new Position();

    public PlayCameraController(Clips clips)
    {
        super();

        this.setWork(clips);
        this.duration = clips.calculateDuration();

        if (this.context != null && this.context.clips != null)
        {
            this.context.clipData.clear();
            this.context.setup(0, 0F);

            for (Clip clip : this.context.clips.getClips(0))
            {
                this.context.apply(clip, this.initialPosition);
            }
        }
    }

    private boolean isStartLoaded()
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world == null)
        {
            return true;
        }

        int chunkX = ((int) Math.floor(this.initialPosition.point.x)) >> 4;
        int chunkZ = ((int) Math.floor(this.initialPosition.point.z)) >> 4;

        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                if (!mc.world.getChunkManager().isChunkLoaded(chunkX + dx, chunkZ + dz))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void setup(Camera camera, float transition)
    {
        this.apply(camera, this.ticks, transition);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.ready)
        {
            this.waitTicks += 1;

            if (this.isStartLoaded() || this.waitTicks >= 60)
            {
                this.ready = true;
            }
            else
            {
                return;
            }
        }

        this.ticks += 1;

        if (this.ticks >= this.duration)
        {
            BBSModClient.getCameraController().remove(this);
        }
    }
}