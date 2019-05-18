package github.chorman0773.gac14.enchantment.core.enchantment;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import github.chorman0773.gac14.enchantment.core.function.DamageModifierFunction;
import github.chorman0773.gac14.enchantment.core.function.EnchantmentAttackConsumer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
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
	private BiConsumer<ItemStack,EntityLivingBase> equipFunc;
	private BiConsumer<ItemStack,EntityLivingBase> unequipFunc;
	private BiConsumer<ItemStack,EntityLivingBase> tickFunc;
	
	
	
	protected Gac14Enchantment(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots,Predicate<ItemStack> checkApply,Predicate<ItemStack> checkEnchant,Predicate<Enchantment> checkApplyWith, EnchantmentAttackConsumer entityAttacked,EnchantmentAttackConsumer userAttacked,int maxLevel,boolean treasure,boolean curse,ITextComponent unName,ResourceLocation name,DamageModifierFunction incomingFunc,DamageModifierFunction outgoingFunc,BiConsumer<ItemStack,EntityLivingBase> equipFunc,BiConsumer<ItemStack,EntityLivingBase> unequipFunc,BiConsumer<ItemStack,EntityLivingBase> tickFunc) {
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
		private EnumSet<EntityEquipmentSlot> slots = EnumSet.noneOf(EntityEquipmentSlot.class);
		private Rarity rarity = Rarity.COMMON;
		private EnumEnchantmentType type = EnumEnchantmentType.ALL;
		
		private DamageModifierFunction outgoingFunc = DamageModifierFunction.one();
		private DamageModifierFunction incomingFunc = DamageModifierFunction.one();
		
		private BiConsumer<ItemStack,EntityLivingBase> equipFunc = (t,u)->{};
		private BiConsumer<ItemStack,EntityLivingBase> unequipFunc = (t,u)->{};
		private BiConsumer<ItemStack,EntityLivingBase> tickFunc = (t,u)->{};
		
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
		
		public Builder withEffectiveSlots(EnumSet<EntityEquipmentSlot> slots) {
			this.slots = slots;
			return this;
		}
		
		public Builder withRarity(Rarity r) {
			this.rarity = r;
			return this;
		}
		
		public Builder withType(EnumEnchantmentType type) {
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
		
		public Builder withEquipAction(BiConsumer<ItemStack,EntityLivingBase> consumer) {
			this.equipFunc = equipFunc.andThen(consumer);
			return this;
		}
		
		public Builder withUnequipAction(BiConsumer<ItemStack,EntityLivingBase> consumer) {
			this.unequipFunc = unequipFunc.andThen(consumer);
			return this;
		}
		
		public Builder withTickAction(BiConsumer<ItemStack,EntityLivingBase> consumer) {
			this.tickFunc = tickFunc.andThen(consumer);
			return this;
		}
		
		public Gac14Enchantment build() {
			return new Gac14Enchantment(rarity, type, slots.stream().toArray(EntityEquipmentSlot[]::new), checkApply, checkApply, checkApplyWith, entityAttacked, entityAttacked, maxLevel, curse, curse, displayName, name, incomingFunc, incomingFunc, equipFunc, equipFunc, equipFunc);
		}
		
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
		return checkApply.test(stack);
	}



	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		// TODO Auto-generated method stub
		return checkApplyWith.test(ench)&&super.canApplyTogether(ench);
	}

	
	public void onItemEquipped(ItemStack stack,EntityLivingBase user) {
		this.equipFunc.accept(stack,user);
	}
	
	public void onItemUnequipped(ItemStack stack,EntityLivingBase user) {
		this.unequipFunc.accept(stack, user);
	}
	
	public void onUpdate(ItemStack stack,EntityLivingBase user) {
		this.tickFunc.accept(stack, user);
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		entityAttacked.accept(user, target, level);
	}



	@Override
	public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {
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
	
	public double getOutgoingDamageModifier(DamageSource src,Entity target, int level) {
		return outgoingMod.apply(src, target, level);
	}
	
	public double getIncomingDamageModifier(DamageSource src,Entity target,int level) {
		return incomingMod.apply(src, target, level);
	}
	
	
	

}
