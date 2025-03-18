package declaration.condition;

/**
 * 查询条件约束接口 <br>
 * 该接口的实现类用于约束查询条件的类型，以便于在查询时进行类型检查 <br>
 * <br>
 * Query condition constraint interface <br>
 * The implementation class of this interface is used to constrain the type of query condition, so as to check the type during query <br>
 * <br>
 */
public sealed interface Condition permits AndCondition, CodeCondition, DateCondition, NotCondition, OrCondition, PatternCondition, ValueCondition {
}
