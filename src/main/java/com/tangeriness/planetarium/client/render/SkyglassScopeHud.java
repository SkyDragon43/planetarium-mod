package com.tangeriness.planetarium.client.render;

import com.tangeriness.planetarium.world.starrysky.Star;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class SkyglassScopeHud {
    public static void render(MatrixStack stack, float delta) {
        MinecraftClient client = MinecraftClient.getInstance(); 
        TextRenderer renderer = client.textRenderer;
        int width = client.getWindow().getWidth()/2;
        int height = client.getWindow().getHeight()/2;
        if (client.world != null) {
            // StarrySky sky = ((StarrySkyHolder)client.world).getStarrySky();
            // for (Star star : sky) {
            //     if (star.discovered) {
            //         renderer.draw(stack, star.name, star.screenPos.x * width, star.screenPos.y * height, 0xffffff);
            //     }
            // }
        }
        
        // renderer.draw(stack, "OWO FOrnITEnITE", 0, 0, 0xffffff);
        // renderer.draw(stack, "This is red", 0, 100, 0xff0000);
        // renderer.draw(stack, "center", width/2, height/2, 0xff0000);
    }
}
