package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    MinecraftClient client;
    @Shadow
    private float fovMultiplier;
    @Shadow
    private float lastFovMultiplier;

    @Overwrite
    private void updateFovMultiplier() {
        float f = 1.0f;
        if (this.client.getCameraEntity() instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)this.client.getCameraEntity();
            f = abstractClientPlayerEntity.getFovMultiplier();
        }
        this.lastFovMultiplier = this.fovMultiplier;
        this.fovMultiplier += (f - this.fovMultiplier) * 0.5f;
        if (this.fovMultiplier > 1.5f) {
            this.fovMultiplier = 1.5f;
        }
        if (this.fovMultiplier < 0.01f) {
            this.fovMultiplier = 0.01f;
        }
    }
}
