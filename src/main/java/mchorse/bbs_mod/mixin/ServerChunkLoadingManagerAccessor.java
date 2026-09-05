package mchorse.bbs_mod.mixin;

import net.minecraft.server.network.ChunkFilter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerChunkLoadingManager.class)
public interface ServerChunkLoadingManagerAccessor
{
    @Invoker("sendWatchPackets")
    void bbs$sendWatchPackets(ServerPlayerEntity player, ChunkFilter chunkFilter);
}
