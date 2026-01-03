package com.leafia.init;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.FalloutConfigJSON.FalloutEntry;
import com.llib.exceptions.LeafiaDevFlaw;
import net.minecraft.block.material.Material;

import java.lang.reflect.Field;
import java.util.List;

public class FalloutConfigInit {
	public static final List<FalloutEntry> entries = FalloutConfigJSON.entries;
	public static void onInit() {
		removeByMatchingMaterial(Material.GRASS);
		removeByMatchingMaterial(Material.GROUND);
		removeByMatchingMaterial(Material.SAND);
		for (int i = 1; i <= 3; i++) {
			int m = 7-i;
			entries.add(FalloutEntry.builder()
					.addPrimary(ModBlocks.waste_sand.getStateFromMeta(m), 1)
					.max(i * 15+25)
					.opaque(true)
					.solid(true)
					.matchingMaterial(Material.SAND)
					.build());
			entries.add(FalloutEntry.builder()
					.addPrimary(ModBlocks.waste_dirt.getStateFromMeta(m), 1)
					.max(i * 15+25)
					.opaque(true)
					.solid(true)
					.matchingMaterial(Material.GROUND)
					.build());
			entries.add(FalloutEntry.builder()
					.addPrimary(ModBlocks.waste_earth.getStateFromMeta(m), 1)
					.max(i * 15+25)
					.opaque(true)
					.solid(true)
					.matchingMaterial(Material.GRASS)
					.build());
		}
	}
	static final Field matchesMaterial;
	static {
		try {
			matchesMaterial = FalloutEntry.class.getDeclaredField("matchesMaterial");
			matchesMaterial.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new LeafiaDevFlaw(e);
		}
	}
	static void removeByMatchingMaterial(Material material) {
		for (int i = 0; i < entries.size();) {
			FalloutEntry entry = entries.get(i);
			try {
				if (material.equals(matchesMaterial.get(entry))) {
					entries.remove(i);
					continue;
				}
			} catch (IllegalAccessException e) {
				throw new LeafiaDevFlaw(e);
			}
			i++;
		}
	}
}
