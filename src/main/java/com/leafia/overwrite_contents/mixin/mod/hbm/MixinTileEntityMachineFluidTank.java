package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.api.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.tileentity.machine.TileEntityMachineFluidTank;
import com.hbm.uninos.UniNodespace;
import com.leafia.contents.network.pipe_amat.uninos.AmatNet;
import com.leafia.contents.network.pipe_amat.uninos.AmatNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityMachineFluidTank.class)
public abstract class MixinTileEntityMachineFluidTank extends TileEntityMachineBase implements IFluidStandardTransceiverMK2 {
	@Shadow(remap = false) protected abstract DirPos[] getConPos();
	@Shadow(remap=false) public FluidTankNTM tank;
	@Shadow(remap=false) public short mode = 0;

	public MixinTileEntityMachineFluidTank(int scount) {
		super(scount);
	}
	@Inject(method = "update",at = @At(value = "INVOKE", target = "Lcom/hbm/tileentity/machine/TileEntityMachineFluidTank;getConPos()[Lcom/hbm/lib/DirPos;",remap=false),require = 1)
	public void onOnUpdate(CallbackInfo ci) {
		for(DirPos pos : this.getConPos()) {
			AmatNode dirNode = (AmatNode) UniNodespace.getNode(world, pos.getPos(),AmatNet.getProvider(tank.getTankType()));

			if(mode == 2) {
				tryProvide(tank, world, pos.getPos(), pos.getDir());
			} else {
				if(dirNode != null && dirNode.hasValidNet()) dirNode.net.removeProvider(this);
			}

			if(mode == 0) {
				if(dirNode != null && dirNode.hasValidNet()) dirNode.net.addReceiver(this);
			} else {
				if(dirNode != null && dirNode.hasValidNet()) dirNode.net.removeReceiver(this);
			}
		}
	}
}
