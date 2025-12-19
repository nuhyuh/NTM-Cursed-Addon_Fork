package com.leafia.init.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.ItemEnums.EnumExpensiveType;
import com.hbm.items.ModItems;
import com.leafia.contents.AddonBlocks.PWR;
import com.llib.exceptions.LeafiaDevFlaw;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.*;

public class AddonAssemblerRecipes {
	public static final AssemblyMachineRecipes INSTANCE = AssemblyMachineRecipes.INSTANCE;
	public static void register() {
		INSTANCE.register(new GenericRecipe("ass.leafia.legacy_pwrcontrol").setup(100,100)
				.outputItems(new ItemStack(PWR.reactor_control))
				.inputItems(
						new OreDictStack(STEEL.ingot(),4),
						new OreDictStack(PB.ingot(),6),
						new OreDictStack(W.bolt(),6),
						new ComparableStack(ModItems.motor)
				)
		);
		INSTANCE.register(new GenericRecipe("ass.leafia.legacy_pwrfuel").setup(150,100)
				.outputItems(new ItemStack(PWR.element_old))
				.inputItems(
						new OreDictStack(STEEL.ingot(),2),
						new OreDictStack(OreDictManager.getReflector(),4),
						new OreDictStack(PB.plate(),2),
						new OreDictStack(ZR.ingot(),2)
				)
		);
		INSTANCE.register(new GenericRecipe("ass.leafia.legacy_pwrconductor").setup(130,100)
				.outputItems(new ItemStack(PWR.conductor))
				.inputItems(
						new OreDictStack(STEEL.ingot(),4),
						new OreDictStack(CU.plate(),12),
						new OreDictStack(W.wireFine(),4)
				)
		);
		replaceOutput("ass.pwrcontrol",new ItemStack(PWR.control));
		replaceOutput("ass.pwrfuel",new ItemStack(PWR.element));
		replaceOutput("ass.pwrchannel",new ItemStack(PWR.channel));
		replaceOutput("ass.pwrheatex",new ItemStack(PWR.exchanger));
		replaceOutput("ass.pwrreflector",new ItemStack(PWR.reflector));
		replaceOutput("ass.pwrcasing",new ItemStack(PWR.hull));
		replaceOutput("ass.pwrcontroller",new ItemStack(PWR.terminal));
		replaceOutput("ass.pwrport",new ItemStack(PWR.port));
		remove("ass.pwrneutronsource");
		remove("ass.pwrheatsink");
	}
	public static void remove(String entry) {
		GenericRecipe recipe = INSTANCE.recipeNameMap.get(entry);
		if (recipe != null) {
			INSTANCE.recipeOrderedList.remove(recipe);
			INSTANCE.recipeNameMap.remove(entry);
		}
	}
	public static void replaceOutput(String entry,ItemStack... outputs) {
		GenericRecipe recipe = INSTANCE.recipeNameMap.get(entry);
		if (recipe != null) {
			recipe.outputItems(outputs);
		} else
			throw new LeafiaDevFlaw("Could not find recipe \""+entry+"\" to replace");
	}
}
