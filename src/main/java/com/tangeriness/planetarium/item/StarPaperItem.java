package com.tangeriness.planetarium.item;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.tangeriness.planetarium.world.starrysky.Star;
import com.tangeriness.planetarium.world.starrysky.StarrySky;
import com.tangeriness.planetarium.world.starrysky.StarrySkyHolder;
import com.tangeriness.planetarium.world.starrysky.StarrySkyManager;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.report.ReporterEnvironment.Server;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class StarPaperItem extends Item {

    public StarPaperItem(Settings settings) {
        super(settings);
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (world != null) {
            StarrySky sky = ((StarrySkyHolder)world).getStarrySky();
            if (sky != null) {
                Star star = this.getStar(stack, sky);
                if (star != null) {
                    if (star.name != null) {
                        if (star.discovered) {
                            tooltip.add(Text.of("§7\""+star.name+"\" §a✔"));
                        } else {
                            tooltip.add(Text.of("§7\""+star.name+"\" §c❌"));
                        }
                        
                    } else {
                        tooltip.add(Text.of("§7Unamed Star"));
                    }
                } else {
                    tooltip.add(Text.of("§7UNKOWN"));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;
            StarrySkyManager skymng = StarrySkyManager.getOrCreateStarrySky(serverWorld);
            Star star = this.getStar(user.getStackInHand(hand), skymng.getStarrySky());
            if (star != null) {
                boolean discovered = skymng.discoverStar(star);
                if (discovered) {
                    user.sendMessage(Text.of("Discovered \"" + star.name + "\""));
                } else {
                    user.sendMessage(Text.of("\"" + star.name + "\" already Discovered!"));
                }
            }
        }
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClient) {
            if (!stack.hasNbt())
                this.assignStar(stack, ((StarrySkyHolder)world).getStarrySky(), world.getRandom());
        }
        
    }


    protected void assignStar(ItemStack stack, StarrySky sky, Random random) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (sky.size() > 0)
            nbt.putInt("StarId", random.nextInt(sky.size()));
    }



    protected Star getStar(ItemStack stack, StarrySky sky) {
        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();

            if (nbt.contains("StarId", NbtElement.INT_TYPE)) {
                int starId = nbt.getInt("StarId");
                return sky.getStar(starId);
            }
        }
        return null;
    }
}
