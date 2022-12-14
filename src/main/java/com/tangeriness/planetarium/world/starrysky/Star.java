package com.tangeriness.planetarium.world.starrysky;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Star {
    private Vec3d pos;
    private Vec3d normalPos;

    public String name;
    public float brightness;
    public Vec2f screenPos;
    public boolean discovered;

    public Star(double x, double y, double z) {
        this.pos = new Vec3d(x, y, z);
        this.normalPos = this.pos.normalize();

        this.name = null;
    }

    public void writeNbt(NbtCompound nbt) {
        NbtList position = new NbtList();
        position.add(NbtDouble.of(this.pos.x));
        position.add(NbtDouble.of(this.pos.y));
        position.add(NbtDouble.of(this.pos.z));
        nbt.put("Pos", position);
        
        if (this.name != null) {
            nbt.putString("Name", this.name);
        }
        nbt.putFloat("Brightness", this.brightness);
        nbt.putBoolean("Discovered", this.discovered);
    }
    public void readNbt(NbtCompound nbt) {
        NbtList pos = nbt.getList("Pos", NbtType.DOUBLE);
        this.pos = new Vec3d(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
        this.normalPos = this.pos.normalize();
        if (nbt.contains("Name", NbtElement.STRING_TYPE)) {
            this.name = nbt.getString("Name");
        }
        this.brightness = nbt.getFloat("Brightness");
        this.discovered = nbt.getBoolean("Discovered");
    }

    public void writeBuf(PacketByteBuf buf) {
        buf.writeDouble(this.pos.x);
        buf.writeDouble(this.pos.y);
        buf.writeDouble(this.pos.z);

        buf.writeBoolean(this.name != null);
        if (this.name != null) buf.writeString(this.name);

        buf.writeFloat(this.brightness);
        buf.writeBoolean(this.discovered);
    }
    public void readBuf(PacketByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        this.pos = new Vec3d(x, y, z);
        this.normalPos = this.pos.normalize();

        boolean hasName = buf.readBoolean();
        if (hasName) this.name = buf.readString();
        
        this.brightness = buf.readFloat();
        this.discovered = buf.readBoolean();
    }


    public Vec3d getPosition() {
        return this.pos;
    }
    public Vec3d getNormalPosition() {
        return this.normalPos;
    }



    public static class StarKnowledge {
        public void writeNbt(NbtCompound nbt) {
            
        }
        public void readNbt(NbtCompound nbt) {

        }
    }
}
