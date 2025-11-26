package com.leafia.contents;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.CoreComponent;
import com.hbm.main.MainRegistry;
import com.leafia.AddonBase;
import com.leafia.contents.building.mixed.BlockMixedConcrete;
import com.leafia.contents.building.pinkdoor.BlockPinkDoor;
import com.leafia.contents.machines.powercores.dfc.AddonCoreComponent;
import com.leafia.contents.network.spk_cable.SPKCableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.Map.Entry;

public class AddonBlocks {
	public static final List<Block> ALL_BLOCKS = new ArrayList();

	public enum GenericBlockResistance {
		// Concrete mayhem
		CONCRETE(84),			// used to be 2400 lmao
		CONCRETE_BRICKS(96),	// used to be 3600 lmao
		RE_BRICKS(180),			// used to be 4800 lmao
		REBARPILLAR(108),		// used to be 2400 lmao

		// Early game
		COMPOUND_MESH(240),		// used to be 6000 lmao

		// Reinforced vanilla
		RE_GLASS(15),			// used to be 120 lmao
		DN_STONE(60),			// used to be 1800 lmao
		RE_LIGHTGEM(48),		// easter egg: the texture glowstone is actually called lightgem :o
		RE_SANDSTONE(24),		// used to be 240 lmfao its just a sand
		RE_RSLAMP(48),			// used to be 180 lmao

		//////////////////////////////////////////////////////////////////////////////////////

		// Asbestos
		ASB_CONCRETE(90),

		// Ducrete
		DUCRETE(300),
		DUCRETE_BRICKS(450),
		RE_DUCRETE(600),

		// Specialized bricks
		BRICKS_LIGHT(12),
		BRICKS_FIREBRICKS(21),
		BRICKS_ASBESTOS(600),
		BRICKS_OBSIDIAN(72),	// man 4800

		// Absurd crappies
		CMB_TILE(3E+3),
		RE_CMB_BRICKS(3E+4),

		// Ores
		ORE_VANILLA(5),
		ORE_CLUSTER(6),
		ORE_HEAVY(8),
		ORE_HEAVY_CLUSTER(9),

		PWR_CASING(135),
		PWR_INSIDE(27),
		;
		public final float v;
		GenericBlockResistance(float resistance) {
			this.v = resistance * 5/3; // counter-spaghetti minecraft coding
		}
		GenericBlockResistance(double resistance) {
			this.v = (float)resistance * 5/3; // counter-spaghetti minecraft coding
		}
		public float half() {
			return this.v /2;
		}
		public float third() {
			return this.v /3;
		}
		public float quart() {
			return this.v /4;
		}
		public float quint() {
			return this.v /5;
		}
		public float triquart() {
			return this.v * 3/4;
		}
	}

	public static final Block door_fuckoff = new BlockPinkDoor(Material.WOOD, "door_fuckoff").setHardness(3);
	public static final Block spk_cable = new SPKCableBlock(Material.IRON, "spk_cable").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
	public static final Block dfc_reinforced = new AddonCoreComponent(Material.IRON, "dfc_reinforced").setHardness(5.0F).setResistance(50.0F).setCreativeTab(MainRegistry.machineTab);
	public static final Block dfc_exchanger = new AddonCoreComponent(Material.IRON, "dfc_exchanger").setHardness(5.0F).setResistance(50.0F).setCreativeTab(MainRegistry.machineTab);
	public static final Block dfc_cemitter = new AddonCoreComponent(Material.IRON, "dfc_cemitter").setHardness(5.0F).setResistance(50.0F).setCreativeTab(MainRegistry.machineTab);

	static boolean mixed_dummy = MixedConcretes.dummy;
	public static class MixedConcretes {
		static boolean dummy = false;
		public static final Map<String,String> idMap = new HashMap<>();
		public static final Map<String,BlockMixedConcrete> blocks = new HashMap<>();
		static {
			idMap.put("brick_concrete","brick");
			idMap.put("brick_concrete_mossy","brickm");
			idMap.put("brick_concrete_cracked","brickc");
			idMap.put("brick_concrete_broken","brickb");
			idMap.put("ducrete_brick","ducrete");
			idMap.put("concrete","raw");
			for (EnumDyeColor dye : EnumDyeColor.values())
				idMap.put("concrete_"+dye.getName(),dye.getName());
			Set<Entry<String,String>> set = idMap.entrySet();
			Entry<String,String>[] entries = new Entry[set.size()];
			int index = 0;
			for (Entry<String,String> entry : set)
				entries[index++] = entry;
			for (int i = 0; i < entries.length-1; i++) {
				for (int j = i+1; j < entries.length; j++) {
					String id = "concrete__mixed_"+entries[i].getValue()+"_"+entries[j].getValue();
					blocks.put(id,new BlockMixedConcrete(id,entries[i].getKey(),entries[j].getKey()));
				}
			}
		}
	}

	private static void modifyBlockParams() {
		ModBlocks.dfc_core.setResistance(65000000);
		ModBlocks.dfc_emitter.setResistance(50);
		ModBlocks.dfc_receiver.setResistance(50);
		ModBlocks.dfc_injector.setResistance(50);
		ModBlocks.dfc_stabilizer.setResistance(50);
		ModBlocks.deco_aluminium.setResistance(30);
		ModBlocks.deco_asbestos.setResistance(30);
		ModBlocks.deco_beryllium.setResistance(30);
		ModBlocks.deco_lead.setResistance(30);
		ModBlocks.deco_steel.setResistance(30);
		ModBlocks.deco_tungsten.setResistance(30);
		ModBlocks.deco_titanium.setResistance(30);
		ModBlocks.deco_red_copper.setResistance(30);
		ModBlocks.control_panel_custom.setResistance(30);
		ModBlocks.steel_corner.setResistance(30);
		ModBlocks.steel_wall.setResistance(30);
		ModBlocks.steel_roof.setResistance(30);
		ModBlocks.steel_scaffold.setResistance(30);
		ModBlocks.steel_grate.setResistance(15);
		ModBlocks.steel_grate_wide.setResistance(15);
	}

	public static void preInit(){
		modifyBlockParams();
		AddonBase._initMemberClasses(AddonBlocks.class);
		System.out.println("Blocks: "+ALL_BLOCKS.size());
		for(Block block : ALL_BLOCKS){
			ForgeRegistries.BLOCKS.register(block);
		}
	}
}
