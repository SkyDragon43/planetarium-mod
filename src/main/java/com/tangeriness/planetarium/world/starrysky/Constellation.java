package com.tangeriness.planetarium.world.starrysky;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtType;
import net.minecraft.util.math.Vec3d;

public class Constellation {
    private List<Edge> edges;

    public String name;


    public Constellation(String name) {
        this.edges = Lists.newArrayList();
        this.name = name;
    }


    public boolean addEdge(Star a, Star b) {
        Edge edge = new Edge(a, b);
        if (!this.edges.contains(edge)) {
            this.edges.add(edge);
            return true;
        }
        return false;
    }


    public void writeNbt(NbtCompound nbt, StarrySky sky) {
        List<Integer> edges = Lists.newArrayList();

        for (Edge edge : this.edges) {
            edges.add(sky.getStarId(edge.a));
            edges.add(sky.getStarId(edge.b));
        }
        nbt.putIntArray("Edges", edges);
        nbt.putString("Name", this.name);
    }
    public void readNbt(NbtCompound nbt, StarrySky sky) {
        int[] edges = nbt.getIntArray("Edges");
        List<Edge> newEdges = Lists.newArrayList();
        for (int i = 0; i < edges.length; i += 2) {
            int ai = i;
            int bi = i + 1;
            Star a = sky.getStar(ai);
            Star b = sky.getStar(bi);
            if (a != null && b != null) {
                Edge edge = new Edge(a, b);
                newEdges.add(edge);
            }
        }
        this.edges = newEdges;
        this.name = nbt.getString("Name");
    }


    public static class Edge {
        public Star a;
        public Star b;

        public Edge(Star a, Star b) {
            this.a = a;
            this.b = b;
        }


        @Override
        public boolean equals(Object arg0) {
            if (arg0 instanceof Edge) {
                Edge edge = (Edge) arg0;
                return (edge.a == this.a && edge.b == this.b) || (edge.b == this.a && edge.a == this.b);
            }
            return false;
        }
    }
}
