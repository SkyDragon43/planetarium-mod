package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.tangeriness.planetarium.client.world.starrysky.ClientStarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;

import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements StarrySkyHolder{

    @Unique private ClientStarrySky starrySky = new ClientStarrySky();
    
    @Override
    public StarrySky getStarrySky() {
        return this.starrySky;
    }
    
}
