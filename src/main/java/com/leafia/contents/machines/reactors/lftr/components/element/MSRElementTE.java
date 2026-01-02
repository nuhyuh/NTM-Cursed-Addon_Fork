package com.leafia.contents.machines.reactors.lftr.components.element;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.interfaces.IRadResistantBlock;
import com.hbm.inventory.OreDictManager;
import com.hbm.util.Tuple.Pair;
import com.leafia.contents.AddonFluids;
import com.leafia.contents.machines.reactors.lftr.components.MSRTEBase;
import com.leafia.contents.machines.reactors.lftr.components.arbitrary.MSRArbitraryBlock;
import com.leafia.contents.machines.reactors.lftr.components.arbitrary.MSRArbitraryTE;
import com.leafia.contents.machines.reactors.pwr.blocks.PWRReflectorBlock;
import com.leafia.dev.LeafiaDebug.Tracker;
import com.leafia.dev.container_utility.LeafiaPacket;
import com.leafia.dev.math.FiaMatrix;
import com.llib.group.LeafiaMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class MSRElementTE extends MSRTEBase {
	public enum MSRByproduct {
		th232(
				1,
				new Pair<>("pa233",1d)
		),
		pa233(
				1,
				new Pair<>("u233",1d)
		),
		u233(
				1,
				new Pair<>("u235",1d)
		);
		final public double division;
		final public Pair<String,Double>[] byproducts;
		MSRByproduct(double division,Pair<String,Double>... byproducts) {
			this.division = division;
			this.byproducts = byproducts;
		}
	}
	public enum MSRFuel {
		th232(
				new Item[]{},
				new String[]{OreDictManager.TH232.nugget()},
				"(x÷2)^0.6/B",
				(x)->Math.pow(x/2,0.6),
				70000000d,
				MSRByproduct.th232
		),
		pa233(
				new Item[]{},
				new String[]{},
				"(x÷2)^0.6/B",
				(x)->Math.pow(x/2,0.6),
				1000000d,
				MSRByproduct.pa233
		),
		u233(
				new Item[]{},
				new String[]{OreDictManager.U233.nugget()},
				"(x×3)^0.65/B",
				(x)->Math.pow(x*3,0.65),
				600000000d,
				MSRByproduct.u233
		),
		u235(
				new Item[]{},
				new String[]{OreDictManager.U235.nugget()},
				"(x×3)^0.6/B",
				(x)->Math.pow(x*3,0.6)
		);
		final public Item[] items;
		final public String[] dicts;
		final public String funcString;
		final public Function<Double,Double> function;
		final public double life;
		final public MSRByproduct byproduct;
		MSRFuel(Item[] items,String[] dicts,String funcString,Function<Double,Double> function) {
			this.items = items;
			this.dicts = dicts;
			this.funcString = funcString;
			this.function = function;
			life = 0;
			byproduct = null;
		}
		MSRFuel(Item[] items,String[] dicts,String funcString,Function<Double,Double> function,double life,MSRByproduct byproduct) {
			this.items = items;
			this.dicts = dicts;
			this.funcString = funcString;
			this.function = function;
			this.life = life;
			this.byproduct = byproduct;
		}
	}
	Block getBlockArbitrary(BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof MSRArbitraryBlock) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof MSRArbitraryTE arbitrary) {
				if (arbitrary.inventory.getStackInSlot(0).getItem() instanceof ItemBlock b)
					block = b.getBlock();
			}
		}
		return block;
	}

	BlockPos toBlockPos(FiaMatrix mat) {
		return new BlockPos(mat.position);
	}

	FiaMatrix toFiaMatrix(BlockPos pos) {
		return new FiaMatrix(new Vec3d(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5));
	}

	boolean isModerator(Block block) {
		return ("_"+block.getRegistryName().getPath()+"_").matches(".*[^a-z]graphite[^a-z].*");
	}

	boolean isRadResistant(Block block) {
		return block instanceof IRadResistantBlock;
	}

	MSRElementTE getReactionTarget(BlockPos pos) {
		Block block = getBlockArbitrary(pos);
		if (block instanceof PWRReflectorBlock)
			return this;
		if (world.getTileEntity(pos) instanceof MSRElementTE te)
			return te;
		return null;
	}

	void addMixture(Map<String,Double> mixture,String fuelType,double amt) {
		if (mixture.containsKey(fuelType))
			mixture.put(fuelType,mixture.get(fuelType)+amt);
		else
			mixture.put(fuelType,amt);
	}

	void react(MSRElementTE te,double distance,double multiplier) {
		double B = te.tank.getFluidAmount()/2000d+tank.getFluidAmount()/2000d;
		FluidStack stack0 = tank.getFluid();
		FluidStack stack1 = te.tank.getFluid();
		if (stack0 != null) {
			double y = 0;
			NBTTagCompound nbt = nbtProtocol(stack0.tag);
			double curRestriction = restriction;
			if (stack1 != null) {
				Map<String,Double> mixture = readMixture(nbt);
				for (Entry<String,Double> entry : mixture.entrySet()) {
					// entry.getValue() doesn't work because the mixture changes concurrently
					double mix = mixture.get(entry.getKey());
					if (mix <= 0) continue;
					try {
						MSRFuel type = MSRFuel.valueOf(entry.getKey());
						double tempAdd = type.function.apply(nbtProtocol(stack1.tag).getDouble("heat")+getBaseTemperature(AddonFluids.fromFF(stack0.getFluid())))*mix*B;
						y += tempAdd;
						if (type.byproduct != null) {
							double addAmt = tempAdd/type.life;
							for (Pair<String,Double> byproduct : type.byproduct.byproducts)
								addMixture(mixture,byproduct.getKey(),/*mix* what was my peanut brain cooking while i was coding ts*/addAmt/type.byproduct.division);
							double perc = mix-addAmt;
							mixture.put(entry.getKey(),perc);
							if (perc <= 0)
								mixture.remove(entry.getKey());
						}
					} catch (IllegalArgumentException ignored) {}
				}
				curRestriction = Math.max(curRestriction,te.restriction);
				nbt.setTag("itemMixture",writeMixture(mixture));
			}
			y *= (1-curRestriction);
			double heat = nbt.getDouble("heat");
			y /= distance/2;
			y *= multiplier;
			double heatMg = y-heat;
			heat += Math.pow(Math.abs(heatMg),0.2)*Math.signum(heatMg);
			nbt.setDouble("heat",heat);
			stack0.tag = nbt;
			double rad = Math.pow(heatMg,0.65)/2;
			ChunkRadiationManager.proxy.incrementRad(world,pos,(float)rad/8,(float)rad);
		}
	}

	void reactCorners2D(FiaMatrix mat,Map<BlockPos,Block> blocks) {
		Tracker._startProfile(this,"reactCorners2D");
		for (int x = -1; x <= 2; x+=2) {
			for (int y = -1; y <= 2; y+=2) {
				BlockPos a = toBlockPos(mat.translate(x,0,0));
				BlockPos b = toBlockPos(mat.translate(0,y,0));
				BlockPos c = toBlockPos(mat.translate(x,y,0));
				Block blockA = blocks.getOrDefault(a,Blocks.AIR);
				Block blockB = blocks.getOrDefault(b,Blocks.AIR);
				boolean moderatedA = isModerator(blockA);
				boolean moderatedB = isModerator(blockB);
				boolean resistantA = isRadResistant(blockA);
				boolean resistantB = isRadResistant(blockA);
				if (!resistantA && !resistantB) {
					MSRElementTE te = getReactionTarget(c);
					if (te != null)
						react(te,2,(moderatedA || moderatedB) ? 2 : 0.5);
				}
				Tracker._tracePosition(this,c,moderatedA,moderatedB);
			}
		}
		Tracker._endProfile(this);
	}

	void reactCorners() {
		Tracker._startProfile(this,"reactCorners");
		Map<BlockPos,Block> mop = new LeafiaMap<>();
		for (EnumFacing value : EnumFacing.values()) {
			BlockPos p = pos.offset(value);
			mop.put(p,getBlockArbitrary(p));
		}
		FiaMatrix mat = toFiaMatrix(pos);
		reactCorners2D(mat,mop);
		reactCorners2D(mat.rotateY(90),mop);
		reactCorners2D(mat.rotateX(90),mop);
		Tracker._endProfile(this);
	}

	void reactLine(EnumFacing facing) {
		Tracker._startProfile(this,"reactLine");
		boolean moderated = false;
		for (int i = 1; i <= 4; i++) {
			BlockPos p = pos.offset(facing,i);
			Tracker._tracePosition(this,p,"moderated: "+moderated);
			Block block = getBlockArbitrary(p);
			if (isModerator(block)) moderated = true;
			MSRElementTE te = getReactionTarget(p);
			if (te != null) {
				react(te,i,moderated ? 2 : 0.5);
				return;
			}
			if (isRadResistant(block)) return;
		}
		Tracker._endProfile(this);
	}

	@Override
	public String getPacketIdentifier() {
		return "MSRElement";
	}
	public BlockPos control = null;
	public double restriction = 0;

	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			/*if (control != null && control.isInvalid()) {
				control = null;
				restriction = 0;
			}*/
			reactCorners();
			for (EnumFacing face : EnumFacing.values())
				reactLine(face);
			LeafiaPacket._start(this).__write(0,restriction).__sendToAffectedClients();
			markChanged();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		restriction = compound.getDouble("restriction");
		if (compound.hasKey("controlX")) {
			control = new BlockPos(
					compound.getInteger("controlX"),
					compound.getInteger("controlY"),
					compound.getInteger("controlZ")
			);;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setDouble("restriction",restriction);
		if (control != null) {
			compound.setInteger("controlX",control.getX());
			compound.setInteger("controlY",control.getY());
			compound.setInteger("controlZ",control.getZ());
		} else {
			compound.removeTag("controlX");
			compound.removeTag("controlY");
			compound.removeTag("controlZ");
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void onReceivePacketLocal(byte key,Object value) {
		super.onReceivePacketLocal(key,value);
		if (key == 0)
			restriction = (double)value;
	}
}
