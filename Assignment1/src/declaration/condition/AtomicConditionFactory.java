package declaration.condition;

import declaration.cmp.Compare;
import declaration.stock.Field;
import declaration.stock.Stock;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * 原子条件工厂 <br>
 * Atomic condition factory <br>
 * <br>
 * 用于创建原子条件约束 <br>
 * Used to create atomic condition constraints <br>
 * <br>
 */
public interface AtomicConditionFactory {

    /**
     * 字段类型不匹配异常 <br>
     * Field type mismatch exception <br>
     * <br>
     * 字段本身应具有的类型属性与约束的类型属性不匹配时抛出 <br>
     * Thrown when the type attribute of the field itself does not match the type attribute of the constraint <br>
     * <br>
     */
    final class UnmatchException extends Exception {}

    /**
     * 创建一个值条件约束 <br>
     * @param field 字段 <br> Field <br>
     * @param value 值 <br> Value <br>
     * @param compare 比较方式 <br> Comparison method <br>
     * @throws UnmatchException 字段类型不匹配异常 <br> Field type mismatch exception <br>
     * @throws Stock.NoSuchFieldException 数据库不支持该字段查询 <br> The database does not support the query of this field <br>
     * @return 值条件约束 <br> Value condition constraint <br>
     */
    ValueCondition newValueCondition(Field field, double value, Compare compare) throws UnmatchException, Stock.NoSuchFieldException;

    /**
     * 创建一个日期条件约束 <br>
     * @param field 字段 <br> Field <br>
     * @param time 时间 <br> Time <br>
     * @param allowUnknown 是否允许未知 <br> Whether to allow unknown <br>
     * @param compare 比较方式 <br> Comparison method <br>
     * @throws UnmatchException 字段类型不匹配异常 <br> Field type mismatch exception <br>
     * @throws Stock.NoSuchFieldException 数据库不支持该字段查询 <br> The database does not support the query of this field <br>
     * @return 日期条件约束 <br> Date condition constraint <br>
     */
    DateCondition newDateCondition(Field field, LocalDate time, boolean allowUnknown, Compare compare) throws UnmatchException, Stock.NoSuchFieldException;

    /**
     * 创建一个模式条件约束 <br>
     * @param field 字符串型字段 <br> Any field <br>
     * @param pattern 模式 <br> Pattern <br>
     * @throws Stock.NoSuchFieldException 数据库不支持该字段查询 <br> The database does not support the query of this field <br>
     * @return 模式条件约束 <br> Pattern condition constraint <br>
     */
    PatternCondition newPatternCondition(Field field, Pattern pattern) throws UnmatchException, Stock.NoSuchFieldException;

    /**
     * 创建一个代码条件约束 <br>
     * @param field 字段 <br> Field <br>
     * @param code 代码 <br> Code <br>
     * @return 代码条件约束 <br> Code condition constraint <br>
     * @throws UnmatchException 字段类型不匹配异常 <br> Field type mismatch exception <br>
     * @throws Stock.NoSuchFieldException 数据库不支持该字段查询 <br> The database does not support the query of this field <br>
     */
    CodeCondition newCodeCondition(Field field, String code) throws UnmatchException, Stock.NoSuchFieldException;

}
