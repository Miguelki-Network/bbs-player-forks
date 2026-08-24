package mchorse.bbs_mod.actions;

import mchorse.bbs_mod.BBSSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DamageControl
{
    private Map<BlockPos, BlockCapture> blocks = new LinkedHashMap<>();
    private List<Entity> entities = new ArrayList<>();

    private ServerWorld world;

    public int nested;
    public boolean enable;

    public DamageControl(ServerWorld world)
    {
        this.world = world;
        this.enable = BBSSettings.damageControl.get();
    }

    public void addBlock(BlockPos pos, BlockState state, BlockEntity entity)
    {
        if (!this.enable)
        {
            return;
        }

        BlockPos immutablePos = pos.toImmutable();

        if (this.blocks.containsKey(immutablePos))
        {
            return;
        }

        this.blocks.put(immutablePos, new BlockCapture(immutablePos, state, entity == null ? null : entity.createNbtWithId(this.world.getRegistryManager())));
    }

    public void addEntity(Entity entity)
    {
        if (!this.enable)
        {
            return;
        }

        this.entities.add(entity);
    }

    public void restore()
    {
        boolean prev = this.enable;
        this.enable = false;

        List<BlockCapture> blocksCopy = new ArrayList<>(this.blocks.values());
        List<Entity> entitiesCopy = new ArrayList<>(this.entities);

        this.blocks.clear();
        this.entities.clear();

        for (BlockCapture block : blocksCopy)
        {
            this.world.setBlockState(block.pos, block.lastState, 2);

            if (block.blockEntity != null)
            {
                BlockEntity blockEntity = BlockEntity.createFromNbt(block.pos, block.lastState, block.blockEntity, this.world.getRegistryManager());

                this.world.addBlockEntity(blockEntity);
            }
        }

        for (Entity entity : entitiesCopy)
        {
            if (!entity.isRemoved())
            {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }

        this.enable = prev;
    }

    private static class BlockCapture
    {
        public BlockPos pos;
        public BlockState lastState;
        public NbtCompound blockEntity;

        public BlockCapture(BlockPos pos, BlockState lastState, NbtCompound blockEntity)
        {
            this.pos = pos;
            this.lastState = lastState;
            this.blockEntity = blockEntity;
        }
    }
}
