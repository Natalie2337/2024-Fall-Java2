package declaration.query.extension;

import declaration.stock.Field;

/**
 * 查询排序规则 <br>
 * Query sort rule <br>
 * <br>
 * 该规则是重复的，即一个查询请求中，可以有多个该规则，先后定义了不同字段的排序优先级 <br>
 * This rule is repeated, that is, in a query request, there can be multiple such rules, which define the sorting priority of different fields <br>
 * <br>
 * 出现在更前的规则具有更高的优先级 <br>
 * Rules that appear earlier have higher priority <br>
 * <br>
 * 不同类型的字段的排序规则定义如下： <br>
 * The sorting rules for fields of different types are defined as follows: <br>
 * <br>
 * 1. 字符串类型的字段，按照字典序排序 <br>
 * 1. For fields of string type, sort in dictionary order <br>
 * 2. 数值类型的字段，按照数值大小排序 <br>
 * 2. For fields of numeric type, sort by numeric size <br>
 * 3. 日期类型的字段，按照日期先后排序 <br>
 * 3. For fields of date type, sort by date <br>
 * <br>
 * @see Ext <br>
 * @param field 字段 <br> Field <br>
 * @param isAscending 是否升序 <br> Whether it is ascending <br>
 */
public record Sort(Field field, boolean isAscending) implements Ext {
}
