package com.tangeriness.planetarium.world.starrysky;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tangeriness.planetarium.world.starrysky.Star.StarKnowledge;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public abstract class StarrySky implements Iterable<Star>{

    protected List<Star> stars;
    protected List<StarKnowledge> knowledge;

    protected List<Constellation> constellations;

    protected StarrySky() {
        this.stars = Lists.newArrayList();
        this.knowledge = Lists.newArrayList();

        this.constellations = Lists.newArrayList();
    }


    public void setStars(Collection<Star> stars) {
        this.stars.clear();
        this.knowledge.clear();
        this.stars.addAll(stars);
        for (int i = 0; i < this.stars.size(); i++) {
            this.knowledge.add(new StarKnowledge());
        }
    }


    public Star getStar(int id) {
        if (id >= 0 && id < this.stars.size())
            return this.stars.get(id);
        return null;
    }
    public int getStarId(Star star) {
        if (this.stars.contains(star))
            return this.stars.indexOf(star);
        return -1;
    }
    
    public StarKnowledge getKnowledge(Star star) {
        int index = this.getStarId(star);
        return getKnowledge(index);
    }
    public StarKnowledge getKnowledge(int id) {
        if (id >= 0 && id < this.stars.size())
            return this.knowledge.get(id);
        return null;
    }

    public int size() { return this.stars.size(); }
    public boolean isEmpty() { return this.stars.isEmpty(); }
    public boolean contains(Star star) { return this.stars.contains(star); }
    // public boolean add(Star star) { return this.stars.add(star); }
    // public boolean remove(Star star) { return this.stars.remove(star); } 
    public void clear() { this.knowledge.clear(); this.stars.clear(); }


    public void writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < this.stars.size(); i++) {
            Star star = this.stars.get(i);
            StarKnowledge knowledge = this.knowledge.get(i);
            NbtCompound knowledgeNbtCompound = new NbtCompound();
            knowledge.writeNbt(knowledgeNbtCompound);

            NbtCompound starNbtCompound = new NbtCompound();
            star.writeNbt(starNbtCompound);

            NbtCompound nbtCompound = new NbtCompound();

            nbtCompound.put("star", starNbtCompound);
            nbtCompound.put("data", knowledgeNbtCompound);
            nbtList.add(nbtCompound);
        }
        nbt.put("Stars", nbtList);

        NbtList constellationList = new NbtList();
        for (int i = 0; i < this.constellations.size(); i++) {
            Constellation c = this.constellations.get(i);
            NbtCompound ct = new NbtCompound();
            c.writeNbt(ct, this);
            constellationList.add(ct);
        }
        nbt.put("Constellations", nbtList);
    }
    public void readNbt(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("Stars", NbtElement.COMPOUND_TYPE);
        List<Star> stars = Lists.newArrayList();
        List<StarKnowledge> knowledges = Lists.newArrayList();
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound starComp = (NbtCompound) nbtList.get(i);

            Star star = new Star(0, 0, 0);
            star.readNbt(starComp.getCompound("star"));
            stars.add(star);

            StarKnowledge knowledge = new StarKnowledge();
            knowledge.readNbt(starComp.getCompound("data"));
            knowledges.add(knowledge);
        }
        this.stars = stars;
        this.knowledge = knowledges;

        NbtList cl = nbt.getList("Constellations", NbtElement.COMPOUND_TYPE);
        List<Constellation> newConstellation = Lists.newArrayList();
        for (int i = 0; i < cl.size(); i++) {
            NbtCompound constComp = (NbtCompound) cl.get(i);

            Constellation con = new Constellation("unamed");
            con.readNbt(constComp, this);
            newConstellation.add(con);
        }
        this.constellations = newConstellation;
    }


    @Override
    public Iterator<Star> iterator() {
        return this.stars.iterator();
    }
}
