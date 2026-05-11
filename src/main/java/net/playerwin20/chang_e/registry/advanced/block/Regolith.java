package net.playerwin20.chang_e.registry.advanced.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.joml.Vector3f;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.playerwin20.chang_e.registry.blockentity.DebugEngineBlockEntity;
import net.playerwin20.chang_e.registry.blockentity.RegolithBlockEntity;

public class Regolith extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    public static final MapCodec<Regolith> CODEC = simpleCodec(Regolith::new);

    //debug
    public static final Logger LOGGER = LogUtils.getLogger();

    public Regolith(Properties properties) {
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
        return new RegolithBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        //LOGGER.info("block incinerated");
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        LOGGER.info("i work");
        List<ItemStack> drops = new ArrayList<>();

        ItemStack stack = new ItemStack(this);

        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof RegolithBlockEntity cast) {
            CompoundTag tag = cast.saveWithFullMetadata(builder.getLevel().registryAccess());
            stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));

            String formatPreRaw = "chang_e.source.chang_e."+tag.getString("source_name");
            stack.set(DataComponents.CUSTOM_NAME,
                //TranslatableContents("CEpresetSourceName."+tag.getString("source_name"))+" Regolith"    ts no work
                Component.literal(Component.translatable(formatPreRaw)+" Regolith").withStyle(Style -> Style.withItalic(false)));
        }

        drops.add(stack);
        return drops;
    }

    // block item thing

    // behaviour

    //private ParticleOptions stepEff = new DustParticleOptions(new Vector3f(0.5f, 0.5f, 0.5f), 3.0f); 
    //private ParticleOptions stepEff = ParticleTypes.CLOUD;
    private ParticleOptions stepEff = new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState());
    private RandomSource rng = RandomSource.create();
    //private Direction lastEntityVel;

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        long cast = pos.asLong();

        if(level.isClientSide && entity.getDeltaMovement().length()>0.1) {
            //LOGGER.info(String.valueOf(entity.getDeltaMovement().length()));
            
            level.addParticle(
                stepEff,

                BlockPos.getX(cast) + rng.nextDouble() + entity.getDeltaMovement().x,
                BlockPos.getY(cast) + 1.0d + entity.getDeltaMovement().y,
                BlockPos.getZ(cast) + rng.nextDouble() + entity.getDeltaMovement().z,

                -entity.getDeltaMovement().x + (rng.nextDouble() - 0.5d)/16,
                -entity.getDeltaMovement().y + 0.25d,
                -entity.getDeltaMovement().z + (rng.nextDouble() - 0.5d)/16
            );
        }
        else {
            //LOGGER.info(String.valueOf(entity.getDeltaMovement().length()));
            if(entity.getDeltaMovement().length()>0.1){
                level.scheduleTick(pos, this, 1);
            }
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        long cast = pos.asLong();
        AreaEffectCloud cloud = new AreaEffectCloud(level, BlockPos.getX(cast)+0.5d, BlockPos.getY(cast)+1.0d, BlockPos.getZ(cast)+0.5d);
        cloud.setRadius(1.44f);
        cloud.setDuration(10); // ticks
        cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
        cloud.setParticle(stepEff);
        level.addFreshEntity(cloud);
    }
}
