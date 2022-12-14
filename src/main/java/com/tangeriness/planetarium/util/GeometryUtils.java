package com.tangeriness.planetarium.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.util.math.Vec3d;

public class GeometryUtils {
    private static int GetMidpointIndex(Map<String, Integer> midpointIndices, List<Vec3d> vertices, int i0, int i1)
    {
        String edgeKey = Math.min(i0, i1) + "_" + Math.max(i0, i1);

        int midpointIndex = -1;

        if (!midpointIndices.containsKey(edgeKey)) {
            var v0 = vertices.get(i0);
            var v1 = vertices.get(i1);

            var midpoint = new Vec3d((v0.x + v1.x) / 2d, (v0.y + v1.y) / 2d, (v0.z + v1.z) / 2d);

            if (vertices.contains(midpoint))
                midpointIndex = vertices.indexOf(midpoint);
            else
            {
                midpointIndex = vertices.size();
                vertices.add(midpoint);
            }
            midpointIndices.put(edgeKey, midpointIndex);
        } else {
            midpointIndex = midpointIndices.get(edgeKey);
        }


        return midpointIndex;

    }
    public static void Subdivide(List<Vec3d> vertices, List<Integer> indices, boolean removeSourceTriangles)
    {
        Map<String, Integer> midpointIndices = Maps.newHashMap();
        List<Integer> newIndices = Lists.newArrayList();

        for (int i = 0; i < indices.size(); i += 3) {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            int m01 = GetMidpointIndex(midpointIndices, vertices, i0, i1);
            int m12 = GetMidpointIndex(midpointIndices, vertices, i1, i2);
            int m02 = GetMidpointIndex(midpointIndices, vertices, i2, i0);

            newIndices.addAll(Lists.newArrayList(
                i0,m01,m02,
                i1,m12,m01,
                i2,m02,m12,
                m02,m01,m12
            ));
        }
        
        indices.clear();
        indices.addAll(newIndices);
    }

    public static void Icosphere(List<Vec3d> vertices, List<Integer> indices, int subdivisions) {

        Icosahedron(vertices, indices);

        for (var i = 0; i < subdivisions; i++)
            Subdivide(vertices, indices, true);

        /// normalize vectors to "inflate" the icosahedron into a sphere.
        for (var i = 0; i < vertices.size(); i++)
            vertices.set(i, vertices.get(i).normalize());
    }
    public static void Icosahedron(List<Vec3d> vertices, List<Integer> indices)
    {
        
        indices.addAll(Lists.newArrayList(
            0,4,1,
            0,9,4,
            9,5,4,
            4,5,8,
            4,8,1,
            8,10,1,
            8,3,10,
            5,3,8,
            5,2,3,
            2,7,3,
            7,10,3,
            7,6,10,
            7,11,6,
            11,0,6,
            0,1,6,
            6,1,10,
            9,0,11,
            9,11,2,
            9,2,5,
            7,2,11
        ));
        
        var X = 0.525731112119133606f;
        var Z = 0.850650808352039932f;

        vertices.addAll(Lists.newArrayList(
            new Vec3d(-X, 0f, Z),
            new Vec3d(X, 0f, Z),
            new Vec3d(-X, 0f, -Z),
            new Vec3d(X, 0f, -Z),
            new Vec3d(0f, Z, X),
            new Vec3d(0f, Z, -X),
            new Vec3d(0f, -Z, X),
            new Vec3d(0f, -Z, -X),
            new Vec3d(Z, X, 0f),
            new Vec3d(-Z, X, 0f),
            new Vec3d(Z, -X, 0f),
            new Vec3d(-Z, -X, 0f) 
        ));
    }

}
