package net.playerwin20.chang_e.registry.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.playerwin20.chang_e.registry.ModBlockEntities;

public class RegolithBlockEntity extends BlockEntity {
    private int[] composition;
    private String sourceName;

    public RegolithBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.REGOLITH_BE.get(), pos, blockState);
    }
}
