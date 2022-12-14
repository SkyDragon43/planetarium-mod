package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.tangeriness.planetarium.init.SSItems;
import com.tangeriness.planetarium.networking.Packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

@Mixin(Mouse.class)
public class MouseMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;
    @Final
    @Shadow
    private SmoothUtil cursorXSmoother;
    @Final
    @Shadow
    private SmoothUtil cursorYSmoother;
    @Shadow
    private double lastMouseUpdateTime;
    @Shadow
    public boolean isCursorLocked() {return false;}

    @Redirect(method = "onMouseScroll(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
    private void spyglassScrollMixin(PlayerInventory inv, double amount) {
        if (this.client.player.isUsingSpyglass() && this.client.player.getActiveItem().isOf(SSItems.SPYGLASS)) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(amount);
            
            ClientPlayNetworking.send(Packets.ADJUST_SPYGLASS, buf);
        } else {
            inv.scrollInHotbar(amount);
        }
    }

    @Overwrite
    public void updateMouse() {
        double l;
        double k;
        double d = GlfwUtil.getTime();
        double e = d - this.lastMouseUpdateTime;
        this.lastMouseUpdateTime = d;
        if (!this.isCursorLocked() || !this.client.isWindowFocused()) {
            this.cursorDeltaX = 0.0;
            this.cursorDeltaY = 0.0;
            return;
        }
        double f = this.client.options.getMouseSensitivity().getValue() * (double)0.6f + (double)0.2f;
        double g = f * f * f;
        double h = g * 8.0;
        if (this.client.options.smoothCameraEnabled) {
            double i = this.cursorXSmoother.smooth(this.cursorDeltaX * h, e * h);
            double j = this.cursorYSmoother.smooth(this.cursorDeltaY * h, e * h);
            k = i;
            l = j;
        } else if (this.client.options.getPerspective().isFirstPerson() && this.client.player.isUsingSpyglass()) {
            float mult = 1;
            ItemStack activeItem = this.client.player.getActiveItem();
            if (activeItem.isOf(SSItems.SPYGLASS)) {
                float zoom = SSItems.SPYGLASS.getZoom(activeItem);
                mult = zoom/.3f;
            }

            this.cursorXSmoother.clear();
            this.cursorYSmoother.clear();
            k = this.cursorDeltaX * g * mult;
            l = this.cursorDeltaY * g * mult;
        } else {
            this.cursorXSmoother.clear();
            this.cursorYSmoother.clear();
            k = this.cursorDeltaX * h;
            l = this.cursorDeltaY * h;
        }
        this.cursorDeltaX = 0.0;
        this.cursorDeltaY = 0.0;
        int m = 1;
        if (this.client.options.getInvertYMouse().getValue().booleanValue()) {
            m = -1;
        }
        this.client.getTutorialManager().onUpdateMouse(k, l);
        if (this.client.player != null) {
            this.client.player.changeLookDirection(k, l * (double)m);
        }
    }
}
