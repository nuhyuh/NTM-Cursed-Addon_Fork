package com.leafia.jei;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.jei.JEIConfig;
import com.hbm.handler.jei.JeiRecipes;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes.IOutput;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemBlueprints;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.lib.RefStrings;
import com.hbm.util.I18nUtil;
import com.leafia.jei.JEIAssembler.Recipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIAssembler implements IRecipeCategory<Recipe> {
	public static final ResourceLocation gui_rl
			= new ResourceLocation("leafia","textures/gui/jei/assembler.png");

	public static class Recipe implements IRecipeWrapper {
		public static final List<Recipe> recipes = new ArrayList<>();
		public static List<Recipe> buildRecipes() {
			for (GenericRecipe recipe : AssemblyMachineRecipes.INSTANCE.recipeOrderedList) {
				recipes.add(new Recipe(recipe));
			}
			return recipes;
		}

		final List<List<ItemStack>> inputs = new ArrayList<>();
		final List<ItemStack> outputs = new ArrayList<>();
		public Recipe(GenericRecipe recipe) {
			List<FluidStack> inputFluid =
					(recipe.inputFluid == null ? new ArrayList<>() : Arrays.asList(recipe.inputFluid));
			List<FluidStack> outputFluid =
					(recipe.outputFluid == null ? new ArrayList<>() : Arrays.asList(recipe.outputFluid));
			List<AStack> inputItem =
					(recipe.inputItem == null ? new ArrayList<>() : Arrays.asList(recipe.inputItem));
			List<IOutput> outputItem =
					(recipe.outputItem == null ? new ArrayList<>() : Arrays.asList(recipe.outputItem));
			for (int i = 0; i < 12; i++) {
				if (inputItem.size() > i) {
					List<ItemStack> stacks = inputItem.get(i).getStackList();
					inputs.add(stacks);
				} else
					inputs.add(Collections.singletonList(new ItemStack(Items.AIR)));
			}
			if (!inputFluid.isEmpty()) {
				ItemStack icon = ItemFluidIcon.make(inputFluid.get(0));
				inputs.add(Collections.singletonList(icon));
			} else {
				inputs.add(Collections.singletonList(new ItemStack(Items.AIR)));
			}
			if (recipe.isPooled()) {
				String[] pools = recipe.getPools();
				if (pools.length > 0)
					inputs.add(Collections.singletonList(ItemBlueprints.make(pools[0])));
				else
					inputs.add(Collections.singletonList(new ItemStack(Items.AIR)));
			} else
				inputs.add(Collections.singletonList(new ItemStack(Items.AIR)));
			ItemStack[] stacks = outputItem.get(0).getAllPossibilities();
			if (stacks != null) {
				outputs.add(stacks[0]);
			}
			if (!outputFluid.isEmpty()) {
				ItemStack icon = ItemFluidIcon.make(outputFluid.get(0));
				outputs.add(icon);
			} else {
				outputs.add(new ItemStack(Items.AIR));
			}
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setInputLists(VanillaTypes.ITEM,inputs);
			ingredients.setOutputs(VanillaTypes.ITEM,outputs);
		}
	}

	protected final IDrawable background;
	protected final IDrawableStatic staticArrow;
	protected final IDrawableAnimated animatedArrow;
	protected final IDrawableStatic staticPower;
	protected final IDrawableAnimated animatedPower;
	public JEIAssembler(IGuiHelper help) {
		this.background = help.createDrawable(gui_rl,6,15,163,55+18);
		staticArrow = help.createDrawable(gui_rl, 16, 86+18, 36, 18);
		animatedArrow = help.createAnimatedDrawable(staticArrow, 48, StartDirection.LEFT, false);
		staticPower = help.createDrawable(gui_rl, 0, 86+18, 16, 52);
		animatedPower = help.createAnimatedDrawable(staticPower, 480, StartDirection.TOP, true);
	}

	@Override public String getUid() { return JEIConfig.ASSEMBLY_MACHINE; }
	@Override public String getTitle() {
		return I18nUtil.resolveKey(ModBlocks.machine_assembly_machine.getTranslationKey()+".name");
	}
	@Override public String getModName() { return RefStrings.MODID; }
	@Override public IDrawable getBackground() { return background; }

	@Override
	public void drawExtras(Minecraft minecraft) {
		animatedArrow.draw(minecraft, 100, 19);
		animatedPower.draw(minecraft, 2, 2);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout,Recipe recipeWrapper,IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 3; y++)
				stacks.init(x+y*4,true,28+x*18,1+y*18);
		}
		stacks.init(12,true,46,55);
		stacks.init(13,true,109,1);
		stacks.init(14,false,136,19);
		stacks.init(15,false,118,55);
		stacks.init(16,true,1,55);
		stacks.set(ingredients);
		stacks.set(16,JeiRecipes.getBatteries());
	}
}
