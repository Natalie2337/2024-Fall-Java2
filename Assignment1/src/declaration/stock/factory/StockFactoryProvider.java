package declaration.stock.factory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 股票工厂提供类 <br>
 * Stock factory provider <br>
 * <br>
 */
public interface StockFactoryProvider {

    /**
     * 股票工厂构建失败异常 <br>
     * Stock factory constructor exception <br>
     * <br>
     * 你可以通过抛出这个异常来告诉系统你的股票工厂构建失败了 <br>
     * You can throw this exception to tell the system that your stock factory construction failed <br>
     * <br>
     * 你可以通过设置成员变量来告诉系统你的股票工厂构建失败的原因 <br>
     * You can tell the system the reason why your stock factory construction failed by setting member variables <br>
     * <br>
     */
    final class StockFactoryConstructorException extends Exception {
        /**
         * 表头缺失重要字段 `Stkcd` <br>
         * The table header is missing the key field `Stkcd` <br>
         */
        public boolean containStockCode = true;
        /**
         * 表头不是单行的字符串，即内容中包含换行符 <br>
         * The table header is not a single line string, that is, the content contains line breaks <br>
         */
        public boolean singleLine = true;
        /**
         * 表头中的 '"' 字符不成对出现 <br>
         * The '"' character in the table header does not appear in pairs <br>
         */
        public boolean quotationMarks = true;
        /**
         * 表头缺失重要字段 `Trddt` <br>
         * The table header is missing the key field `Trddt` <br>
         */
        public boolean containTradeDate = true;
    }

    /**
     * 获得股票工厂 <br>
     * Get stock factory <br>
     * <br>
     * @param tableHeader 表头 <br> Table header <br>
     * @return 你实现的股票工厂 <br> Your stock factory <br>
     */
    StockFactory newStockFactory(String tableHeader) throws StockFactoryConstructorException;

}
