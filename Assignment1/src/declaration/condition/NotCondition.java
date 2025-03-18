package declaration.condition;

/**
 * 非条件约束 <br>
 * Non-condition constraint <br>
 * <br>
 * 描述了一个条件约束的「非」关系 <br>
 * Describes the "non" relationship of a condition constraint <br>
 * <br>
 * @param condition 被否定的条件约束 <br>
 *                  The condition constraint to be negated <br>
 */
public record NotCondition(Condition condition) implements Condition {
}
