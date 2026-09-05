package mchorse.bbs_mod.camera;

import net.minecraft.util.math.Vec3d;

public interface IBBSCameraPlayer
{
    public void bbs$setCameraPosition(Vec3d pos);

    public Vec3d bbs$getCameraPosition();

    public boolean bbs$hasCameraPosition();

    public void bbs$clearCameraPosition();
}
