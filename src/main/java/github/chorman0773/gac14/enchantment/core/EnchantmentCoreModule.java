package github.chorman0773.gac14.enchantment.core;

import github.chorman0773.gac14.Gac14Module;
import github.chorman0773.gac14.Version;
import net.minecraft.util.ResourceLocation;

public class EnchantmentCoreModule extends Gac14Module<EnchantmentCoreModule> {

	public EnchantmentCoreModule() {
		// TODO Auto-generated constructor stub
	}
	
	private static final ResourceLocation MODULE_NAME = ResourceLocation.makeResourceLocation("gac14:enchantment/core");
	private static final Version MODULE_VERSION = new Version("1.0");

	@Override
	public ResourceLocation getModuleName() {
		// TODO Auto-generated method stub
		return MODULE_NAME;
	}

	@Override
	public Version getModuleVersion() {
		// TODO Auto-generated method stub
		return MODULE_VERSION;
	}

}
