package declaration.condition;

import declaration.stock.Field;

/**
 * 「代码」型字段约束 <br>
 * "Code" type field constraint <br>
 * <br>
 * 「代码」型字段约束指明对数据中相应的代码字段进行约束，使其与指定的代码相等 <br>
 * "Code" type field constraint specifies the constraint on the corresponding code field in the data,
 * so that it is equal to the specified code <br>
 * <br>
 * 例如，在查询字段 Stkcd 为 603818 的股票交易信息时，所有它的交易记录都满足条件；而其他股票均不满足条件 <br>
 * For example, when querying the stock trading information with the field Stkcd as 603818,
 * all its trading records satisfy the condition; while other stocks do not satisfy the condition <br>
 * <br>
 *
 * @param field 代码型字段 <br> "Code" type field <br>
 * @param code 指定的代码 <br> Specified code <br>
 */
public record CodeCondition(Field field, String code) implements Condition {
}
