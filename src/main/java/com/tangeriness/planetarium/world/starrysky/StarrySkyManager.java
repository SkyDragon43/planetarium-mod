package com.tangeriness.planetarium.world.starrysky;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixer;
import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.networking.Packets;
import com.tangeriness.planetarium.server.world.ServerStarrySky;
import com.tangeriness.planetarium.util.NameUtils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class StarrySkyManager extends PersistentState{
    public static final String STARRY_SKY = Planetarium.MOD_ID+":"+"starry_sky";

    private boolean loaded = false;
    private final ServerStarrySky sky;
    private final ServerWorld world;

    public StarrySkyManager(ServerWorld world) {
        this.world = world;
        this.sky = (ServerStarrySky) ((StarrySkyHolder)world).getStarrySky();
        this.setDirty(true);
    }


    public void generateStars(Random random) {
        this.sky.clear();
        List<String> names = Lists.newArrayList();
        List<Star> newStars = Lists.newArrayList();
        for (int i = 0; i < 5000; i++) {
            double x = random.nextFloat() * 2.0f - 1.0f;
            double y = random.nextFloat() * 2.0f - 1.0f;
            double z = random.nextFloat() * 2.0f - 1.0f;

            double h = x * x + y * y + z * z;
            if (!(h < 1.0) || !(h > 0.01)) continue;
            Star star = new Star(x, y, z);
            star.brightness = random.nextFloat() * 0.5f + 0.5f;
            String name = NameUtils.generateStarName(random);
            while (names.contains(name))
                name = NameUtils.generateStarName(random);
            star.name = name;
            newStars.add(star);
        }
        this.sky.setStars(newStars);
        this.setDirty(true);
    }

    public boolean discoverStar(Star star) {
        if (this.sky.contains(star)) {
            if (!star.discovered) {
                star.discovered = true;
                for (ServerPlayerEntity player : PlayerLookup.world(this.world)) {
                    ServerPlayNetworking.send(player, Packets.UPDATE_STAR, this.createUpdateStarPacket(star));
                }
                this.markDirty();
                return true;
            }
        }
        return false;
    }

    public StarrySky getStarrySky() {
        return this.sky;
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound skyComp = new NbtCompound();
        this.sky.writeNbt(skyComp);
        nbt.put("Sky", skyComp);

        nbt.putBoolean("Loaded", this.loaded);
        return nbt;
    }

    public static String nameFor(RegistryEntry<DimensionType> dimensionTypeEntry) {
        return STARRY_SKY;
    }

    public PacketByteBuf createRevealStarsPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(this.world.getRegistryKey().getValue());
        buf.writeInt(this.sky.size());
        for (Star star : this.sky) {
            star.writeBuf(buf);
        }
        return buf;
    }
    public PacketByteBuf createUpdateStarPacket(Star star) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(this.world.getRegistryKey().getValue());
        boolean contains = this.sky.contains(star);
        buf.writeBoolean(contains);
        if (contains) {
            buf.writeInt(this.sky.getStarId(star));
            star.writeBuf(buf);
        }
        return buf;
    }
    

    public void updateClients() {
        PacketByteBuf packet = this.createRevealStarsPacket();
        for (ServerPlayerEntity player : this.world.getPlayers()) {
            ServerPlayNetworking.send(player, Packets.REVEAL_STARS, packet);
        }
    }

    public static StarrySkyManager fromNbt(ServerWorld world, NbtCompound nbt) {
        StarrySkyManager starManager = new StarrySkyManager(world);
        // raidManager.nextAvailableId = nbt.getInt("NextAvailableID");
        // raidManager.currentTime = nbt.getInt("Tick");
        NbtCompound skyNbt = nbt.getCompound("Sky");
        starManager.sky.readNbt(skyNbt);

        starManager.loaded = nbt.getBoolean("Loaded");
        return starManager;
    }

    public void Load() {
        if (!this.loaded) {
            Planetarium.LOGGER.info("Generating the starry night sky.");
            this.loaded = true;

            this.generateStars(Random.create(this.world.getTime()));

            this.markDirty();

            this.updateClients();
        }
    }
    public void Reload() {
        this.loaded = false;
        this.Load();
    }

    public static StarrySkyManager getOrCreateStarrySky(ServerWorld world) {
        StarrySkyManager mng = world.getPersistentStateManager().getOrCreate(nbt -> StarrySkyManager.fromNbt(world, nbt), () -> new StarrySkyManager(world), StarrySkyManager.nameFor(world.getDimensionEntry()));
        return mng;
    }
}
