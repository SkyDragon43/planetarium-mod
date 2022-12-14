package com.tangeriness.planetarium.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tangeriness.planetarium.Planetarium;
import com.tangeriness.planetarium.item.ConstellationPaperItem;
import com.tangeriness.planetarium.item.RetractableSpyglassItem;
import com.tangeriness.planetarium.item.StarChartItem;
import com.tangeriness.planetarium.item.StarPaperItem;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class SSItems {
    public static final Map<String, Item> ITEMS = new HashMap<>();

    //public static final BucketItem OCEAN_WATER_BUCKET = register("ocean_water_bucket", new BucketItem(SSFluids.OCEAN_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ItemGroup.MISC)));

    public static final RetractableSpyglassItem SPYGLASS = register("retractable_spyglass", new RetractableSpyglassItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final StarChartItem STAR_CHART = register("star_chart", new StarChartItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
    public static final StarPaperItem STAR_PAPER = register("star_paper", new StarPaperItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
    public static final ConstellationPaperItem CONSTELLATION_PAPER = register("constellation_paper", new ConstellationPaperItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));


    private static <T extends Item> T register(String name, T item) {
        ITEMS.put(name, item);
        return item;
    }
    public static void register() {
        for (Entry<String, Item> item : ITEMS.entrySet()) {
			Registry.register(Registries.ITEM, Planetarium.id(item.getKey()), item.getValue());
		}
    }
}
