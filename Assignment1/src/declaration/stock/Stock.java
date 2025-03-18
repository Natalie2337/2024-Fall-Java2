package declaration.stock;

import java.util.Formatter;

/**
 * 股票数据 <br>
 * Stock data <br>
 * <br>
 * 描述了一条有效的股票数据 <br>
 * Describes a valid stock data <br>
 * <br>
 */
public interface Stock {
    /**
     * 无数据异常 <br>
     * No data exception <br>
     * <br>
     * 在访问一条股票数据的相关字段而其中没有数据时抛出 <br>
     * Thrown when accessing a related field of a stock data and there is no data in it <br>
     */
    final class NoSuchDataException extends Exception {}

    /**
     * 无字段异常 <br>
     * No field exception <br>
     * <br>
     * 在不支持该字段查询的股票类型数据中查询该字段时抛出 <br>
     * Thrown when querying the field in the stock type data that does not support the field query <br>
     */
    final class NoSuchFieldException extends Exception {}

    /**
     * 获得一条股票数据中的某个字段的值，同 {@link #getRawField(Field)}, 但是将值格式化为字符串 <br>
     * Get the value of a field in a stock data, same as {@link #getRawField(Field)}, but format the value to string <br>
     * <br>
     * 这个函数会被单独测试，所以请确保它的正确性 <br>
     * This function will be tested separately, so please ensure its correctness <br>
     * <br>
     * 下面是三种不同类型数据的格式化规则 <br>
     * Here are the formatting rules for three different types of data <br>
     * <br>
     * 1. 日期类型，格式化为 "yyyy-MM-dd" <br>
     * 1. Date type, format to "yyyy-MM-dd" <br>
     * 2. 数值类型，格式化为 "0.00" <br>
     * 2. Number type, format to "0.00" <br>
     * 3. 编码类型/字符串类型，不做任何格式化 <br>
     * 3. Code type/String type, no formatting <br>
     * <br>
     * @param field 字段 <br> Field <br>
     * @return 字段值 <br> Field value <br>
     * @throws NoSuchDataException 如果股票数据该字段为空，抛出该异常 <br> If the field of the stock data is empty, throw this exception <br>
     * @throws NoSuchFieldException 如果股票数据类型中没有该字段，抛出该异常 <br> If the field is not in the stock data type, throw this exception <br>
     */
    String getField(Field field) throws NoSuchDataException, NoSuchFieldException;

    /**
     * 获得一条股票数据中的某个字段的原始值 <br>
     * Get the original value of a field in a stock data <br>
     * <br>
     * 可以随意实现，该函数不会被主动测试 <br>
     * Can be implemented arbitrarily, this function will not be tested actively <br>
     * @param field 字段 <br> Field <br>
     * @return 字段原始值 <br> Field original value <br>
     * @throws NoSuchDataException 如果股票数据该字段为空，抛出该异常 <br> If the field of the stock data is empty, throw this exception <br>
     * @throws NoSuchFieldException 如果股票数据类型中没有该字段，抛出该异常 <br> If the field is not in the stock data type, throw this exception <br>
     */
    Object getRawField(Field field) throws NoSuchDataException, NoSuchFieldException;

}
