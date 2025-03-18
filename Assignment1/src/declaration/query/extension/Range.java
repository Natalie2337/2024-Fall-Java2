package declaration.query.extension;

/**
 * 查询范围约束 <br>
 * Query range constraint <br>
 * <br>
 * 该规则是唯一的，即一个查询请求中，至多只能有一个该规则，或没有该规则，即不限制查询范围 <br>
 * This rule is unique, that is, in a query request, there can be at most one such rule, or there is no such rule, that is, the query range is not limited <br>
 * <br>
 * 如果该规则的范围大于了实际的数据范围，则超出的部分会被忽略，即相当于总是输出两个区间的交集 <br>
 * If the range of this rule is greater than the actual data range, the excess part will be ignored, that is, it is equivalent to always outputting the intersection of the two intervals <br>
 * <br>
 * 关于边界的规则，与常见的数学规则相同，即左闭右开 <br>
 * The rules about boundaries are the same as common mathematical rules, that is, left closed and right open <br>
 * <br>
 * @param left 左边界 <br> Left boundary <br>
 * @param right 右边界 <br> Right boundary <br>
 */
public record Range(int left, int right) implements Ext {
}
