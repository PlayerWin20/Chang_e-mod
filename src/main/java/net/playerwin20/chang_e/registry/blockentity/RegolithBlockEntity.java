package net.playerwin20.chang_e.registry.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.playerwin20.chang_e.registry.ModBlockEntities;

public class RegolithBlockEntity extends BlockEntity {
    private int hex_color = 0xffffff;
    private String source_name = "Blank";
    private int[] composition = new int[0];

    public RegolithBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.REGOLITH_BE.get(), pos, blockState);
    }

    // write setter

    public void setHexColor(int newHexColor) {
        this.hex_color = newHexColor;
        sync();
    }

    public void setSourceName(String newSourceName) {
        this.source_name = newSourceName;
        sync();
    }

    public void setComposition(int[] newCompositionArray) {
        this.composition = newCompositionArray;
        sync();
    }

    private void sync() {
        setChanged();

        if(level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // fetch getter

    public int getHexColor() {
        return this.hex_color;
    }

    public String getSourceName() {
        return this.source_name;
    }

    public int[] getComposition() {
        return this.composition;
    }

    //setup

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("hex_color", hex_color);
        tag.putString("source_name", source_name);
        tag.putIntArray("composition", composition);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        hex_color = tag.getInt("hex_color");
        source_name = tag.getString("source_name");
        composition = tag.getIntArray("composition");

        sync();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }
}
