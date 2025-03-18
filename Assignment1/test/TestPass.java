import declaration.CoreUtil;
import declaration.condition.NotCondition;
import declaration.condition.OrCondition;
import declaration.query.extension.NormalFormatOrder;
import declaration.query.extension.Sort;
import declaration.stock.Field;

import java.nio.file.Path;
import java.util.List;

public class TestPass {
    public static void main(String[] args) throws Exception {
        var mainClazz = Class.forName("implementation.Main");
        var mainConstructor = mainClazz.getConstructor();
        var mainIns = (CoreUtil ) mainConstructor.newInstance();
        // read data
        var csvReader = mainIns.getCsvReader();
        var path = Path.of("data");
        var dailyPath = path.resolve("TRD_Dalyr.csv");
        var profilePath = path.resolve("TRD_Co.csv");
        var dailyLines = csvReader.csvRead(dailyPath);
        var profileLines = csvReader.csvRead(profilePath);
        // process data
        var provider = mainIns.getAbstractStockFactory();
        var dailyFactory = provider.newStockFactory(dailyLines.get(0));
        var db = mainIns.getDatabase();
        for (var line : dailyLines.subList(1, dailyLines.size())) {
            var stock = dailyFactory.newStock(line);
            var r = db.insert(List.of(stock));
            assert r.count() == 1;
        }
        var profileFactory = provider.newStockFactory(profileLines.get(0));
        for (var line : profileLines.subList(1, profileLines.size())) {
            var trade = profileFactory.newStock(line);
            var r = db.insert(List.of(trade));
            assert r.count() == 1;
        }
        // any condition
        var any = new NotCondition(new OrCondition(List.of()));
        var showTag = new NormalFormatOrder(List.of(Field.STOCK_CODE, Field.HIGH_PRICE, Field.LOW_PRICE));
        var sort = new Sort(Field.OPEN_PRICE, true);
        var result = db.query(any, showTag, sort);
        var r = result.get();
        assert r.size() == 67828;
        return ;
    }
}
