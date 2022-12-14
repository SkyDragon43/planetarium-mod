package com.tangeriness.planetarium.networking;

import com.tangeriness.planetarium.Planetarium;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class Packets {
    public static final Identifier ADJUST_SPYGLASS = new Identifier(Planetarium.MOD_ID, "adust_spyglass"); // CLIENT -> SERVER

    public static final Identifier REVEAL_STARS = new Identifier(Planetarium.MOD_ID, "reveal_stars"); // SERVER -> CLIENT
    public static final Identifier UPDATE_STAR = new Identifier(Planetarium.MOD_ID, "update_star"); // SERVER -> CLIENT



    
}
