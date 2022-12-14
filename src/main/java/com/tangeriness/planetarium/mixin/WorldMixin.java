package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;

import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin implements StarrySkyHolder {
    
}
