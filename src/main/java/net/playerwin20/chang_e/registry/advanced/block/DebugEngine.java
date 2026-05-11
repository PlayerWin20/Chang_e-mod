package net.playerwin20.chang_e.registry.advanced.block;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

//import dev.ryanhcode.sable;

public class DebugEngine extends BaseEntityBlock  {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    public static final MapCodec<Regolith> CODEC = simpleCodec(Regolith::new);

    //debug
    public static final Logger LOGGER = LogUtils.getLogger();

    public DebugEngine(Properties properties) {
        super(properties);
    }

    @Override 
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override 
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos,
        @org.jetbrains.annotations.Nullable Direction direction) {
        return true;
    }

    // behaviour

    private static boolean Chang_eOrionPropulsion(BlockPos pos, ServerLevel level) {
        long cast = pos.asLong();

        level.sendParticles(
            ParticleTypes.FLASH,
            (double) BlockPos.getX(cast) + 0.5,
            (double) BlockPos.getY(cast) + 0.5,
            (double) BlockPos.getZ(cast) + 0.5,
            1,
            0,
            0,
            0,
            0
        );

        ((ServerLevel) level).playLocalSound(pos, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.MASTER, 1.0f, 1.0f, false);

        return true;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(level.hasNeighborSignal(pos)) {
            Chang_eOrionPropulsion(pos, level);
        }
        super.tick(state, level, pos, random);
    }
}
