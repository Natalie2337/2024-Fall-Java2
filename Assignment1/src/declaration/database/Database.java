package declaration.database;

import declaration.condition.Condition;
import declaration.query.extension.Ext;
import declaration.query.extension.NormalFormatOrder;
import declaration.stock.Field;
import declaration.stock.Stock;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 数据库引擎接口 <br>
 * Database engine interface <br>
 * <br>
 */
public interface Database {

    /**
     * 插入数据的结果 <br>
     * @param count 插入的数据个数 <br> Number of inserted data <br>
     * @param fails 插入失败的数据 <br> Data that failed to be inserted <br>
     */
    record InsertResult(long count, List<Stock> fails) {}

    /**
     * 非法的插入状态异常 <br>
     * Illegal insert state exception <br>
     * <br>
     * 当数据库引擎处于查询处理阶段时（非数据增加阶段），调用插入方法，抛出此异常 <br>
     * When the database engine is in the query processing stage (non-data addition stage), call the insert method to throw this exception <br>
     * <br>
     */
    final class InvalidInsertStateException extends Exception {}

    /**
     * 数据增加阶段，插入若干数据，并返回插入的数据个数 <br>
     * Data addition stage, insert several data, and return the number of inserted data <br>
     * <br>
     * @param stocks 股票数据列表 <br> Stock data list <br>
     * @return 插入的数据个数 <br> Number of inserted data <br>
     */
    InsertResult insert(List<Stock> stocks) throws InvalidInsertStateException;

    /**
     * 查询处理异常 <br>
     * Query processing exception <br>
     * <br>
     */
    final class QueryException extends Exception {
        /**
         * 多个格式化规则冲突 <br>
         * Multiple formatting rules conflict <br>
         * <br>
         * 当查询操作包含了多个格式化规则时，抛出的异常会设置该字段以记录所有的格式化规则冲突 (尽管它们可能是相容的，该异常也同样会抛出，以简化代码实现) <br>
         * When the query operation contains multiple formatting rules, the exception thrown will set this field to record all formatting rule conflicts
         * (although they may be compatible, the exception will also be thrown to simplify the code implementation) <br>
         */
        public Set<NormalFormatOrder> conflictOrders = Collections.emptySet();

        /**
         * 查询操作中的 Range 限制条件错误 <br>
         * Range restriction error in query operation <br>
         * <br>
         * Ext 中 Range 并不是一个合法的 Range, 即 Range 的长度是负值，或 Range 的下标有负值，或 Ext 中有超过一个 Range <br>
         * Range in Ext is not a legal Range, that is, the length of Range is negative, or the subscript of Range is negative, or there is more than one Range in Ext <br>
         * <br>
         */
        public boolean isRangeErr = false;

        /**
         * 查询操作中的过滤谓词冲突 <br>
         * Filter predicate conflict in query operation <br>
         * <br>
         * 过滤谓词用于隐式地指定查询的表格数据，当多个谓词隐式地指定了不同的表格数据时，该异常的该值会被设置为 true <br>
         * The filter predicate is used to implicitly specify the table data of the query. When multiple predicates implicitly specify different table data, the value of this exception will be set to true <br>
         * <br>
         * 该字段的内涵也被拓展至 Ext 中的其他谓词，如 NormalFormatOrder, Sort 等 <br>
         * The connotation of this field is also extended to other predicates in Ext, such as NormalFormatOrder, Sort, etc. <br>
         * <br>
         */
        public boolean isConditionConflict = false;

        /**
         * 查询操作中涉及到的字段该数据库不支持 <br>
         * The fields involved in the query operation are not supported by the database <br>
         * <br>
         */
        public Set<Field> noSuchFields = Collections.emptySet();
    }

    /**
     * 查询处理阶段，根据条件约束，返回符合条件的数据 <br>
     * Query processing stage, according to the condition constraint, return the data that meets the condition <br>
     * <br>
     * 特别地，数据库引擎在处理数据增加阶段时，调用此方法，自动将自身状态转换为查询处理阶段 <br>
     * The database engine will automatically convert its own state to the query processing stage,
     * by calling this method when processing the data addition stage especially <br>
     * @param condition 条件约束 <br> Condition constraint <br>
     * @param extensions 查询扩展 <br> Extensions <br>
     * @return 符合条件的数据 <br> Data that meets the condition <br>
     */
    Supplier<List<?>> query(Condition condition, Ext... extensions) throws QueryException;

    /**
     * 查询处理并返回处理结果，同 query, 但是直接返回结果 <br>
     * Query processing and return the processing results, same as query, but return the results directly <br>
     * @param condition 条件约束 <br> Condition constraint <br>
     * @param extensions 扩展 <br> Extensions <br>
     * @return 符合条件的数据 <br> Data that meets the condition <br>
     */
    default List<?> select(Condition condition, Ext... extensions) throws QueryException {
        return query(condition, extensions).get();
    }

}
