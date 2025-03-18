package declaration.condition;

import declaration.cmp.Compare;
import declaration.stock.Field;

import java.time.LocalDate;

/**
 * 日期条件约束 <br>
 * Date condition constraint <br>
 * <br>
 * 日期条件约束指明对数据中相应的日期字段进行约束，使其与指定的日期相比满足某种关系（早于，等于，晚于）<br>
 * 特别的，对于实际数据中不确定的日期范围，通过 allowUnsure 允许将不确定的关系认定为满足条件 <br>
 * <br>
 * Date condition constraint specifies the constraint on the corresponding date field in the data,
 * so that it satisfies some relationship (earlier than, equal to, later than) compared to the specified date <br>
 * In particular, for the uncertain date range in the actual data,
 * the allowUnsure allows the uncertain relationship to be regarded as satisfying the condition <br>
 * <br>
 * 例如，股票数据 x 记录了一次在 2022-01-14 时的交易记录，对于 (2022-02-01, Lt, false) 的查询，x 满足条件 <br>
 * For example, the stock data x records a transaction record at 2022-01-14, for the query (2022-02-01, Lt, false), x satisfies the condition <br>
 * <br>
 *
 * @param dateField 股票数据中的日期字段 <br> Date field in stock data
 * @param time 指定的日期 <br> Specified date
 * @param order 指定的关系 <br> Specified relationship
 * @param allowUnsure 是否允许不确定的关系 <br> Whether to allow uncertain relationships
 */
public record DateCondition(Field dateField, LocalDate time, Compare order, boolean allowUnsure) implements Condition {
}
