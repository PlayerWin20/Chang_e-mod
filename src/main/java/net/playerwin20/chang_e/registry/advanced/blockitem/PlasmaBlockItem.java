package net.playerwin20.chang_e.registry.advanced.blockitem;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

//actually an igniter item
public class PlasmaBlockItem extends BlockItem {
    public PlasmaBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Fent and steel"); //non translated
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {

        InteractionResult result = super.place(context);
        if (result.consumesAction()) {

            context.getLevel().playSound(
                null,
                context.getClickedPos(),
                SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
            );
        }

        return result;
    }
}
