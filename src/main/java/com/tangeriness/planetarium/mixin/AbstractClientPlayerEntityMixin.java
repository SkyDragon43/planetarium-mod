package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.init.SSItems;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    private AbstractClientPlayerEntity asAbstractClientPlayerEntity = AbstractClientPlayerEntity.class.cast(this);

    @Inject(
        method = "getFovMultiplier()F", 
        at = @At(value = "RETURN", ordinal = 0),
        cancellable = true
    )
    private void spyglassFov(CallbackInfoReturnable<Float> callback) {
        ItemStack activeItem = this.asAbstractClientPlayerEntity.getActiveItem();
        if (activeItem.isOf(SSItems.SPYGLASS)) {
            float zoom = SSItems.SPYGLASS.getZoom(activeItem);
            callback.setReturnValue(zoom);
        }
    }
}
