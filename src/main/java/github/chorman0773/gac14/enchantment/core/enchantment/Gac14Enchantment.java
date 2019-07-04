package github.chorman0773.gac14.enchantment.core.enchantment;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Predicates;

import github.chorman0773.gac14.enchantment.core.function.DamageModifierFunction;
import github.chorman0773.gac14.enchantment.core.function.EnchantmentAttackConsumer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class Gac14Enchantment extends Enchantment {
	
	private Predicate<ItemStack> checkApply;
	private Predicate<ItemStack> checkEnchant;
	private Predicate<Enchantment> checkApplyWith;
	private int maxLevel;
	private boolean curse;
	private boolean treasure;
	private EnchantmentAttackConsumer entityAttacked;
	private EnchantmentAttackConsumer userAttacked;
	private ITextComponent displayName;
	private DamageModifierFunction outgoingMod;
	private DamageModifierFunction incomingMod;
	private BiConsumer<ItemStack,LivingEntity> equipFunc;
	private BiConsumer<ItemStack,LivingEntity> unequipFunc;
	private BiConsumer<ItemStack,LivingEntity> tickFunc;
	
	private static EnchantmentSpi provider;
	
	
	
	public static boolean isFirstTrigger(ResourceLocation name,LivingEntity on) {
		return provider.isFirstTrigger(name,on);
	}
	
	public static int getEnchantmentLevel(ItemStack stack,Gac14Enchantment ench) {
		return provider.getEnchantmentLevel(stack, ench);
	}
	
	
	
	public static void reregister(Gac14Enchantment e) {
		provider.reregister(e.getRegistryName(), e);
	}
	
	public static void lateRegister(Gac14Enchantment e) {
		provider.lateRegister(e.getRegistryName(), e);
	}
	
	
	protected Gac14Enchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots,Predicate<ItemStack> checkApply,Predicate<ItemStack> checkEnchant,Predicate<Enchantment> checkApplyWith, EnchantmentAttackConsumer entityAttacked,EnchantmentAttackConsumer userAttacked,int maxLevel,boolean treasure,boolean curse,ITextComponent unName,ResourceLocation name,DamageModifierFunction incomingFunc,DamageModifierFunction outgoingFunc,BiConsumer<ItemStack,LivingEntity> equipFunc,BiConsumer<ItemStack,LivingEntity> unequipFunc,BiConsumer<ItemStack,LivingEntity> tickFunc) {
		super(rarityIn, typeIn, slots);
		this.checkApply = checkApply;
		this.checkEnchant = checkEnchant;
		this.checkApplyWith = checkApplyWith;
		this.entityAttacked = entityAttacked;
		this.userAttacked = userAttacked;
		this.maxLevel = maxLevel;
		this.curse = curse;
		this.treasure = treasure;
		this.displayName = unName;
		this.setRegistryName(name);
		this.outgoingMod = outgoingFunc;
		this.incomingMod = incomingFunc;
		this.equipFunc = equipFunc;
		this.unequipFunc = unequipFunc;
		this.tickFunc = tickFunc;
	}
	
	public static class Builder{
		private Predicate<ItemStack> checkApply = Predicates.alwaysFalse();
		private Predicate<ItemStack> checkEnchant = Predicates.alwaysFalse();
		private Predicate<Enchantment> checkApplyWith = Predicates.alwaysFalse();
		
		private int maxLevel = 0;
		private boolean curse = false;
		private boolean treasure = false;
		
		private EnchantmentAttackConsumer entityAttacked = EnchantmentAttackConsumer.nop();
		private EnchantmentAttackConsumer userAttacked = EnchantmentAttackConsumer.nop();
		private ITextComponent displayName;
		private ResourceLocation name;
		private EnumSet<EquipmentSlotType> slots = EnumSet.noneOf(EquipmentSlotType.class);
		private Rarity rarity = Rarity.COMMON;
		private EnchantmentType type = EnchantmentType.ALL;
		
		private DamageModifierFunction outgoingFunc = DamageModifierFunction.one();
		private DamageModifierFunction incomingFunc = DamageModifierFunction.one();
		
		private BiConsumer<ItemStack,LivingEntity> equipFunc = (t,u)->{};
		private BiConsumer<ItemStack,LivingEntity> unequipFunc = (t,u)->{};
		private BiConsumer<ItemStack,LivingEntity> tickFunc = (t,u)->{};
		
		public static Builder create() {
			return new Builder();
		}
		
		public Builder withApply(Predicate<ItemStack> check) {
			checkApply = checkApply.or(check);
			return this;
		}
		
		public Builder withEnchant(Predicate<ItemStack> check) {
			checkEnchant = checkEnchant.or(check);
			return this;
		}
		
		public Builder withMutalExclusionFilter(Predicate<Enchantment> filter) {
			checkApplyWith = checkApplyWith.or(filter);
			return this;
		}
		
		public Builder withOnUserAttacked(EnchantmentAttackConsumer consumer) {
			this.userAttacked = userAttacked.andThen(consumer);
			return this;
		}
		
		public Builder withOnEntityAttacked(EnchantmentAttackConsumer consumer) {
			this.entityAttacked = entityAttacked.andThen(consumer);
			return this;
		}
		
		public Builder withOutgoingModifier(DamageModifierFunction mod) {
			outgoingFunc = DamageModifierFunction.join(outgoingFunc, mod, (d1,d2)->d1*d2);
			return this;
		}
		
		public Builder withIncomingModifier(DamageModifierFunction mod) {
			incomingFunc = DamageModifierFunction.join(incomingFunc, mod, (d1,d2)->d1*d2);
			return this;
		}
		
		public Builder withMaxLevel(int level) {
			this.maxLevel = level;
			return this;
		}
		
		public Builder withEffectiveSlots(EnumSet<EquipmentSlotType> slots) {
			this.slots = slots;
			return this;
		}
		
		public Builder withRarity(Rarity r) {
			this.rarity = r;
			return this;
		}
		
		public Builder withType(EnchantmentType type) {
			this.type = type;
			return this;
		}
		
		public Builder withRegistryName(ResourceLocation loc) {
			this.name = loc;
			return this;
		}
		
		public Builder withDisplayName(ITextComponent dispName) {
			this.displayName = dispName;
			return this;
		}
		
		public Builder withEquipAction(BiConsumer<ItemStack,LivingEntity> consumer) {
			this.equipFunc = equipFunc.andThen(consumer);
			return this;
		}
		
		public Builder withUnequipAction(BiConsumer<ItemStack,LivingEntity> consumer) {
			this.unequipFunc = unequipFunc.andThen(consumer);
			return this;
		}
		
		public Builder withTickAction(BiConsumer<ItemStack,LivingEntity> consumer) {
			this.tickFunc = tickFunc.andThen(consumer);
			return this;
		}
		
		public Builder asTreasureEnchantment() {
			this.treasure = true;
			return this;
		}
		
		public Builder asCurse() {
			this.curse = true;
			return this;
		}
		
		public Gac14Enchantment build() {
			return new Gac14Enchantment(rarity, type, slots.stream().toArray(EquipmentSlotType[]::new), checkApply, checkApply, checkApplyWith, entityAttacked, entityAttacked, maxLevel, treasure, curse, displayName, name, incomingFunc, incomingFunc, equipFunc, equipFunc, equipFunc);
		}
		
	}
	
	public static Stream<Enchantment> getEnchantmentsOnItem(ItemStack s){
		return provider.getEnchantmentsOnItem(s);
	}
	


	@Override
	public int getMinLevel() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return maxLevel;
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}
	
	public ITextComponent getDisplayName() {
		return displayName;
	}


	@Override
	public boolean canApply(ItemStack stack) {
		// TODO Auto-generated method stub
		return checkApply.test(stack)&&super.canApply(stack);
	}



	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		// TODO Auto-generated method stub
		return checkApplyWith.test(ench)&&super.canApplyTogether(ench);
	}

	
	public void onItemEquipped(ItemStack stack,LivingEntity user) {
		this.equipFunc.accept(stack,user);
	}
	
	public void onItemUnequipped(ItemStack stack,LivingEntity user) {
		this.unequipFunc.accept(stack, user);
	}
	
	public void onUpdate(ItemStack stack,LivingEntity user) {
		this.tickFunc.accept(stack, user);
	}

	public void onEntityDamaged(LivingEntity user, Entity target, ItemStack level) {
		entityAttacked.accept(user, target, level);
	}



	public void onUserHurt(LivingEntity user, Entity attacker, ItemStack level) {
		userAttacked.accept(user, attacker, level);
	}



	@Override
	public boolean isTreasureEnchantment() {
		// TODO Auto-generated method stub
		return treasure;
	}



	@Override
	public boolean isCurse() {
		// TODO Auto-generated method stub
		return curse;
	}



	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		// TODO Auto-generated method stub
		return checkEnchant.test(stack);
	}



	@Override
	public boolean isAllowedOnBooks() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public double getOutgoingDamageModifier(DamageSource src,Entity target, ItemStack level) {
		return outgoingMod.apply(src, target, level);
	}
	
	public double getIncomingDamageModifier(DamageSource src,Entity target,ItemStack level) {
		return incomingMod.apply(src, target, level);
	}
	
	
	
	
	public static void applyToItem(Enchantment ench,ItemStack stack,int level) {
		provider.applyToItem(ench, stack,level);
	}
	
	public static boolean enchantmentExists(ResourceLocation loc) {
		return provider.hasEnchantment(loc);
	}
	
	public static Enchantment getEnchantment(ResourceLocation loc) {
		return provider.getEnchantment(loc);
	}
	
	public static void applyRandomToItem(Stream<Enchantment> stream, ItemStack stack,Random rand) {
		Enchantment ench = stream
				.filter(e->e.canApply(stack))
				.parallel()
				.sequential()
				.reduce((e1,e2)->rand.nextBoolean()?e1:e2)
				.orElseThrow(RuntimeException::new);
		applyToItem(ench,stack,rand.nextInt(ench.getMaxLevel()));
	}
	

}
