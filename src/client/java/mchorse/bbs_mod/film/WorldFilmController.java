package mchorse.bbs_mod.film;

import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.camera.clips.CameraClipContext;
import mchorse.bbs_mod.camera.clips.misc.AudioClientClip;
import mchorse.bbs_mod.camera.data.Position;
import mchorse.bbs_mod.utils.clips.Clip;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.Map;

public class WorldFilmController extends BaseFilmController
{
    protected CameraClipContext context;
    protected Position position = new Position();

    public int tick;
    public int duration;
    private int waitTicks;
    private boolean ready;

    public WorldFilmController(Film film)
    {
        super(film);

        this.createEntities();

        this.duration = film.camera.calculateDuration();
        this.context = new CameraClipContext();
        this.context.clips = film.camera;
    }

    private boolean isStartLoaded()
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world == null || this.context == null || this.context.clips == null)
        {
            return true;
        }

        Position sample = new Position();
        this.context.clipData.clear();
        this.context.setup(0, 0F);

        for (Clip clip : this.context.clips.getClips(0))
        {
            this.context.apply(clip, sample);
        }

        int chunkX = ((int) Math.floor(sample.point.x)) >> 4;
        int chunkZ = ((int) Math.floor(sample.point.z)) >> 4;

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
    public Map<String, Integer> getActors()
    {
        return BBSModClient.getFilms().actors.get(this.film.getId());
    }

    @Override
    public int getTick()
    {
        return this.tick;
    }

    @Override
    public boolean hasFinished()
    {
        return this.tick >= this.duration;
    }

    @Override
    public void update()
    {
        if (!this.ready)
        {
            this.waitTicks += 1;

            if (this.isStartLoaded() || this.waitTicks >= 60)
            {
                this.ready = true;
            }
            else
            {
                super.update();

                return;
            }
        }

        if (!this.paused)
        {
            this.tick += 1;
        }

        super.update();
    }

    @Override
    public void render(WorldRenderContext context)
    {
        super.render(context);

        int tick = Math.max(this.tick, 0);
        List<Clip> clips = this.context.clips.getClips(tick);

        if (clips.isEmpty())
        {
            return;
        }

        this.context.clipData.clear();
        this.context.setup(tick, context.tickCounter().getTickDelta(false));

        for (Clip clip : clips)
        {
            this.context.apply(clip, this.position);
        }

        this.context.currentLayer = 0;

        AudioClientClip.manageSounds(this.context);
    }

    @Override
    public void shutdown()
    {
        super.shutdown();

        this.context.shutdown();
    }
}