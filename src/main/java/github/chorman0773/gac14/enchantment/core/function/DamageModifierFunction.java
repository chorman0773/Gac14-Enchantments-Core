package github.chorman0773.gac14.enchantment.core.function;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

@FunctionalInterface
public interface DamageModifierFunction {
	public double apply(DamageSource src,Entity target,int level);
	
	public default DamageModifierFunction andThen(DoubleUnaryOperator op) {
		return (s,t,l)->op.applyAsDouble(DamageModifierFunction.this.apply(s, t, l));
	}
	
	public static DamageModifierFunction join(DamageModifierFunction f1,DamageModifierFunction f2, DoubleBinaryOperator joinOp) {
		return (s,t,l)->joinOp.applyAsDouble(f1.apply(s, t, l), f2.apply(s, t, l));
	}
	
	public static DamageModifierFunction one() {
		return (s,t,l)->1.0;
	}
	
	public static DamageModifierFunction value(double d) {
		return (s,t,l)->d;
	}
	
	
}
