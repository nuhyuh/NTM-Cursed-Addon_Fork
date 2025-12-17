package com.leafia.dev.blocks;

import com.hbm.render.block.BlockBakeFrame;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AddonBlockBakeFrame extends BlockBakeFrame {
	public AddonBlockBakeFrame(BlockForm form,@NotNull String... textures) {
		super(form,textures);
	}

	public AddonBlockBakeFrame(String texture) {
		super(texture);
	}

	public AddonBlockBakeFrame(String topTexture,String sideTexture) {
		super(topTexture,sideTexture);
	}

	public AddonBlockBakeFrame(String topTexture,String sideTexture,String bottomTexture) {
		super(topTexture,sideTexture,bottomTexture);
	}

	public void registerBlockTextures(TextureMap map) {
		for (String texture : this.textureArray) {
			ResourceLocation spriteLoc = new ResourceLocation("leafia", ROOT_PATH + texture);
			map.registerSprite(spriteLoc);
		}
	}

	public ResourceLocation getSpriteLoc(int index) {
		return new
				ResourceLocation("leafia", ROOT_PATH + textureArray[index]);
	}
}
