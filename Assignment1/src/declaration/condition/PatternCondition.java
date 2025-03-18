package declaration.condition;


import declaration.stock.Field;

import java.util.regex.Pattern;

/**
 * 字段模式匹配条件约束 <br>
 * Field pattern matching condition constraint <br>
 * <br>
 * 字段模式匹配条件约束指明对数据中相应的字段进行约束，使其与指定的模式相匹配 <br>
 * Field pattern matching condition constraint specifies the constraint on the corresponding field in the data,
 * so that it matches the specified pattern <br>
 * <br>
 * 除去「代码型字段」以外，其他的解释型字段也可以被「模式匹配」，例如股票简称 Stknme_en <br>
 * In addition to the "code" type field, other interpretive fields can also be "pattern matched",
 * such as the stock abbreviation Stknme_en <br>
 * <br>
 * @param field 可以「模式匹配」的字段
 * @param pattern 指定的模式
 */
public record PatternCondition(Field field, Pattern pattern) implements Condition {
}
