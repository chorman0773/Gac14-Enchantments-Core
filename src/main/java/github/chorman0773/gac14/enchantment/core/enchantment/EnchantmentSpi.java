package github.chorman0773.gac14.enchantment.core.enchantment;

import java.util.stream.Stream;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class EnchantmentSpi {

	public EnchantmentSpi() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract Stream<Enchantment> getEnchantmentsOnItem(ItemStack s);
	public abstract int getEnchantmentLevel(ItemStack s,Enchantment ench);
	public abstract void lateRegister(ResourceLocation loc,Gac14Enchantment ench);
	public abstract void reregister(ResourceLocation loc,Gac14Enchantment ench);
	public abstract void deregister(Gac14Enchantment ench);
	public abstract void applyToItem(Enchantment ench,ItemStack stack, int level);
	public abstract boolean isFirstTrigger(ResourceLocation name,LivingEntity on);

	public abstract boolean hasEnchantment(ResourceLocation loc);

	public abstract Enchantment getEnchantment(ResourceLocation loc);
}
