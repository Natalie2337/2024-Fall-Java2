package declaration.condition;

import declaration.cmp.Compare;
import declaration.stock.Field;

/**
 * 数值条件约束 <br>
 * Numeric condition constraint <br>
 * <br>
 * 描述了一个数值条件约束，包括约束的字段、约束的值、约束的比较方式 <br>
 * Describes a numeric condition constraint, including the constrained field, the constrained value, and the comparison method of the constraint <br>
 * <br>
 * 特别的，对于浮点数的的相等定义为： <br>
 * 两值之差的绝对值小于等于其二者绝对值大者值的 0.001%. <br>
 * 其次才使用一般的浮点数比较方式。 <br>
 * <br>
 * Specifically, the definition of equality for floating-point numbers is: <br>
 * The absolute value of the difference between the two values is less than or equal to 0.001% of the larger of the absolute value of the two values. <br>
 * General floating-point comparison is used only after that. <br>
 * <br>
 * @param field 数值约束字段 <br> Numeric constraint field <br>
 * @param value 数值约束值 <br> Numeric constraint value <br>
 * @param compare 数值约束比较方式 <br> Numeric constraint comparison method <br>
 */
public record ValueCondition(Field field, double value, Compare compare) implements Condition {
}
