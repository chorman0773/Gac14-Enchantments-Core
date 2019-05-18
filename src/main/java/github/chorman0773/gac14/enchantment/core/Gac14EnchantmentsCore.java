package github.chorman0773.gac14.enchantment.core;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import github.chorman0773.gac14.Gac14Module;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("gac14-enchantment-core")
public class Gac14EnchantmentsCore
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Gac14EnchantmentsCore() {
    	instance = this;
    }
    
    private final EnchantmentCoreModule mod = new EnchantmentCoreModule();
    
    private static Gac14EnchantmentsCore instance;
    
    public static Gac14EnchantmentsCore getInstance() {
    	return instance;
    }
    
    public EnchantmentCoreModule getModule() {
    	return mod;
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerModules(RegistryEvent.Register<Gac14Module<?>> e) {
        	e.getRegistry().register(Gac14EnchantmentsCore.instance.mod);
        }
    }
}
