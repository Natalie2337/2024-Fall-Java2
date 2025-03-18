package declaration.stock.factory;

import declaration.stock.Field;
import declaration.stock.Stock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 股票数据工厂 <br>
 * Stock factory <br>
 */
public interface StockFactory {

    /**
     * 股票数据创建失败异常 <br>
     * Stock instantiation exception <br>
     */
    final class StockInstantiationException extends Exception {
        /**
         * 列数不匹配 <br>
         * Column count not match <br>
         * <br>
         * 如果输入的股票数据列数不匹配，该字段将会保存一个长度为二的列表，列表的第一个元素为输入的股票数据列数，第二个元素为实际的股票数据列数 <br>
         * If the column count of input stock data not match, this field will save a list with length 2, the first element of the list is the column count of input stock data, the second element is the actual column count of stock data <br>
         * <br>
         */
        public List<Integer> unMatchColumnCount = Collections.emptyList();
        /**
         * 是否为单行数据 <br>
         * Is single line <br>
         */
        public boolean singleLine = true;
        /**
         * 无法被解析的股票字段 <br>
         * Unparsable stock fields <br>
         * <br>
         * 注意，缺失的字段不被认为是错误，而解析失败的字段才会被认为是错误 <br>
         * Note that missing fields are not considered as errors, only fields that fail to parse are considered as errors <br>
         * <br>
         */
        public Set<Field> fieldParseFailed = Collections.emptySet();

        /**
         * '"' 没有成对出现 <br>
         * '"' not appear in pairs <br>
         * <br>
         */
        public boolean quotationMarks = true;
        // public Object containTradeDate;
    }

    /**
     * 从一条股票数据中创建一个股票对象 <br>
     * Create a stock object from a stock data <br>
     * @param line 一条股票数据 <br> A stock data <br>
     * @return 股票数据对象 <br> Stock object <br>
     */
    Stock newStock(String line) throws StockInstantiationException;

}
