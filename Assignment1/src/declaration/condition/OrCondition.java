package declaration.condition;

import java.util.List;

/**
 * 或条件约束 <br>
 * Or condition constraint <br>
 * <br>
 * 描述了多个条件约束的「或」关系 <br>
 * Describes the "or" relationship of multiple condition constraints <br>
 * <br>
 * 特别地，当条件约束的个数为 0 时，表示「假」 <br>
 * In particular, when the number of condition constraints is 0, it means "false" <br>
 * <br>
 * @param conditions 条件约束的列表 <br>
 *                   List of condition constraints <br>
 */
public record OrCondition(List<Condition> conditions) implements Condition {
}
