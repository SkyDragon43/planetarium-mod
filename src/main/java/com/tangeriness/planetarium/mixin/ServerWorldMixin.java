package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tangeriness.planetarium.server.world.ServerStarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements StarrySkyHolder{
    @Unique private StarrySkyManager starrySkyManager;
    @Unique private ServerStarrySky starrySky;

    private ServerWorld asServerWorld = ServerWorld.class.cast(this);

    @Inject(at = @At("TAIL"), method = "<init>()V")
	private void init(CallbackInfo info) {
		System.out.println("This line is printed by an example mod mixin! SERVERWORLD MIXIN!");

        this.starrySky = new ServerStarrySky();
        this.starrySkyManager = StarrySkyManager.getOrCreateStarrySky(this.asServerWorld);
	}

    @Override
    public StarrySky getStarrySky() {
        return this.starrySky;
    }
}
