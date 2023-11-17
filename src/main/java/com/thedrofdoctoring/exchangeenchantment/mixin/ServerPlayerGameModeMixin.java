package com.thedrofdoctoring.exchangeenchantment.mixin;

import com.thedrofdoctoring.exchangeenchantment.Exchange;
import com.thedrofdoctoring.exchangeenchantment.ExchangeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow @Final protected ServerPlayer player;

    @Shadow protected ServerLevel level;

    //Block break would be better than a mixin, but unsuitable here as it gets called too early.

    @Inject(method = "removeBlock", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void removeBlock(BlockPos pos, boolean canHarvest, CallbackInfoReturnable<Boolean> cir) {
        Player player = this.player;
        Level level = this.level;
        BlockState state = level.getBlockState(pos);
        if(!player.getMainHandItem().isEnchanted() || player.isCrouching()) {
            return;
        }
        if(!(player.getMainHandItem().getEnchantmentLevel(Exchange.EXCHANGE_ENCHANTMENT.get()) > 0)) {
            return;
        }
        if(player.getOffhandItem().getItem() instanceof BlockItem) {
            ItemStack offHandItem = player.getOffhandItem();
            BlockItem item = (BlockItem) offHandItem.getItem();
            if(!item.getBlock().canSurvive(item.getBlock().defaultBlockState(), level, pos) || state.is(ExchangeBlockTags.noExchangeTag)) {
                return;
            }
            HitResult hitResult = player.pick(0,0, true);
            BlockState replacementState = item.getBlock().defaultBlockState();
            if(hitResult instanceof BlockHitResult blockHitResult) {
                replacementState = item.getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.OFF_HAND, offHandItem, blockHitResult));
            }
            boolean removed = state.onDestroyedByPlayer(this.level, pos, this.player, canHarvest, this.level.getFluidState(pos));
            level.setBlockAndUpdate(pos, replacementState);
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, replacementState));
            offHandItem.shrink(1);
            cir.setReturnValue(removed);
        }
    }
}
