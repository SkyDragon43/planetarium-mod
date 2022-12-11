package com.tangeriness.planetarium.client.render.sky;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;

public class StarrySkyRenderer{

    private VertexBuffer starsBuffer;


    public StarrySkyRenderer() {
        this.renderStars();
    }


    private void renderStars() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (this.starsBuffer != null) {
            this.starsBuffer.close();
        }
        this.starsBuffer = new VertexBuffer();
        BufferBuilder.BuiltBuffer builtBuffer = this.renderStars(bufferBuilder);
        this.starsBuffer.bind();
        this.starsBuffer.upload(builtBuffer);
        VertexBuffer.unbind();
    }
    private BufferBuilder.BuiltBuffer renderStars(BufferBuilder buffer) {
        Random random = Random.create(10842L);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 500; ++i) {
            double d = random.nextFloat() * 2.0f - 1.0f;
            double e = random.nextFloat() * 2.0f - 1.0f;
            double f = random.nextFloat() * 2.0f - 1.0f;
            double g = 0.15f + random.nextFloat() * 0.1f;
            double h = d * d + e * e + f * f;
            if (!(h < 1.0) || !(h > 0.01)) continue;
            h = 1.0 / Math.sqrt(h);
            double j = (d *= h) * 100.0;
            double k = (e *= h) * 100.0;
            double l = (f *= h) * 100.0;
            double m = Math.atan2(d, f);
            double n = Math.sin(m);
            double o = Math.cos(m);
            double p = Math.atan2(Math.sqrt(d * d + f * f), e);
            double q = Math.sin(p);
            double r = Math.cos(p);
            double s = random.nextDouble() * Math.PI * 2.0;
            double t = Math.sin(s);
            double u = Math.cos(s);
            for (int v = 0; v < 4; ++v) {
                double ab;
                double w = 0.0;
                double x = (double)((v & 2) - 1) * g;
                double y = (double)((v + 1 & 2) - 1) * g;
                double z = 0.0;
                double aa = x * u - y * t;
                double ac = ab = y * u + x * t;
                double ad = aa * q + 0.0 * r;
                double ae = 0.0 * q - aa * r;
                double af = ae * n - ac * o;
                double ag = ad;
                double ah = ac * n + ae * o;
                buffer.vertex(j + af, k + ag, l + ah).color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 255).next();
            }
        }
        return buffer.end();
    }



    public void render(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, float brightness) {
        RenderSystem.setShaderColor(brightness, brightness, brightness, brightness);
        BackgroundRenderer.clearFog();
        this.starsBuffer.bind();
        
        this.starsBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionColorProgram());
        VertexBuffer.unbind();
    }
}
