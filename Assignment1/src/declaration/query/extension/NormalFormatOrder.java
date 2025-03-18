package declaration.query.extension;

import declaration.stock.Field;

import java.util.List;

/**
 * 查询输出的字段顺序 <br>
 * Field order of query output <br>
 * <br>
 * 该规则是唯一的，即一个查询请求中，至多只能有一个该规则 <br>
 * This rule is unique, that is, in a query request, there can be at most one such rule <br>
 * <br>
 * 在此规则下，一条数据将会转换为一个 String List, 每个 String 对应一个字段，各个字段按照顺序排列 <br>
 * Under this rule, a piece of data will be converted to a String List, each String corresponds to a field, and the fields are arranged in order <br>
 * <br>
 * 如果一条数据不包含某个字段，则该数据会被隐式地过滤掉，不会被包含到输出结果中 <br>
 * If a piece of data does not contain some field, the data will be implicitly filtered out and will not be included in the output result <br>
 * @see Ext <br>
 * @param fields 字段列表 <br> Field list <br>
 */
public record NormalFormatOrder(List<Field> fields) implements Ext {
}
