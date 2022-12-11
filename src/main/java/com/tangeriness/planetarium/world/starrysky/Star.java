package com.tangeriness.planetarium.world.starrysky;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Star {
    public Vec3d pos;

    public Star(double x, double y, double z) {
        this.pos = new Vec3d(x, y, z);
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList position = new NbtList();
        position.add(NbtDouble.of(this.pos.x));
        position.add(NbtDouble.of(this.pos.y));
        position.add(NbtDouble.of(this.pos.z));
        nbt.put("pos", position);
        return nbt;
    }
    public static Star fromNbt(NbtCompound nbt) {
        NbtList pos = nbt.getList("pos", NbtType.DOUBLE);
        Star star = new Star(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
        return star;
    }
}
