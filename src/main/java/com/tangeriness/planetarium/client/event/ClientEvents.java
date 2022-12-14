package com.tangeriness.planetarium.client.event;

import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.client.render.SkyglassScopeHud;
import com.tangeriness.planetarium.init.SSItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    private static Event<ScreenMouseEvents.AllowMouseScroll> allowMouseScroll;

    private static MinecraftClient minecraft = MinecraftClient.getInstance(); 

    public static void registerEvents() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            allowMouseScroll = ScreenMouseEvents.allowMouseScroll(screen);
            
            registerScreenEvents();
        });

        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
            if (minecraft.player != null && minecraft.player.isUsingSpyglass()) {
                if (minecraft.player.getActiveItem().isOf(SSItems.SPYGLASS)) {
                    SkyglassScopeHud.render(matrixStack, delta);
                }
            }
        });
    }

    private static void registerScreenEvents() {
    }
}
