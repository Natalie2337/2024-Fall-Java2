package declaration;

import declaration.condition.AtomicConditionFactory;
import declaration.database.Database;
import declaration.io.CsvReader;
import declaration.stock.factory.StockFactoryProvider;
import declaration.streamsupport.StreamUtil;



/**
 * 核心工具接口 <br>
 * Core tool interface <br>
 * <br>
 * TODO: 这是本次 Assignment 你需要实现的唯一一个接口 <br>
 * TODO: This is the only interface you need to implement in this assignment <br>
 * <br>
 */
public interface CoreUtil {

    /**
     * 获得 csv 文件读取器 <br>
     * According to the given file path, return a csv file reader <br>
     * @return 你实现的 csv 文件读取器 <br> The reader you implemented <br>
     */
    CsvReader getCsvReader();

    /**
     * 获得股票数据工厂提供类 <br>
     * return a stock factory provider <br>
     * <br>
     * @see StockFactoryProvider <br>
     * @return 你实现的股票数据抽象工厂 <br> The stock factory provider you implemented <br>
     */
    StockFactoryProvider getAbstractStockFactory();


    /**
     * 获得数据库引擎 <br>
     * return a database engine <br>
     * <br>
     * @return 你实现的数据库引擎 <br> The database engine you implemented <br>
     */
    Database getDatabase();

    /**
     * 获得原子性数据条件工厂 <br>
     * return an atomic condition factory <br>
     * <br>
     * @return 你实现的原子性数据条件工厂 <br> The atomic condition factory you implemented <br>
     */
    AtomicConditionFactory getAtomicConditionFactory();

    /**
     * 获得流处理工具支持 <br>
     * return a stream support <br>
     * <br>
     * @return 你实现的流处理工具支持 <br> The stream support you implemented <br>
     */
    StreamUtil getStreamUtil();

}
