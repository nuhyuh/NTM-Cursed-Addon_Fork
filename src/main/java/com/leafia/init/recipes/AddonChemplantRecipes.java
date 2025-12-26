package com.leafia.init.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.ModItems;
import com.leafia.contents.AddonFluids;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.*;

public class AddonChemplantRecipes {
	public static final ChemicalPlantRecipes INSTANCE = ChemicalPlantRecipes.INSTANCE;
	public static void register() {
		INSTANCE.register(new GenericRecipe("chem.leafia.flibe").setupNamed(60,400).setIcon(ModItems.fluid_icon,AddonFluids.FLUORIDE.getID())
				.inputItems(new RecipesCommon.OreDictStack(BE.ingot(),1),new OreDictStack(LI.ingot(),1),new OreDictStack(F.dust(),2))
				.inputFluids(new FluidStack(Fluids.SULFURIC_ACID,2_000))
				.outputItems(new ItemStack(ModItems.ingot_calcium,2),new ItemStack(ModItems.sulfur,2))
				.outputFluids(new FluidStack(AddonFluids.FLUORIDE,2700),new FluidStack(Fluids.WATER,2_000),new FluidStack(Fluids.OXYGEN,2_000)));
		/*INSTANCE.register(new GenericRecipe("chem.leafia.fluorine").setupNamed(100,600).setIcon(ModItems.fluid_icon,Fluids.FLUORINE.getID())
				.inputItems(new RecipesCommon.OreDictStack(F.dust(),1))
				.outputItems(new ItemStack(ModItems.ingot_calcium))
				.outputFluids(new FluidStack(Fluids.FLUORINE,1000))
		);*/
	}
}
