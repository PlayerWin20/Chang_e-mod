package net.playerwin20.chang_e.registry.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.playerwin20.chang_e.registry.ModBlockEntities;

public class DebugEngineBlockEntity extends BlockEntity {
    private String something;

    public DebugEngineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEBUG_ENGINE_BE.get(), pos, blockState);
    }
}
