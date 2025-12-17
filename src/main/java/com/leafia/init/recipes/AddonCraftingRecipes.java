package com.leafia.init.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.inventory.material.Mats;
import com.hbm.items.ItemEnums.EnumCircuitType;
import com.hbm.items.ModItems;
import com.leafia.contents.AddonBlocks;
import com.leafia.contents.AddonItems;
import com.leafia.contents.AddonItems.LeafiaRods;
import com.leafia.contents.control.fuel.nuclearfuel.LeafiaRodItem;
import com.leafia.contents.machines.reactors.pwr.debris.PWRDebrisCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Objects;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.OreDictManager.ZR;
import static com.hbm.main.CraftingManager.*;

public class AddonCraftingRecipes {
	public static void craftingRegister() {
		ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>)hack.getRegistry();

		addRecipeAuto(new ItemStack(AddonBlocks.spk_cable, 16), " W ", "RRR", " W ", 'W', ModItems.plate_dineutronium, 'R',OreDictManager.MAGTUNG.wireFine());
		addShapelessAuto(new ItemStack(ModBlocks.dfc_receiver, 1), AddonItems.dfcsh_beam, AddonItems.dfcsh_cable, AddonItems.dfcsh_corner, AddonItems.dfcsh_core, OreDictManager.STEEL.heavyBarrel(), AddonItems.dfcsh_front, AddonItems.dfcsh_corner, AddonItems.dfcsh_beam, AddonItems.dfcsh_beam);
		addRecipeAuto(new ItemStack(AddonBlocks.dfc_reinforced, 1), "SDS", "TXL", "SDS", 'S', OSMIRIDIUM.plateWelded(), 'D', ModItems.plate_dineutronium, 'T', ModItems.thermo_unit_endo, 'L', ModBlocks.dfc_receiver, 'X', ModBlocks.block_dineutronium);
		addRecipeAuto(new ItemStack(AddonBlocks.dfc_exchanger, 1), "SCS", "HMP", "SCS", 'S', OSMIRIDIUM.plateWelded(), 'C', ModItems.plate_combine_steel, 'H', ModBlocks.heater_heatex, 'M', ModItems.motor, 'P', ModItems.pipes_steel);

		addRecipeAuto(new ItemStack(AddonItems.fuzzy_identifier, 1), "=  ", "@CS", "@MP", '@', OreDictManager.GOLD.wireFine(), 'P', ANY_PLASTIC.ingot(), '=', DictFrame.fromOne(ModItems.circuit, EnumCircuitType.ADVANCED), 'M', ModItems.motor_desh, 'C', ModItems.coil_gold, 'S', ModItems.screwdriver_desh);

		addRecipeAuto(new ItemStack(LeafiaRods.leafRod, 4), "O", "I", "O", 'O', ZR.billet(), 'I', ZR.nugget());
		for (LeafiaRodItem rod : LeafiaRodItem.fromResourceMap.values()) {
			if (rod.baseItem != null) {
				addShapelessAuto(new ItemStack(rod,1),LeafiaRods.leafRod,new ItemStack(rod.baseItem,1,rod.baseMeta));
				addShapelessAuto(new ItemStack(rod.baseItem,1,rod.baseMeta),rod);
			}
		}
		// A NOTE TO SELF: Ingredient keys are supposed to be chars, not strings.
		// If they're strings, they things its part of the shape and probably shits your bed off.

		// CraftingManager can suck my ass god fucking dammit
		addRecipeAuto(new ItemStack(AddonBlocks.amat_duct,3),"CCC","DDD","CCC",'C',ALLOY.wireFine(),'D',new ItemStack(ModBlocks.fluid_duct_neo,1,0));
		addRecipeAuto(new ItemStack(AddonBlocks.amat_charger)," D ","DED"," D ",'D',AddonBlocks.amat_duct,'E',ModBlocks.machine_battery);

		for (int meta = 0; meta < 3; meta++) {
			addRecipeAuto(new ItemStack(AddonBlocks.ff_duct,3,meta),"DDD",'D',new ItemStack(ModBlocks.fluid_duct_neo,1,meta));
			addShapelessAuto(new ItemStack(ModBlocks.fluid_duct_neo,1,meta),'D',new ItemStack(AddonBlocks.ff_duct,1,meta));
		}
		addShapelessAuto(new ItemStack(AddonBlocks.ff_pump),new ItemStack(AddonBlocks.ff_duct,1,2),new ItemStack(ModItems.motor));
		addShapelessAuto(new ItemStack(AddonBlocks.ff_converter),new ItemStack(AddonBlocks.ff_duct,1,2),AL.plate());

		removeRecipesForItem(reg,ModItems.ams_lens);

		hack.getRegistry().register(new PWRDebrisCrafting().setRegistryName(new ResourceLocation("leafia", "lwr_debris_crafting_handler")));
	}
	static void removeRecipesForItem(ForgeRegistry<IRecipe> reg,Item item) {
		ResourceLocation loc = new ResourceLocation("hbm", Objects.requireNonNull(item.getRegistryName()).getPath());
		int i = 0;
		ResourceLocation r_loc = loc;
		while(net.minecraft.item.crafting.CraftingManager.REGISTRY.containsKey(r_loc)) {
			i++;
			reg.remove(r_loc);
			r_loc = new ResourceLocation("hbm", loc.getPath() + "_" + i);
		}
	}
}
