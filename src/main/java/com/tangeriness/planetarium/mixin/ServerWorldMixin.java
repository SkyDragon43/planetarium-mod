package com.tangeriness.planetarium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tangeriness.planetarium.util.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements StarrySkyHolder{
    private StarrySkyManager starrySkyManager;

    private ServerWorld asServerWorld = ServerWorld.class.cast(this);

    @Inject(at = @At("TAIL"), method = "<init>()V")
	private void init(CallbackInfo info) {
		System.out.println("This line is printed by an example mod mixin! SERVERWORLD MIXIN!");

        this.starrySkyManager = StarrySkyManager.getOrCreateStarrySky(this.asServerWorld);
	}

    @Override
    public StarrySkyManager getStarrySky() {
        return this.starrySkyManager;
    }
}
