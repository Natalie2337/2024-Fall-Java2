package implementation;

import declaration.CoreUtil;
import declaration.cmp.Compare;
import declaration.condition.*;
import declaration.database.Database;
import declaration.io.CsvReader;
import declaration.query.extension.Ext;
import declaration.query.extension.NormalFormatOrder;
import declaration.query.extension.Range;
import declaration.query.extension.Sort;
import declaration.stock.Field;
import declaration.stock.Stock;
import declaration.stock.factory.StockFactory;
import declaration.stock.factory.StockFactoryProvider;
import declaration.streamsupport.StreamUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static implementation.Main.DailyTradeImpl.nullMinComparator;

public class Main_copy implements CoreUtil {

    static class StockImpl implements Stock {

        String stockCode;
        String stockName;
        LocalDate listDate;
        String companyNameCn;
        String companyNameEn;
        String industryCode1;
        String industryName3;
        String provinceName;
        String cityName;
        Double initialPublicOfferPrice;
        String initialPublicOfferCurrency;
        Double shareInitialPublicOfferNumbers;
        String marketType;

        public static Comparator<StockImpl> fromField(Field field) {
            switch (field) {
                case STOCK_CODE -> {
                    return Comparator.comparing(o -> o.stockCode);
                }
                case LIST_DATE -> {
                    return nullMinComparator(o -> o.listDate);
                }
                case STOCK_NAME -> {
                    return nullMinComparator(o -> o.stockName);
                }
                case COMPANY_NAME -> {
                    return nullMinComparator(o -> o.companyNameCn);
                }
                case COMPANY_EN_NAME -> {
                    return nullMinComparator(o -> o.companyNameEn);
                }
                case INDUSTRY_CODE_A -> {
                    return nullMinComparator(o -> o.industryCode1);
                }
                case INDUSTRY_NAME_C -> {
                    return nullMinComparator(o -> o.industryName3);
                }
                case PROVINCE -> {
                    return nullMinComparator(o -> o.provinceName);
                }
                case CITY -> {
                    return nullMinComparator(o -> o.cityName);
                }
                case IPO_PRICE -> {
                    return nullMinComparator(o -> o.initialPublicOfferPrice);
                }
                case IPO_CURRENCY -> {
                    return nullMinComparator(o -> o.initialPublicOfferCurrency);
                }
                case IPO_SHARES -> {
                    return nullMinComparator(o -> o.shareInitialPublicOfferNumbers);
                }
                case MARKET_TYPE -> {
                    return nullMinComparator(o -> o.marketType);
                }
                default -> {
                    throw new RuntimeException("No such field in stock to sort. ");
                }
            }
        }

        // assert the condition is only related to this stock info
        public boolean handleCondition(Condition condition) {
            if (condition instanceof AndCondition c) {
                var cs = c.conditions();
                return cs.stream().allMatch(this::handleCondition);
            }
            if (condition instanceof OrCondition c) {
                var cs = c.conditions();
                return cs.stream().anyMatch(this::handleCondition);
            }
            if (condition instanceof NotCondition c) {
                var cs = c.condition();
                return !handleCondition(cs);
            }
            if (condition instanceof CodeCondition c) {
                switch (c.field()) {
                    case STOCK_CODE -> {
                        var cc = c.code();
                        return cc.equals(this.stockCode);
                    }
                    case INDUSTRY_CODE_A -> {
                        var cc = c.code();
                        return cc.equals(this.industryCode1);
                    }
                    case MARKET_TYPE -> {
                        var cc = c.code();
                        return cc.equals(this.marketType);
                    }
                    default -> {
                        throw new RuntimeException("No such field in stock info");
                    }
                }
            }
            if (condition instanceof PatternCondition c) {
                switch (c.field()) {
                    case STOCK_CODE -> {
                        var p = c.pattern();
                        return p.matcher(this.stockCode).matches();
                    }
                    case INDUSTRY_CODE_A -> {
                        var p = c.pattern();
                        return this.industryCode1 != null && p.matcher(this.industryCode1).matches();
                    }
                    case MARKET_TYPE -> {
                        var p = c.pattern();
                        return this.marketType != null && p.matcher(this.marketType).matches();
                    }
                    case STOCK_NAME -> {
                        var p = c.pattern();
                        return this.stockName != null && p.matcher(this.stockName).matches();
                    }
                    case COMPANY_NAME -> {
                        var p = c.pattern();
                        return this.companyNameCn != null && p.matcher(this.companyNameCn).matches();
                    }
                    case COMPANY_EN_NAME -> {
                        var p = c.pattern();
                        return this.companyNameEn != null && p.matcher(this.companyNameEn).matches();
                    }
                    case INDUSTRY_NAME_C -> {
                        var p = c.pattern();
                        return this.industryName3 != null && p.matcher(this.industryName3).matches();
                    }
                    case PROVINCE -> {
                        var p = c.pattern();
                        return this.provinceName != null && p.matcher(this.provinceName).matches();
                    }
                    case CITY -> {
                        var p = c.pattern();
                        return this.cityName != null && p.matcher(this.cityName).matches();
                    }
                    case IPO_CURRENCY -> {
                        var p = c.pattern();
                        return this.initialPublicOfferCurrency != null && p.matcher(this.initialPublicOfferCurrency).matches();
                    }
                    default -> {
                        throw new RuntimeException("No such field in stock info");
                    }
                }
            }
            if (condition instanceof DateCondition c) {
                if (Objects.requireNonNull(c.dateField()) == Field.LIST_DATE) {
                    return this.listDate != null && c.order().test(this.listDate, c.time());
                }
                throw new RuntimeException("No such field in stock info");
            }
            if (condition instanceof ValueCondition c) {
                var f = c.field();
                switch (f) {
                    case IPO_PRICE -> {
                        return this.initialPublicOfferPrice != null && c.compare().test(this.initialPublicOfferPrice, c.value());
                    }
                    case IPO_SHARES -> {
                        return this.shareInitialPublicOfferNumbers != null && c.compare().test(this.shareInitialPublicOfferNumbers, c.value());
                    }
                    default -> {
                        throw new RuntimeException("No such field in stock info");
                    }
                }
            }
            throw new RuntimeException("No such field in stock info");
        }

        static class Factory implements StockFactory {

            public StreamUtil streamUtil;

            public List<Optional<Integer>> fieldIdx;
            public int size;

            public Factory(String tableHeader, StreamUtil streamUtil) throws StockFactoryProvider.StockFactoryConstructorException  {
                this.streamUtil = Objects.requireNonNull(streamUtil);
                var exception = new StockFactoryProvider.StockFactoryConstructorException();
                // check 'data' is valid (single line)
                {
                    if (tableHeader.contains(System.lineSeparator())) {
                        exception.singleLine = false;
                        throw exception;
                    }
                }
                // check 'data' is valid (csv format)
                List<String> l;
                {
                    try {
                        l = splitAsList(tableHeader);
                    } catch (RuntimeException e) {
                        exception.quotationMarks = false;
                        throw exception;
                    }
                }
                Stream<Optional<Integer>> fieldIdx = fieldNames.stream().map(n -> {
                    var ls = l.stream();
                    var is = IntStream.iterate(0, i -> i + 1).boxed();
                    Stream<Optional<Integer>> zip = streamUtil.zip(ls, is, (s, idx) -> {
                        if (n.equals(s)) {
                            return Optional.of(idx);
                        } else {
                            return Optional.empty();
                        }
                    });
                    var zip2 = streamUtil.filterMap(zip, Function.identity());
                    try {
                        var r = streamUtil.onlyOne(zip2);
                        return Optional.of(r);
                    } catch (StreamUtil.NoElementInStreamException | StreamUtil.MoreThanOneElementInStreamException e) {
                        return Optional.empty();
                    }
                });
                var idx = fieldIdx.toList();
                if (idx.get(0).isEmpty()) {
                    exception.containStockCode = false;
                    throw exception;
                }
                this.fieldIdx = idx;
                this.size = l.size();
            }

            public final static List<String> fieldNames = List.of(
                    "Stkcd",
                    "Stknme",
                    "Listdt",
                    "Conme",
                    "Conme_en",
                    "Indcd",
                    "Nnindcnme",
                    "PROVINCE",
                    "CITY",
                    "Ipoprc",
                    "Ipocur",
                    "Nshripo",
                    "Markettype"
            );

            @Override
            public Stock newStock(String data) throws StockInstantiationException {
                var exception = new StockInstantiationException();
                // check 'data' is valid (single line)
                {
                    if (data.contains(System.lineSeparator())) {
                        exception.singleLine = false;
                        throw exception;
                    }
                }
                // check 'data' is valid (csv format)
                List<String> l;
                try {
                    l = splitAsList(data);
                } catch (RuntimeException e) {
                    exception.quotationMarks = false;
                    throw exception;
                }
                // check 'data' column count is proper
                {
                    var s = l.size();
                    if (s != this.size) {
                        exception.unMatchColumnCount = List.of(this.size, s);
                        throw exception;
                    }
                }
                var length = this.fieldIdx.size();
                var stock = new StockImpl();
                for (int i = 0; i < length; ++i) {
                    var idx = this.fieldIdx.get(i);
                    if (idx.isEmpty()) {
                        continue;
                    }
                    int iGet = idx.get();
                    var actualValue = l.get(iGet);
                    try {
                        switch (i) {
                            case 0 ->
                                    stock.stockCode = actualValue;
                            case 1 ->
                                    stock.stockName = actualValue;
                            case 2 ->
                                    stock.listDate = LocalDate.parse(actualValue, DateTimeFormatter.ofPattern("yyyy/M/d"));
                            case 3 ->
                                    stock.companyNameCn = actualValue;
                            case 4 ->
                                    stock.companyNameEn = actualValue;
                            case 5 ->
                                    stock.industryCode1 = actualValue;
                            case 6 ->
                                    stock.industryName3 = actualValue;
                            case 7 ->
                                    stock.provinceName = actualValue;
                            case 8 ->
                                    stock.cityName = actualValue;
                            case 9 ->
                                    stock.initialPublicOfferPrice = Double.parseDouble(actualValue);
                            case 10 ->
                                    stock.initialPublicOfferCurrency = actualValue;
                            case 11 ->
                                    stock.shareInitialPublicOfferNumbers = Double.parseDouble(actualValue);
                            case 12 ->
                                    stock.marketType = actualValue;
                            default ->
                                    throw new RuntimeException();
                        }
                    } catch (RuntimeException ignored) {
                        if (!(exception.fieldParseFailed instanceof HashSet<Field>)) {
                            exception.fieldParseFailed = new HashSet<>(exception.fieldParseFailed);
                        }
                        var fieldName = fieldNames.get(i);
                        Arrays.stream(Field.values()).filter(f -> f.name().equals(fieldName)).findAny().ifPresent(exception.fieldParseFailed::add);
                    }
                }
                if (!exception.fieldParseFailed.isEmpty()) {
                    throw exception;
                }
                return stock;
            }
        }

        public final static DateTimeFormatter localDateTimeDefaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String getField(Field field) throws NoSuchFieldException, NoSuchDataException {
            var f = getRawField(field);
            switch (field) {
                case STOCK_CODE, INDUSTRY_CODE_A, IPO_CURRENCY, MARKET_TYPE -> {
                    return (String ) f;
                }
                case LIST_DATE -> {
                    return ((LocalDate ) f).format(localDateTimeDefaultFormatter);
                }
                case STOCK_NAME, COMPANY_NAME, COMPANY_EN_NAME, INDUSTRY_NAME_C, PROVINCE, CITY -> {
                    return (String ) f;
                }
                case IPO_PRICE, IPO_SHARES -> {
                    var formatter = new Formatter();
                    var p = formatter.format("%.2f", (Double ) f);
                    var r = p.toString();
                    return r;
                }
                default -> {
                    // impossible
                    throw new RuntimeException();
                }
            }
        }

        @Override
        public Object getRawField(Field field) throws NoSuchDataException, NoSuchFieldException {
            try {
                return switch (field) {
                    case STOCK_CODE -> this.stockCode;
                    case STOCK_NAME -> Objects.requireNonNull(this.stockName);
                    case LIST_DATE -> Objects.requireNonNull(this.listDate);
                    case COMPANY_NAME -> Objects.requireNonNull(this.companyNameCn);
                    case COMPANY_EN_NAME -> Objects.requireNonNull(this.companyNameEn);
                    case INDUSTRY_CODE_A -> Objects.requireNonNull(this.industryCode1);
                    case INDUSTRY_NAME_C -> Objects.requireNonNull(this.industryName3);
                    case PROVINCE -> Objects.requireNonNull(this.provinceName);
                    case CITY -> Objects.requireNonNull(this.cityName);
                    case IPO_PRICE -> Objects.requireNonNull(this.initialPublicOfferPrice);
                    case IPO_CURRENCY -> Objects.requireNonNull(this.initialPublicOfferCurrency);
                    case IPO_SHARES -> Objects.requireNonNull(this.shareInitialPublicOfferNumbers);
                    case MARKET_TYPE -> Objects.requireNonNull(this.marketType);
                    default -> throw new NoSuchFieldException();
                };
            } catch (NullPointerException ignored) {
                throw new NoSuchDataException();
            }
        }

    }

    static class DailyTradeImpl implements Stock {

        String stockCode;
        LocalDate tradeDate;
        Double openPrice;
        Double highestPrice;
        Double lowestPrice;
        Double closePrice;
        Double dividend;

        public boolean handleCondition(Condition condition) {
            // TODO
            throw new UnsupportedOperationException();
        }

        public static <T, R extends Comparable<R>> Comparator<T> nullMinComparator(Function<T, R> map) {
            return (t1, t2) -> {
                var r1 = map.apply(t1);
                var r2 = map.apply(t2);
                if (r1 == null) {
                    return r2 == null ? 0 : -1;
                }
                if (r2 == null) {
                    return 1;
                }
                return r1.compareTo(r2);
            };
        }


        public static Comparator<DailyTradeImpl> fromField(Field field) {
            // TODO
            throw new UnsupportedOperationException();
        }

        @Override
        public String getField(Field field) throws NoSuchDataException, NoSuchFieldException {
            // TODO
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getRawField(Field field) throws NoSuchDataException, NoSuchFieldException {
            // TODO
            throw new UnsupportedOperationException();
        }

        public static class Factory implements StockFactory {

            public static final List<String> fieldNames = List.of(
                    "Stkcd",
                    "Trddt",
                    "Opnprc",
                    "Hiprc",
                    "Loprc",
                    "Clsprc",
                    "Adjprcnd"
            );


            @Override
            public Stock newStock(String line) throws StockInstantiationException {
                // TODO
                throw new UnsupportedOperationException();
            }
        }

    }

    private static List<String> splitAsList(String origin) {
        var rst = new ArrayList<String>();
        var cps = origin.codePoints().toArray();
        var idx = 0;
        var sb = new StringBuilder();
        var isQuote = false;
        while (idx < cps.length) {
            var c = cps[idx];
            switch (c) {
                case '"' :
                    if (!isQuote) {
                        if (!sb.isEmpty()) {
                            throw new RuntimeException("Unexpected quote. ");
                        }
                        isQuote = true;
                    } else {
                        if (idx + 1 < cps.length && cps[idx + 1] == '"') {
                            sb.appendCodePoint(c);
                            idx += 1;
                        } else if (idx + 1 == cps.length || cps[idx + 1] == ',' ){
                            isQuote = false;
                        }
                    }
                    break;
                case ',':
                    if (isQuote) {
                        sb.appendCodePoint(c);
                    } else {
                        rst.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    break;
                default:
                    sb.appendCodePoint(c);
                    break;
            }
            idx += 1;
        }
        if (isQuote) {
            throw new RuntimeException("Unmatched quote. ");
        }
        rst.add(sb.toString());
        return rst;
    }


    @Override
    public CsvReader getCsvReader() {
        // TODO

        throw new UnsupportedOperationException();
    }

    @Override
    public StockFactoryProvider getAbstractStockFactory() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Database getDatabase() {
        return new Database() {
            private boolean state = false;

            private ArrayList<StockImpl> stocks = new ArrayList<>();
            private ArrayList<DailyTradeImpl> trades = new ArrayList<>();

            private Set<String> stockCodes = new HashSet<>();

            record DailyTradeKey(String stockCode, LocalDate date) {}

            private Set<DailyTradeKey> dailyTradeKeys = new HashSet<>();

            @Override
            public InsertResult insert(List<Stock> stocks) throws InvalidInsertStateException {
                if (state) throw new InvalidInsertStateException();
                long cnt = 0;
                List<Stock> failed = new ArrayList<>();
                for (Stock stock : stocks) {
                    if (stock instanceof StockImpl si) {
                        var key = si.stockCode;
                        if (stockCodes.contains(key)) {
                            failed.add(stock);
                        } else {
                            this.stocks.add(si);
                            cnt += 1;
                        }
                    } else if (stock instanceof DailyTradeImpl dt) {
                        var key = new DailyTradeKey(dt.stockCode, dt.tradeDate);
                        if (dailyTradeKeys.contains(key)) {
                            failed.add(stock);
                        } else {
                            dailyTradeKeys.add(key);
                            this.trades.add(dt);
                            cnt += 1;
                        }
                    } else {
                        failed.add(stock);
                    }
                }
                return new InsertResult(cnt, failed);
            }

            @Override
            public Supplier<List<?>> query(Condition condition, Ext... extensions) throws QueryException {
                state = true;
                var conditionFields = getRelatedFields(condition);
                var exts = Arrays.stream(extensions);
                var su = getStreamUtil();
                var p = su.split(exts, (e) -> {
                    if (e instanceof Sort s) {
                        return Optional.of(s);
                    } else {
                        return Optional.empty();
                    }
                });
                var sorts = p.second();
                var p2 = su.split(p.first(), (e) -> {
                    if (e instanceof NormalFormatOrder o) {
                        return Optional.of(o);
                    } else {
                        return Optional.empty();
                    }
                });
                var orders = p2.second();
                var p3 = su.split(p2.first(), (e) -> {
                    if (e instanceof Range l) {
                        return Optional.of(l);
                    } else {
                        return Optional.empty();
                    }
                });
                var ranges = p3.second();
                var nulls = p3.first();
                var a = nulls.findAny().isEmpty();
                if (!a) {
                    // impossible
                    throw new RuntimeException();
                }
                var exception = new QueryException();
                List<Field> orderFields = Collections.emptyList();
                NormalFormatOrder order = null;
                // check order exist or not
                {
                    var set = orders.collect(Collectors.toSet());
                    if (set.size() > 1) {
                        exception.conflictOrders = set;
                        throw exception;
                    }
                    if (set.size() == 1) {
                        order = set.stream().findAny().get();
                        orderFields = order.fields();
                    }
                }
                // check range
                Range r;
                {
                    try {
                        r = su.onlyOne(ranges);
                    } catch (StreamUtil.MoreThanOneElementInStreamException e) {
                        exception.isRangeErr = true;
                        throw exception;
                    } catch (StreamUtil.NoElementInStreamException e) {
                        // do nothing
                        r = null;
                    }
                }
                // check sorts
                List<Sort> sortSorts;
                List<Field> sortFields;
                {
                    sortSorts = sorts.collect(Collectors.toList());
                    sortFields = sortSorts.stream().map(Sort::field).collect(Collectors.toList());
                }
                Set<Field> allFields = new HashSet<>();
                allFields.addAll(conditionFields);
                allFields.addAll(orderFields);
                allFields.addAll(sortFields);
                var errs = allFields.stream().filter(f -> {
                    switch (f) {
                        case STOCK_CODE, STOCK_NAME, LIST_DATE, COMPANY_NAME, COMPANY_EN_NAME, INDUSTRY_CODE_A, INDUSTRY_NAME_C, PROVINCE, CITY, IPO_PRICE, IPO_CURRENCY, IPO_SHARES, MARKET_TYPE -> {
                            return false;
                        }
                        case OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, DIVIDEND_CLOSE_PRICE, TRADE_DATE -> {
                            return false;
                        }
                        default -> {
                            return true;
                        }
                    }
                }).collect(Collectors.toSet());
                if (!errs.isEmpty()) {
                    exception.noSuchFields = errs;
                    throw exception;
                }
                // check if related stock info
                var stockInfo = allFields.stream().anyMatch(f -> {
                    switch (f) {
                        case STOCK_NAME, LIST_DATE, COMPANY_NAME, COMPANY_EN_NAME, INDUSTRY_CODE_A, INDUSTRY_NAME_C, PROVINCE, CITY, IPO_PRICE, IPO_CURRENCY, IPO_SHARES, MARKET_TYPE -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                });
                var tradeInfo = allFields.stream().anyMatch(f -> {
                    switch (f) {
                        case OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, DIVIDEND_CLOSE_PRICE, TRADE_DATE -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                });
                // check if conflict
                if (stockInfo && tradeInfo) {
                    exception.isConditionConflict = true;
                    throw exception;
                }
                if (tradeInfo) {
                    var rr = r;
                    var o = order;
                    return () -> {
                        // trade info
                        var tradePass = new ArrayList<DailyTradeImpl>(trades.stream().filter(t -> t.handleCondition(condition)).toList());
                        // sort ~
                        for (int size = sortSorts.size(); size > 0; size--) {
                            var field = sortSorts.get(size - 1);
                            var comparator = DailyTradeImpl.fromField(field.field());
                            if (!field.isAscending()) {
                                comparator = comparator.reversed();
                            }
                            tradePass.sort(comparator);
                        }
                        // range
                        var subList = rr == null ? tradePass : tradePass.subList(rr.left(), rr.right());
                        // format
                        List<?> rst;
                        if (o != null) {
                            var m = subList.stream().<List<String>>mapMulti((d, rstCall) -> {
                                var os = o.fields().stream();
                                var p4 = su.split(os, f -> {
                                    try {
                                        return Optional.of(d.getField(f));
                                    } catch (Stock.NoSuchDataException e) {
                                        return Optional.empty();
                                    } catch (Stock.NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                if (p4.first().findAny().isPresent()) {
                                    return ;
                                }
                                var l = p4.second().toList();
                                rstCall.accept(l);
                            });
                            rst = m.toList();
                        } else {
                            rst = subList;
                        }
                        return rst;
                    };
                } else {
                    // stock info
                    var rr = r;
                    var o = order;
                    return () -> {
                        var stockPass = new ArrayList<StockImpl>(stocks.stream().filter(t -> t.handleCondition(condition)).toList());
                        // sort ~
                        for (int size = sortSorts.size(); size > 0; size--) {
                            var field = sortSorts.get(size - 1);
                            var comparator = StockImpl.fromField(field.field());
                            if (!field.isAscending()) {
                                comparator = comparator.reversed();
                            }
                            stockPass.sort(comparator);
                        }
                        // range
                        var subList = rr == null ? stockPass : stockPass.subList(rr.left(), rr.right());
                        // format
                        List<?> rst;
                        if (o != null) {
                            var m = subList.stream().<List<String>>mapMulti((d, rstCall) -> {
                                var os = o.fields().stream();
                                var p4 = su.split(os, f -> {
                                    try {
                                        return Optional.of(d.getField(f));
                                    } catch (Stock.NoSuchDataException e) {
                                        return Optional.empty();
                                    } catch (Stock.NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                if (p4.first().findAny().isPresent()) {
                                    return ;
                                }
                                var l = p4.second().toList();
                                rstCall.accept(l);
                            });
                            rst = m.toList();
                        } else {
                            rst = subList;
                        }
                        return rst;
                    };
                }
            }
        };
    }

    // 递归地获取所有相关的字段
    public static List<Field> getRelatedFields(Condition condition) {
        if (condition instanceof AndCondition a) {
            return a.conditions().stream().map(Main_copy::getRelatedFields).flatMap(List::stream).toList();
        } else if (condition instanceof OrCondition a) {
            return a.conditions().stream().map(Main_copy::getRelatedFields).flatMap(List::stream).toList();
        } else if (condition instanceof NotCondition a) {
            return getRelatedFields(a.condition());
        } else if (condition instanceof ValueCondition a) {
            return List.of(a.field());
        } else if (condition instanceof DateCondition a) {
            return List.of(a.dateField());
        } else if (condition instanceof PatternCondition a) {
            return List.of(a.field());
        } else if (condition instanceof CodeCondition a) {
            return List.of(a.field());
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public AtomicConditionFactory getAtomicConditionFactory() {
        return new AtomicConditionFactory() {

            public static void assertField(Field field) throws Stock.NoSuchFieldException {
                switch (field) {
                    case STOCK_CODE, STOCK_NAME, LIST_DATE, COMPANY_NAME, COMPANY_EN_NAME, INDUSTRY_CODE_A, INDUSTRY_NAME_C, PROVINCE, CITY, IPO_PRICE, IPO_CURRENCY, IPO_SHARES, MARKET_TYPE -> {
                        return;
                    }
                    case OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, DIVIDEND_CLOSE_PRICE, TRADE_DATE -> {
                        return ;
                    }
                    default -> {
                        throw new Stock.NoSuchFieldException();
                    }
                }
            }

            @Override
            public ValueCondition newValueCondition(Field field, double value, Compare compare) throws UnmatchException, Stock.NoSuchFieldException {
                assertField(field);
                return switch (field) {
                    case OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, DIVIDEND_CLOSE_PRICE,
                            IPO_PRICE, IPO_SHARES ->
                            new ValueCondition(field, value, compare);
                    default ->
                            throw new UnmatchException();
                };
            }

            @Override
            public DateCondition newDateCondition(Field field, LocalDate time, boolean allowUnknown, Compare compare) throws UnmatchException, Stock.NoSuchFieldException {
                // TODO
                throw new UnsupportedOperationException();
            }

            @Override
            public PatternCondition newPatternCondition(Field field, Pattern pattern) throws UnmatchException, Stock.NoSuchFieldException {
                // TODO
                throw new UnsupportedOperationException();
            }

            @Override
            public CodeCondition newCodeCondition(Field field, String code) throws UnmatchException, Stock.NoSuchFieldException {
                assertField(field);
                return switch (field) {
                    case STOCK_CODE, INDUSTRY_CODE_A -> new CodeCondition(field, code);
                    default -> throw new UnmatchException();
                };
            }

        };
    }

    @Override
    public StreamUtil getStreamUtil() {
        // TODO
        return new StreamUtil() {
            @Override
            public <R, T, U> Stream<R> zip(Stream<T> stream1, Stream<U> stream2, BiFunction<? super T, ? super U, ? extends R> map) {
                // TODO
                throw new UnsupportedOperationException();
            }

            public <R, T, U> Stream<R> flatZip(Stream<T> stream1, Stream<U> stream2, BiFunction<? super T, ? super U, Stream<? extends R>> flatMap) {
                // TODO
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T onlyOne(Stream<T> stream) throws NoElementInStreamException, MoreThanOneElementInStreamException {
                var it = stream.iterator();
                if (!it.hasNext()) {
                    throw new NoElementInStreamException();
                }
                var rst = it.next();
                if (it.hasNext()) {
                    throw new MoreThanOneElementInStreamException();
                }
                return rst;
            }

            static class ChuckIterator<T, U> {

                Deque<T> left = new ArrayDeque<>();
                Deque<U> right = new ArrayDeque<>();
                Spliterator<T> iterator;
                Function<? super T, Optional<? extends U>> filterMap;

                class LeftIterator implements Spliterator<T> {

                    @Override
                    public boolean tryAdvance(Consumer<? super T> consumer) {
                        while (true) {
                            var f = left.pollFirst();
                            if (f != null) {
                                consumer.accept(f);
                                return true;
                            }
                            var b = iterator.tryAdvance((t2) -> {
                                filterMap.apply(t2).ifPresentOrElse(right::addLast, () -> left.addLast(t2));
                            });
                            if (!b) {
                                return false;
                            }
                        }
                    }

                    @Override
                    public Spliterator<T> trySplit() {
                        return null;
                    }

                    @Override
                    public long estimateSize() {
                        return iterator.estimateSize();
                    }

                    @Override
                    public int characteristics() {
                        return ORDERED;
                    }
                }

                class RightIterator implements Spliterator<U> {

                    @Override
                    public boolean tryAdvance(Consumer<? super U> consumer) {
                        while (true) {
                            var f = right.pollFirst();
                            if (f != null) {
                                consumer.accept(f);
                                return true;
                            }
                            var b = iterator.tryAdvance((t2) -> {
                                filterMap.apply(t2).ifPresentOrElse(right::addLast, () -> left.addLast(t2));
                            });
                            if (!b) {
                                return false;
                            }
                        }
                    }

                    @Override
                    public Spliterator<U> trySplit() {
                        return null;
                    }

                    @Override
                    public long estimateSize() {
                        return iterator.estimateSize();
                    }

                    @Override
                    public int characteristics() {
                        return ORDERED;
                    }
                }

            }

            @Override
            public <T, U> Pair<Stream<T>, Stream<U>> split(Stream<T> stream, Function<? super T, Optional<? extends U>> filterMap) {
                var it = new ChuckIterator<T, U>();
                it.iterator = stream.spliterator();
                it.filterMap = filterMap;
                var left = it.new LeftIterator();
                var right = it.new RightIterator();
                return new Pair<>(StreamSupport.stream(left, false), StreamSupport.stream(right, false));
            }
        };
    }

}
