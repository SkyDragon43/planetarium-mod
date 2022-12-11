package com.tangeriness.planetarium.mixin;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.client.render.sky.StarrySkyRenderer;

import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MatrixUtil;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Final
	@Shadow
	private MinecraftClient client;

    @Shadow
    @Nullable
    private ClientWorld world;
   
    @Shadow
    @Nullable
    private VertexBuffer starsBuffer;

    @Unique private StarrySkyRenderer starRenderer;


    @Shadow
    private void renderEndSky(MatrixStack matrices) {}
    @Shadow
    private boolean method_43788(Camera camera) {return false;}


    @Inject(method = "<init>", at = @At("TAIL"))
    private void worldRenderer(MinecraftClient client, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BufferBuilderStorage bufferBuilders, CallbackInfo callback) {
        this.starRenderer = new StarrySkyRenderer();
    }
    
    @Overwrite
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
    @Overwrite
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

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_23787(F)F"))
    private float starBrightness(ClientWorld world, float tickDelta) {
        return 0.0f;
    }
    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_23787(F)F", shift = At.Shift.AFTER))
    private void renderSky(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo callback) {



        if (this.client.world != null) {
			// DimensionRenderingRegistry.SkyRenderer renderer = DimensionRenderingRegistry.getSkyRenderer(world.getRegistryKey());

			// if (renderer != null) {
			// 	renderer.render(context);
			// 	info.cancel();
			// }
		}
        float brightness = this.world.method_23787(tickDelta) * 1;
        this.starRenderer.render(matrices, projectionMatrix, tickDelta, camera, brightness);
        // RenderSystem.setShaderColor(brightness, brightness, brightness, brightness);
        // BackgroundRenderer.clearFog();
        // this.starsBuffer.bind();
        
        // this.starsBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionColorProgram());
        // VertexBuffer.unbind();
        runnable.run();

    }
}
