package com.tangeriness.planetarium.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tangeriness.planetarium.Planetarium;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SSBlocks {
    public static final Map<String, Block> BLOCKS = new HashMap<>();

    //public static final SSWaterBlock OCEAN_WATER = register("ocean_water",new SSWaterBlock(SSFluids.OCEAN_WATER, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0f).dropsNothing()));

    private static <T extends Block> T register(String name, T block) {
        BLOCKS.put(name, block);
        return block;
    }
    public static void register() {
        for (Entry<String, Block> block : BLOCKS.entrySet()) {
			Registry.register(Registries.BLOCK, Planetarium.id(block.getKey()), block.getValue());
		}
    }
}
