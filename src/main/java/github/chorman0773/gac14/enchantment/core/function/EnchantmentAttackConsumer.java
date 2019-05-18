package github.chorman0773.gac14.enchantment.core.function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public interface EnchantmentAttackConsumer {
	public void accept(EntityLivingBase user,Entity other, int level);
	
	public default EnchantmentAttackConsumer andThen(EnchantmentAttackConsumer next) {
		return (u,o,l)->{EnchantmentAttackConsumer.this.accept(u,o,l);next.accept(u, o, l);};
	}
	
	public static EnchantmentAttackConsumer nop() {
		return (u,o,l)->{};
	}
}
