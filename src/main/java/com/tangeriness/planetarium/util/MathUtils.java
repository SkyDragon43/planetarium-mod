package com.tangeriness.planetarium.util;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4d;

import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
    public static Vec3d sphereToXYZ(double lat, double lon, double r) {
        double x = r * Math.cos(lat) * Math.cos(lon);
        double z = r * Math.cos(lat) * Math.sin(lon);
        double y = r * Math.sin(lat);
        return new Vec3d(x, y, z);
    }

    public static Vec2f worldToScreen(Vec3d point3D, Matrix4f viewMatrix, Matrix4f projectionMatrix, int width, int height) {


      Matrix4f MVP = projectionMatrix.mul(viewMatrix);
      Vector4d v0 = new Vector4d( point3D.x, point3D.y, point3D.z, 1 ).mul(MVP);

      Vec3d v = new Vec3d(v0.x / v0.w, v0.y / v0.w, v0.z);

      // Matrix4f viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
      // //transform world to clipping coordinates
      //   Vector3d v1 = new Vector3d(point3D.x, point3D.y, point3D.z);
      //   v1 = v1.mulProject(viewProjectionMatrix);
      double winX = ((( v.x + 1)/2) * width );
      //we calculate -point3D.getY() because the screen Y axis is
      //oriented top->down 
      double winY = ((( 1 - v.y)/2) * height );
      if (v.z < 0) {
        return Vec2f.ZERO;
      } else {
        return new Vec2f((float)(winX), (float)(winY));
      }
      
}
}
