package com.tangeriness.planetarium.client.render.sky;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.core.util.NameUtil;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Overwrite;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.init.SSItems;
import com.tangeriness.planetarium.util.GeometryUtils;
import com.tangeriness.planetarium.util.MathUtils;
import com.tangeriness.planetarium.util.NameUtils;
import com.tangeriness.planetarium.world.starrysky.Star;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;

import io.netty.util.internal.MathUtil;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class StarrySkyRenderer{

    private VertexBuffer starsBuffer;
    private VertexBuffer skyBuffer;

    private static final Identifier EARTH = new Identifier(Planetarium.MOD_ID, "textures/environment/earth.png");


    public StarrySkyRenderer() {
        this.renderStars();
        this.renderSky();
    }

    private void renderSky() {
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (this.skyBuffer != null) {
            this.skyBuffer.close();
        }
        this.skyBuffer = new VertexBuffer();
        BufferBuilder.BuiltBuffer builtBuffer = this.renderSky(bufferBuilder);
        this.skyBuffer.bind();
        this.skyBuffer.upload(builtBuffer);
        VertexBuffer.unbind();
    }
    private BufferBuilder.BuiltBuffer renderSky(BufferBuilder buffer) {
        Random random = Random.create(new Date().getTime());

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        // buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR_TEXTURE);
        double toRadians = Math.PI/180D;
        double radius = 100;
        for (int i = 0; i < 360; i+=10) {

            for (int j = -80; j <= 80; j += 10) {
                
                double lon = i * toRadians;
                double lat = j * toRadians;
                Vec3d p = MathUtils.sphereToXYZ(lat, lon, 100);
                Vec3d p2 = MathUtils.sphereToXYZ(lat + 10 * toRadians, lon, 100);
                float r = (j + 80f)/160f;
                if (j < 80) {
                    buffer.vertex(p.x, p.y, p.z).color(1f, 1f, 1f, 1f).next();
                    buffer.vertex(p2.x, p2.y, p2.z).color(1f, 1f, 1f, 1f).next();
                }
                
                
                //buffer.vertex(p.x, p.x, p.x).color(1f, 1f, 1f, 1f).next();

                p = MathUtils.sphereToXYZ(lat, lon, 100);
                p2 = MathUtils.sphereToXYZ(lat, lon + 10 * toRadians, 100);
                buffer.vertex(p.x, p.y, p.z).color(1f, 1f, 1f, 1f).next();
                buffer.vertex(p2.x, p2.y, p2.z).color(1f, 1f, 1f, 1f).next();
            }
        }
        // buffer.vertex(0, 0, 0).color(1f, 0f, 0f, 1f).next();
        // buffer.vertex(100, 0, 0).color(1f, 0f, 0f, 1f).next();
        // buffer.vertex(0, 0, 0).color(0f, 1f, 0f, 1f).next();
        // buffer.vertex(0, 100, 0).color(0f, 1f, 0f, 1f).next();
        // buffer.vertex(0, 0, 0).color(0f, 0f, 1f, 1f).next();
        // buffer.vertex(0, 0, 100).color(0f, 0f, 1f, 1f).next();
        // List<Vec3d> vertices = Lists.newArrayList();
        // List<Integer> indices = Lists.newArrayList();
        // GeometryUtils.Icosphere(vertices, indices, 4);

        // Vec2f first = Vec2f.ZERO;
        // for (int i = 0; i < indices.size(); i+=3) {
        //     Vec3d v1 = vertices.get(indices.get(i));
        //     Vec3d v2 = vertices.get(indices.get(i + 1));
        //     Vec3d v3 = vertices.get(indices.get(i + 2));
            
        //     float r = random.nextFloat();
        //     float g = random.nextFloat();
        //     float b = random.nextFloat();
            
                
        //     Vec2f tex1 = getTexCoord(v1);
        //     Vec2f tex2 = getTexCoord(v2);
        //     Vec2f tex3 = getTexCoord(v3);
        //     float x1 = tex1.x;
        //     float y1 = tex1.y;
        //     float x2 = tex2.x;
        //     float y2 = tex2.y;
        //     float x3 = tex3.x;
        //     float y3 = tex3.y;



        //     if (x2 - x1 > 0.5 && x3 - x1 > 0.5) x1 += 1;
        //     if (x1 - x2 > 0.5 && x3 - x2 > 0.5) x2 += 1;
        //     if (x2 - x3 > 0.5 && x1 - x3 > 0.5) x3 += 1;

        //     if (x1 - x2 > 0.5 && x1 - x3 > 0.5) x1 -= 1;
        //     if (x2 - x1 > 0.5 && x2 - x3 > 0.5) x2 -= 1;
        //     if (x3 - x1 > 0.5 && x3 - x2 > 0.5) x3 -= 1;

        //     if (tex1.y == 0 || tex1.y == 1) x1 = (tex2.x + tex3.x) / 2;
        //     if (tex2.y == 0 || tex2.y == 1) x2 = (tex1.x + tex3.x) / 2;
        //     if (tex3.y == 0 || tex3.y == 1) x3 = (tex1.x + tex2.x) / 2;
            
            
        //     buffer.vertex(v1.x * 100, v1.y * 100, v1.z * 100).color(1f, 1f, 1f, 1f).texture(x1, y1).next();
        //     buffer.vertex(v2.x * 100, v2.y * 100, v2.z * 100).color(1f, 1f, 1f, 1f).texture(x2, y2).next();
        //     buffer.vertex(v3.x * 100, v3.y * 100, v3.z * 100).color(1f, 1f, 1f, 1f).texture(x3, y3).next();
        // }

        return buffer.end();
    }
    private Vec2f getTexCoord(Vec3d vec)
    {
        float u = (float)(Math.atan2(vec.z,vec.x) / (2f * Math.PI));
        float v = (float)(Math.asin(vec.y) / Math.PI) + 0.5f;
        float theta = (float) ((Math.atan2(vec.x, vec.z) / MathHelper.PI) / 2.f + 0.5f);
        float phi = (float) ((Math.asin(-vec.y) / (MathHelper.PI / 2.f)) / 2.f + 0.5f);
        return new Vec2f(u, v);
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
        MinecraftClient client = MinecraftClient.getInstance();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Random random = Random.create(new Date().getTime());

        if (client.world != null) {
            StarrySky starrysky = ((StarrySkyHolder)client.world).getStarrySky();

            Planetarium.LOGGER.info("RENDERING STARS");
                
                
            for (Star star : starrysky) {
                Vec3d pos = star.getPosition();
                Vec3d normalPos = star.getNormalPosition();

                double d = pos.x;
                double e = pos.y;
                double f = pos.z;
                double g = 0.15f + 0.1f;
                double h = d * d + e * e + f * f;
                //if (!(h < 1.0) || !(h > 0.01)) continue;
                double size = 1-h;
                
                double j = normalPos.x * 100.0;
                double k = normalPos.y * 100.0;
                double l = normalPos.z * 100.0;
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
                    buffer.vertex(j + af * size, k + ag * size, l + ah * size).color(1f, 1f, 1f, (float)((size * star.brightness) * 0.9 + 0.1)).next();
                }
            }
        }
        return buffer.end();
        // Random random = Random.create(new Date().getTime());
        // buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        
        // for (int i = 0; i < 5000; ++i) {
        //     double starBrightness = 0.05;
        //     if (i < 50) {
        //         starBrightness = 1;
        //     } else if (i < 250) {
        //         starBrightness = .6;
        //     } else if (i < 1000) {
        //         starBrightness = 0.3;
        //     } else if (i < 2500) {
        //         starBrightness = 0.1;
        //     }
        //     double d = random.nextFloat() * 2.0f - 1.0f;
        //     double e = random.nextFloat() * 2.0f - 1.0f;
        //     double f = random.nextFloat() * 2.0f - 1.0f;
        //     double g = 0.15f + random.nextFloat() * 0.1f;
        //     double h = d * d + e * e + f * f;
        //     if (!(h < 1.0) || !(h > 0.01)) continue;
        //     h = 1.0 / Math.sqrt(h);
        //     double j = (d *= h) * 100.0;
        //     double k = (e *= h) * 100.0;
        //     double l = (f *= h) * 100.0;
        //     double m = Math.atan2(d, f);
        //     double n = Math.sin(m);
        //     double o = Math.cos(m);
        //     double p = Math.atan2(Math.sqrt(d * d + f * f), e);
        //     double q = Math.sin(p);
        //     double r = Math.cos(p);
        //     double s = random.nextDouble() * Math.PI * 2.0;
        //     double t = Math.sin(s);
        //     double u = Math.cos(s);
        //     for (int v = 0; v < 4; ++v) {
        //         double ab;
        //         double w = 0.0;
        //         double x = (double)((v & 2) - 1) * g;
        //         double y = (double)((v + 1 & 2) - 1) * g;
        //         double z = 0.0;
        //         double aa = x * u - y * t;
        //         double ac = ab = y * u + x * t;
        //         double ad = aa * q + 0.0 * r;
        //         double ae = 0.0 * q - aa * r;
        //         double af = ae * n - ac * o;
        //         double ag = ad;
        //         double ah = ac * n + ae * o;
        //         buffer.vertex(j + af, k + ag, l + ah).color(1f, 1f, 1f, (float)(starBrightness)).next();
        //     }
        // }
        // return buffer.end();
    }



    public void render(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, float brightness) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextRenderer textren = minecraft.textRenderer;

        float i = 1.0f - minecraft.world.getRainGradient(tickDelta);

        if (Math.random() < 0.01) {
            this.renderStars();
        }
        brightness *= 2 * i;
        matrices.pop();
        matrices.push();
        float skyAngle = minecraft.world.getSkyAngle(tickDelta) * 360.0f;
        Quaternionf starRotation = RotationAxis.POSITIVE_X.rotationDegrees(-45f).mul(RotationAxis.POSITIVE_Y.rotationDegrees(-skyAngle));

        boolean isUsingSpyglass = minecraft.player.isUsingSpyglass() && minecraft.player.getActiveItem().isOf(SSItems.SPYGLASS) && minecraft.options.getPerspective().isFirstPerson();
        
        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-skyAngle));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
        matrices.multiply(starRotation);

        RenderSystem.setShaderColor(1f, 1f, 1f, brightness * 0.25f);
        //RenderSystem.setShaderTexture(0, EARTH);
        RenderSystem.lineWidth(1000f);
        BackgroundRenderer.clearFog();
        
        // RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT); // Not GL_CLAMP_TO_EDGE.
        // RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT); // Not GL_CLAMP_TO_EDGE.
        // RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        // RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        if (isUsingSpyglass) {
            this.skyBuffer.bind();
            this.skyBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionColorProgram());
            VertexBuffer.unbind();
        }
        

        
        RenderSystem.setShaderColor(brightness, brightness, brightness, brightness);
    
        
        // matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)Math.random() * 20.0f));
        
        
        this.starsBuffer.bind();
        this.starsBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionColorProgram());
        VertexBuffer.unbind();

        matrices.pop();
        matrices.push();
        Matrix4f m2 = new Matrix4f(projectionMatrix).mul(new Matrix4f(matrices.peek().getPositionMatrix()));

        int starWidth = textren.getWidth("✧");
        Quaternionf textRotation = new Quaternionf(starRotation).invert().mul(camera.getRotation());
        if (minecraft.world != null) {
            StarrySky starrysky = ((StarrySkyHolder)minecraft.world).getStarrySky();
            if (isUsingSpyglass) {
                for (Star star : starrysky) {
                    
                    if (star.discovered) {
                        String text = star.name;
                        Vec3d normalPos = star.getNormalPosition();
                        Vec3d pos = star.getPosition();

                        matrices.push();
                    
                        matrices.multiply(starRotation);

                        double d = pos.x;
                        double e = pos.y;
                        double f = pos.z;
                        double h = d * d + e * e + f * f;
                        float size = (float)(1-h);

                        matrices.translate(normalPos.x * 100, normalPos.y * 100, normalPos.z * 100);
                        matrices.multiply(textRotation);
                        
                        matrices.scale(-0.3f, -.3f, .3f);
                        matrices.scale(size, size, size);
                        // float h = -textren.getWidth(text) / 2;
                        textren.draw(matrices, "✧", -starWidth/2f + 0.15f, -3.8f, 16777215);

                        float phase = 0;
                        float halfPi = (float) (Math.PI/2);
                        float xPhase = MathHelper.cos(phase) * 6f;
                        int textWidth = textren.getWidth(text);
                        textren.draw(matrices, text, (xPhase < 0 ? -textWidth : 0) + xPhase, MathHelper.sin(phase) * 10f -3.8f, 16777215);

                        matrices.pop();
                    }
    
                    
                    //textren.draw(matrices, null, i, skyAngle, 0)
                    //star.screenPos = MathUtils.worldToScreen(star.pos.multiply(100), new Matrix4f(matrices.peek().getPositionMatrix()), new Matrix4f(projectionMatrix), 1, 1);
                }
            }
            
        }

        

        
        
    }
}
