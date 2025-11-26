package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.handler.jei.AssemblyMachineRecipeHandler;
import com.hbm.handler.jei.CentrifugeRecipeHandler;
import com.hbm.handler.jei.ChemicalPlantRecipeHandler;
import com.hbm.handler.jei.JEIConfig;
import com.leafia.jei.JEIAssembler;
import com.leafia.jei.JEICentrifuge;
import com.leafia.jei.JEICentrifuge.Recipe;
import com.leafia.jei.JEIChemplant;
import com.llamalad7.mixinextras.sugar.Local;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = JEIConfig.class,remap = false)
public class MixinJEIConfig {
	private static List<IRecipeCategory> addon_categories = new ArrayList<>();
	@Redirect(method = "registerCategories",at = @At(value = "INVOKE", target = "Lmezz/jei/api/recipe/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/IRecipeCategory;)V"))
	public void onRegisterCategories(IRecipeCategoryRegistration instance,IRecipeCategory[] iRecipeCategories,@Local(type = IGuiHelper.class) IGuiHelper help) {
		addon_categories.add(new JEICentrifuge(help));
		addon_categories.add(new JEIChemplant(help));
		addon_categories.add(new JEIAssembler(help));

		for (IRecipeCategory<Recipe> category : addon_categories)
			instance.addRecipeCategories(category);
		try {
			instance.addRecipeCategories(iRecipeCategories);
		} catch (IllegalArgumentException iarg) {
			String message = iarg.getMessage();
			String category = message.split("\"")[1];
			boolean okay = false;
			for (IRecipeCategory<Recipe> addonCategory : addon_categories) {
				if (addonCategory.getUid().equals(category)) {
					okay = true;
					break;
				}
			}
			if (!okay)
				throw iarg;
		}
	}

	@Redirect(method = "register",at = @At(value = "INVOKE", target = "Lcom/hbm/handler/jei/CentrifugeRecipeHandler;getRecipes()Ljava/util/List;"))
	public List centrifuge(CentrifugeRecipeHandler instance) {
		return JEICentrifuge.Recipe.buildRecipes();
	}
	@Redirect(method = "register",at = @At(value = "INVOKE", target = "Lcom/hbm/handler/jei/ChemicalPlantRecipeHandler;getRecipes()Ljava/util/List;"))
	public List chemplant(ChemicalPlantRecipeHandler instance) {
		return JEIChemplant.Recipe.buildRecipes();
	}
	@Redirect(method = "register",at = @At(value = "INVOKE", target = "Lcom/hbm/handler/jei/AssemblyMachineRecipeHandler;getRecipes()Ljava/util/List;"))
	public List assembler(AssemblyMachineRecipeHandler instance) {
		return JEIAssembler.Recipe.buildRecipes();
	}
}
