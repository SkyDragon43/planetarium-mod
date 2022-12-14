package com.tangeriness.planetarium.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

// getString(ctx, "string")
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
// word()
import static com.mojang.brigadier.arguments.StringArgumentType.word;
 // literal("foo")
import static net.minecraft.server.command.CommandManager.literal;

import org.joml.Random;

import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

// argument("bar", word())
import static net.minecraft.server.command.CommandManager.argument;
// Import everything in the CommandManager
import static net.minecraft.server.command.CommandManager.*;

public class PCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            dispatcher.register(
                literal("regenstarrysky")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(context -> {
                        // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                        context.getSource().sendMessage(Text.literal("Generating starry sky"));
                        StarrySkyManager mng = StarrySkyManager.getOrCreateStarrySky(context.getSource().getWorld());
                        mng.Reload();
                        return 1;
                    }
                )
            )
        );
    }
}
