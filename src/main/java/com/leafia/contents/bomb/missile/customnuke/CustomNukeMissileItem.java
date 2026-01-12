package com.leafia.contents.bomb.missile.customnuke;

import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissileStandard;
import com.leafia.contents.AddonItems;

public class CustomNukeMissileItem extends ItemMissileStandard {
	public CustomNukeMissileItem(String s) {
		super(s,MissileFormFactor.MICRO,MissileTier.TIER0);
		ModItems.ALL_ITEMS.remove(this);
		AddonItems.ALL_ITEMS.add(this);
	}
}
