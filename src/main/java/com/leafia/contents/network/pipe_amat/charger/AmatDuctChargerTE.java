package com.leafia.contents.network.pipe_amat.charger;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluidmk2.FluidNetMK2;
import com.hbm.api.fluidmk2.IFluidProviderMK2;
import com.hbm.api.fluidmk2.IFluidStandardSenderMK2;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.uninos.GenNode;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.Compat;
import com.leafia.contents.network.pipe_amat.AmatDuctTE;
import com.leafia.contents.network.pipe_amat.uninos.AmatNet;
import com.leafia.dev.container_utility.LeafiaPacket;
import com.leafia.dev.container_utility.LeafiaPacketReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AmatDuctChargerTE extends TileEntity implements ITickable, IEnergyReceiverMK2, LeafiaPacketReceiver, IFluidProviderMK2 {
	boolean loaded = true;
	long power = 0;
	public static long maxPower = 10000;
	FluidType filter = Fluids.NONE;

	@Override
	public void update() {
		if (!world.isRemote) {
			List<AmatNet> blacklist = new ArrayList<>();
			for (EnumFacing facing : EnumFacing.values()) {
				BlockPos offset = pos.offset(facing);
				ForgeDirection dir = ForgeDirection.getOrientation(facing);
				trySubscribe(world,offset,dir);
				TileEntity te = Compat.getTileStandard(world,offset.getX(),offset.getY(),offset.getZ());
				if (te instanceof AmatDuctTE duct) {
					if (duct.canConnect(filter,dir)) {
						GenNode<AmatNet> node = UniNodespace.getNode(world,offset,AmatNet.getProvider(filter));
						if(node != null && node.net != null) {
							if (!blacklist.contains(node.net)) {
								blacklist.add(node.net);
								long demand = AmatNet.maxPower-node.net.power;
								long transfer = Math.min(power,demand);
								if (transfer > 0) {
									power -= transfer;
									node.net.power += transfer;
								}
							}
						}
					}
				}
			}
			LeafiaPacket._start(this).__write(31,power).__sendToAffectedClients();
		}
	}

	public void sendTypeUpdatePacket() {
		LeafiaPacket._start(this).__write(30,filter.getID()).__sendToAffectedClients();
	}
	public void setType(FluidType type) {
		filter = type;
		sendTypeUpdatePacket();
	}
	public FluidType getType() {
		return filter;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("power",power);
		compound.setInteger("filter",filter.getID());
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		filter = Fluids.fromID(compound.getInteger("filter"));
		power = compound.getLong("power");
	}

	@Override
	public double affectionRange() {
		return LeafiaPacketReceiver.super.affectionRange();
	}

	@Override
	public void onChunkUnload() {
		loaded = false;
		super.onChunkUnload();
	}

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public void setPower(long l) {
		power = l;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public String getPacketIdentifier() {
		return "AMAT_CHRGR";
	}

	@Override
	public void onReceivePacketLocal(byte key,Object value) {
		if (key == 31)
			power = (long)value;
		else if (key == 30)
			filter = Fluids.fromID((int)value);
	}

	@Override
	public void onReceivePacketServer(byte key,Object value,EntityPlayer plr) { }

	@Override
	public void onPlayerValidate(EntityPlayer plr) {
		LeafiaPacket._start(this).__write(30,filter.getID()).__sendToClient(plr);
	}

	@Override
	public void useUpFluid(FluidType fluidType,int i,long l) { }
	@Override
	public long getFluidAvailable(FluidType fluidType,int i) {
		return 0;
	}
	@Override
	public FluidTankNTM[] getAllTanks() {
		return new FluidTankNTM[0];
	}
}
