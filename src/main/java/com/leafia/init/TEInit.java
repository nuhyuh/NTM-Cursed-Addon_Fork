package com.leafia.init;

import com.leafia.AddonBase;
import com.leafia.contents.building.sign.SignTE;
import com.leafia.contents.debug.ff_test.source.FFSourceTE;
import com.leafia.contents.debug.ff_test.tank.FFTankTE;
import com.leafia.contents.machines.misc.heatex.CoolantHeatexTE;
import com.leafia.contents.machines.powercores.dfc.components.cemitter.CoreCEmitterTE;
import com.leafia.contents.machines.powercores.dfc.components.exchanger.CoreExchangerTE;
import com.leafia.contents.machines.processing.mixingvat.MixingVatTE;
import com.leafia.contents.machines.processing.mixingvat.proxy.MixingVatProxy;
import com.leafia.contents.machines.reactors.lftr.components.arbitrary.MSRArbitraryTE;
import com.leafia.contents.machines.reactors.lftr.components.control.MSRControlTE;
import com.leafia.contents.machines.reactors.lftr.components.ejector.MSREjectorTE;
import com.leafia.contents.machines.reactors.lftr.components.element.MSRElementTE;
import com.leafia.contents.machines.reactors.lftr.components.plug.MSRPlugTE;
import com.leafia.contents.machines.reactors.lftr.processing.separator.SaltSeparatorTE;
import com.leafia.contents.machines.reactors.pwr.blocks.components.control.PWRControlTE;
import com.leafia.contents.machines.reactors.pwr.blocks.components.element.PWRElementTE;
import com.leafia.contents.machines.reactors.pwr.blocks.components.port.PWRPortTE;
import com.leafia.contents.machines.reactors.pwr.blocks.components.terminal.PWRTerminalTE;
import com.leafia.contents.machines.reactors.pwr.blocks.wreckage.PWRMeshedWreckEntity;
import com.leafia.contents.network.ff_duct.FFDuctTE;
import com.leafia.contents.network.ff_duct.utility.converter.FFConverterTE;
import com.leafia.contents.network.ff_duct.utility.pump.FFPumpTE;
import com.leafia.contents.network.pipe_amat.AmatDuctTE;
import com.leafia.contents.network.pipe_amat.charger.AmatDuctChargerTE;
import com.leafia.contents.network.spk_cable.SPKCableTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TEInit {
	public static void preInit() {
		{
			// Debug TEs
			register(FFSourceTE.class,"debug_ff_source");
			register(FFTankTE.class,"debug_ff_tank");
		}
		register(SPKCableTE.class,"spk_cable_te");
		register(CoreCEmitterTE.class,"core_creative_emitter_te");
		register(CoreExchangerTE.class,"core_exchanger_te");
		register(SignTE.class,"letter_sign_te");
		register(FFDuctTE.class,"ff_duct_te");
		register(FFPumpTE.class,"ff_pump_te");
		register(FFConverterTE.class,"ff_converter_te");
		register(SaltSeparatorTE.class,"salt_separator_te");
		register(MSRArbitraryTE.class,"lftr_arbitrary_te");
		register(MSRControlTE.class,"lftr_control_te");
		register(MSREjectorTE.class,"lftr_ejector_te");
		register(MSRElementTE.class,"lftr_element_te");
		register(MSRPlugTE.class,"lftr_plug_te");
		register(MixingVatTE.class,"mixing_vat_te");
		register(MixingVatProxy.class,"mixing_vat_proxy_te");
		register(CoolantHeatexTE.class,"coolant_heatex_te");
		register(PWRControlTE.class,"lwr_control_te");
		register(PWRElementTE.class,"lwr_element_te");
		register(PWRPortTE.class,"lwr_port_te");
		register(PWRTerminalTE.class,"lwr_terminal_te");
		register(PWRMeshedWreckEntity.class,"lwr_wreck_te");
		register(AmatDuctTE.class,"pipe_amat_te");
		register(AmatDuctChargerTE.class,"charger_amat_te");
	}
	private static void register(Class<? extends TileEntity> clazz,String res) {
		GameRegistry.registerTileEntity(clazz,new ResourceLocation(AddonBase.MODID,res));
	}
}
