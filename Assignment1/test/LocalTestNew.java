import declaration.CoreUtil;
import declaration.cmp.Compare;
import declaration.condition.AndCondition;
import declaration.condition.AtomicConditionFactory;
import declaration.database.Database;
import declaration.io.CsvReader;
import declaration.query.extension.NormalFormatOrder;
import declaration.query.extension.Sort;
import declaration.stock.Field;
import declaration.stock.Stock;
import declaration.stock.factory.StockFactory;
import declaration.stock.factory.StockFactoryProvider;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocalTestNew {

    // OJ needs an absolute path.
    //   public static Path topPath = Path.of("/",  "oj", "static");
    public static Path topPath = Path.of("D:\\IDEA_project\\Fall2023\\Assignment1", "static");

    public CoreUtil core;

    @BeforeEach
    public void initCore() throws Exception {
        try {
            var ori = Class.forName("implementation.Main");
            var i = ori.getDeclaredConstructor().newInstance();
            core = (CoreUtil) i;
            return ;
        } catch (Exception ignored) {
        }
        var clazz = Class.forName("implementation.CoreImpl");
        var i = clazz.getDeclaredConstructor().newInstance();
        core = (CoreUtil) i;
    }

    // check the class is defined
    @Test
    public void classDefinition() {
        Assertions.assertNotNull(core);
    }

    // check the methods are defined
    @Test
    public void methodDefinitions() {
        Assertions.assertNotNull(core.getCsvReader());
        Assertions.assertNotNull(core.getAbstractStockFactory());
        Assertions.assertNotNull(core.getDatabase());
        Assertions.assertNotNull(core.getAtomicConditionFactory());
        Assertions.assertNotNull(core.getStreamUtil());
    }

    @Test
    public void readCsvLineCountCheck() throws Exception {
        var reader = core.getCsvReader();
        Assertions.assertNotNull(reader);
        Path dir = topPath.resolve("localtest1");
        var files = dir.toFile().listFiles();
        Assertions.assertNotNull(files);
        for (var file: files) {
            var simpleName = file.getName();
            var sub = simpleName.substring(7);
            var trim = sub.indexOf('.');
            var num = sub.substring(0, trim);
            var lines = reader.csvRead(file.toPath());
            Assertions.assertNotNull(lines);
            var len = Integer.parseInt(num);
            Assertions.assertEquals(len, lines.size());
        }
    }

    @Test
    public void readCsvIOExceptionCheck() throws Exception {
        var reader = core.getCsvReader();
        Assertions.assertNotNull(reader);
        var dir = topPath.resolve("localtest1");
        var testDir = topPath.resolve("localtest2");
        var files = dir.toFile().list();
        Assertions.assertNotNull(files);
        for (var fs : files ) {
            var testFile = testDir.resolve(fs);
            try {
                var testLines = reader.csvRead(testFile);
                Assertions.fail();
            } catch (IOException ignored) {
            }
        }
    }

    public static String bytesToHex(byte[] bytes) {
        var sb = new StringBuilder();
        for (var b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Test
    public void testReadContent() throws Exception {
        var md = MessageDigest.getInstance("MD5");
        var reader = core.getCsvReader();
        Assertions.assertNotNull(reader);
        var flist = List.of("main.cpp", "calculator.racket", "demo.swift");
        var ans = List.of("fb73562cc897610d4879459bf9fd48c2", "aa7492edc18435d82ab470fcdcb8c3ba", "94f712660a2113ae662371e8fc943fe8");
        var dir = topPath.resolve("localtest3");
        for (int i = 0; i < flist.size(); ++i) {
            var f = flist.get(i);
            var path = dir.resolve(f);
            var lines = reader.csvRead(path);
            Assertions.assertNotNull(lines);
            md.reset();
            lines.forEach(l -> {
                md.update(l.getBytes(StandardCharsets.UTF_8));
                var len = l.length();
                md.update(ByteBuffer.allocate(4).putInt(len).array());
            });
            var r = md.digest();
            var hex = bytesToHex(r);
            var correctAns = ans.get(i);
            Assertions.assertEquals(correctAns, hex);
        }
    }

    public void testWithHeaders(List<String> lines) {
        var factory = core.getAbstractStockFactory();
        Assertions.assertNotNull(factory);
        for (var l: lines) {
            var table = l.substring(2);
            boolean s = l.charAt(0) == 't';
            boolean suc;
            try {
                factory.newStockFactory(table);
                suc = true;
            } catch (StockFactoryProvider.StockFactoryConstructorException e) {
                suc = false;
            }
            Assertions.assertEquals(s, suc);
        }
    }

    @Test
    public void testErrHeaders() throws IOException {
        var dir = topPath.resolve("localtest4");
        var file = "headerErrTest.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testWithHeaders(lines);
    }

    @Test
    public void testCorrectHeaders() throws IOException {
        var dir = topPath.resolve("localtest4");
        var file = "headerCorrectTest.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testWithHeaders(lines);
    }

    @Test
    public void testBasicHeaders() throws IOException {
        var dir = topPath.resolve("localtest4");
        var file = "headerTest.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testWithHeaders(lines);
    }

    @Test
    public void testPatternCondition() throws Exception {
        var dir = topPath.resolve("localtest7");
        var file = "condition2.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testCondition(lines);
    }

    @Test
    public void testValueCondition() throws Exception {
        var dir = topPath.resolve("localtest7");
        var file = "condition1.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testCondition(lines);
    }

    @Test
    public void testDateCondition() throws Exception {
        var dir = topPath.resolve("localtest7");
        var file = "condition3.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testCondition(lines);
    }

    @Test
    public void testCodeCondition() throws Exception {
        var dir = topPath.resolve("localtest7");
        var file = "condition4.txt";
        var f = dir.resolve(file);
        var lines = Files.readAllLines(f);
        Assertions.assertNotNull(lines);
        testCondition(lines);
    }

    public void testCondition(List<String> list) throws IllegalAccessException {
        var factory = core.getAtomicConditionFactory();
        Assertions.assertNotNull(factory);
        for (var l: list) {
            var table = l.split("#");
            boolean s = table[0].equals("t");
            var method = Arrays.stream(factory.getClass().getMethods()).filter(m -> m.getName().equals(table[1])).findFirst().get();
            method.setAccessible(true);
            List<Object> args = new ArrayList<>();
            var field = Field.valueOf(table[2]);
            args.add(field);
            int idx = 3;
            while (idx < table.length) {
                switch (table[idx]) {
                    case "Double":
                        args.add(Double.parseDouble(table[idx + 1]));
                        break;
                    case "Code":
                        args.add(table[idx + 1]);
                        break;
                    case "Pattern":
                        args.add(Pattern.compile(table[idx + 1]));
                        break;
                    case "LocalDate":
                        args.add(LocalDate.parse(table[idx + 1]));
                        break;
                    case "Bool":
                        args.add(Boolean.parseBoolean(table[idx + 1]));
                        break;
                    case "Cmp":
                        args.add(Compare.valueOf(table[idx + 1]));
                        break;
                    default:
                        throw new RuntimeException("Unknown type");
                }
                idx += 2;
            }
            boolean suc;
            try {
                var arg = args.toArray();
                method.invoke(factory, arg);
                suc = true;
            } catch (InvocationTargetException e) {
                suc = false;
            }
            Assertions.assertEquals(s, suc);
        }
    }

    @Test
    public void testDailyTradeGenerated() throws Exception {
        var md5 = MessageDigest.getInstance("MD5");
        var dir = topPath.resolve("localtest8");
        var file = "dailyTrade1.csv";
        var f = dir.resolve(file);
        var lines = core.getCsvReader().csvRead(f);
        Assertions.assertNotNull(lines);
        var factory = core.getAbstractStockFactory();
        Assertions.assertNotNull(factory);
        var sf = factory.newStockFactory(lines.get(0));
        Assertions.assertNotNull(sf);
        var rst = lines.stream().skip(1).map(i -> {
            try {
                return sf.newStock(i);
            } catch (StockFactory.StockInstantiationException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        Assertions.assertNotNull(rst);
        for (Stock stock : rst) {
            var name = stock.getField(Field.STOCK_CODE);
            md5.update(name.getBytes(StandardCharsets.UTF_8));
            md5.update(ByteBuffer.allocate(4).putInt(name.length()).array());
            var date = stock.getField(Field.TRADE_DATE);
            md5.update(date.getBytes(StandardCharsets.UTF_8));
            md5.update(ByteBuffer.allocate(4).putInt(date.length()).array());
        }
        var r = md5.digest();
        var hex = bytesToHex(r);
        Assertions.assertEquals("89ecbd0e26f1911110f0d6be8d52f47b", hex);
    }

    @Test
    public void testCompanyProfileQuery() throws CsvReader.UnsupportEncodingException, IOException, StockFactoryProvider.StockFactoryConstructorException, StockFactory.StockInstantiationException, NoSuchAlgorithmException, Stock.NoSuchFieldException, Stock.NoSuchDataException, Database.InvalidInsertStateException, AtomicConditionFactory.UnmatchException, Database.QueryException {
        var sha = MessageDigest.getInstance("SHA-1");
        var dir = topPath.resolve("localtest5");
        var file = "company2.csv";
        var f = dir.resolve(file);
        var l = core.getCsvReader().csvRead(f);
        var it = l.iterator();
        var factory = core.getAbstractStockFactory();
        Assertions.assertNotNull(factory);
        var sf = factory.newStockFactory(it.next());
        Assertions.assertNotNull(sf);
        var db = core.getDatabase();
        Assertions.assertNotNull(db);
        var la = new ArrayList<Stock>();
        var conditionF = core.getAtomicConditionFactory();
        while (it.hasNext()) {
            var li = it.next();
            var s = sf.newStock(li);
            Assertions.assertNotNull(s);
            la.add(s);
        }
        var r = db.insert(la);
        if (!r.fails().isEmpty()) {
            Assertions.fail("Insert err");
        }
        if (r.count() != la.size()) {
            Assertions.fail("Insert cnt mismatch");
        }
        try {
            var s = db.query(
                    new AndCondition(
                            List.of(conditionF.newValueCondition(Field.HIGH_PRICE, 0.0, Compare.Gt),
                                    conditionF.newPatternCondition(Field.COMPANY_NAME, Pattern.compile("a"))
                            )
                    ));
            Assertions.fail("Do conflict condition query");
        } catch (Database.QueryException ignored) {
        }
        var s = db.query(conditionF.newPatternCondition(Field.STOCK_NAME, Pattern.compile(Pattern.quote("ST"))), new NormalFormatOrder(List.of(Field.STOCK_CODE, Field.STOCK_NAME, Field.CITY)));
        var sr = s.get();
        for (Object o : sr) {
            if (!(o instanceof List)) {
                Assertions.fail("Query result type mismatch");
            }
            var oa = (List<String> ) o;
            var code = oa.get(0);
            var name = oa.get(1);
            var city = oa.get(2);
            sha.update(code.getBytes(StandardCharsets.UTF_8));
            sha.update((byte ) 0);
            sha.update(name.getBytes(StandardCharsets.UTF_8));
            sha.update((byte ) 0);
            sha.update(city.getBytes(StandardCharsets.UTF_8));
            sha.update((byte ) 0);
        }
        var shar = sha.digest();
        var hex = bytesToHex(shar);
        Assertions.assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", hex);
    }

    @Test
    public void testDailyTradeQuery() throws Exception {
        var md5 = MessageDigest.getInstance("MD5");
        var dir = topPath.resolve("localtest8");
        //var file = "dailyTrade2.csv";
        var file = "dailyTrade2.csv";
        var f = dir.resolve(file);
        var lines = core.getCsvReader().csvRead(f);
        Assertions.assertNotNull(lines);
        var factory = core.getAbstractStockFactory();
        Assertions.assertNotNull(factory);
        var sf = factory.newStockFactory(lines.get(0));
        Assertions.assertNotNull(sf);
        List<Stock> rst = lines.stream().skip(1).<Stock>mapMulti((i, r) -> {
            try {
                var s = sf.newStock(i);
                r.accept(s);
            } catch (StockFactory.StockInstantiationException ignored) {
            }
        }).collect(Collectors.toList());
        Assertions.assertEquals(94, rst.size());
        var db = core.getDatabase();
        Assertions.assertNotNull(db);
        var r = db.insert(rst);
        Assertions.assertEquals(91, r.count());
        Assertions.assertEquals(3, r.fails().size());
        var s = db.query(new AndCondition(List.of()), new NormalFormatOrder(List.of(Field.CLOSE_PRICE, Field.HIGH_PRICE)), new Sort(Field.OPEN_PRICE, true));
        var rss = s.get();
        System.out.println(rss);
        Assertions.assertEquals(90, rss.size());

        for (Object o : rss) {
            if (!(o instanceof List)) {
                Assertions.fail("Query result type mismatch");
            }
            var oa = (List<String> ) o;
            var close = oa.get(0);
            var high = oa.get(1);
            md5.update(close.getBytes(StandardCharsets.UTF_8));
            md5.update((byte ) 0);
            md5.update(high.getBytes(StandardCharsets.UTF_8));
            md5.update((byte ) 0);
        }
        var md5r = md5.digest();
        var hex = bytesToHex(md5r);
        Assertions.assertEquals("606e704337b5808e6eb319d6e718351c", hex);
    }

    @Test
    public void testStreamZip() throws IOException {
        var su = core.getStreamUtil();
        Assertions.assertNotNull(su);
        var dir = topPath.resolve("localtest6");
        var sinP = dir.resolve("sin1.txt");
        var sin = new Scanner(sinP);
        var sins = DoubleStream.iterate(0, (a) -> sin.hasNextDouble(), (a) -> sin.nextDouble()).boxed().skip(1);
        var cosP = dir.resolve("cos1.txt");
        var cos = new Scanner(cosP);
        var coss = DoubleStream.iterate(0, (a) -> cos.hasNextDouble(), (a) -> cos.nextDouble()).boxed().skip(1);
        var de = su.zip(sins, coss, (s, c) ->
                s * s + c * c
        );
        de.forEach(v -> {
            Assertions.assertEquals(1, v, 1e-7);
        });
    }

    @Test
    public void testStreamFlatZip() {
        var su = core.getStreamUtil();
        Assertions.assertNotNull(su);
        var idx = IntStream.iterate(0, i -> i + 1);
        var fib = Stream.iterate(new BigInteger[]{BigInteger.ZERO, BigInteger.ONE}, i -> new BigInteger[]{i[1], i[0].add(i[1])}).map(i -> i[1]);
        var fibLimit = fib.limit(100);
        var rst = su.flatZip(idx.boxed(), fibLimit, (i, f) -> {
            if (f.mod(BigInteger.valueOf(2)).intValueExact() == 0) {
                return Stream.of(i);
            } else {
                return Stream.empty();
            }
        });
        var rstl = rst.collect(Collectors.toList());
        var rstActual = IntStream.iterate(2, i -> i + 3).limit(33).boxed().toList();
        Assertions.assertEquals(rstActual, rstl);
    }

    @Test
    public void testStreamFlatZip2() throws NoSuchAlgorithmException {
        var md5 = MessageDigest.getInstance("MD5");
        var su = core.getStreamUtil();
        Assertions.assertNotNull(su);
        var idx = IntStream.iterate(0, i -> i + 1).boxed();
        var idxClone = IntStream.iterate(0, i -> i + 1).boxed();
        var pair = su.flatZip(idx, idxClone, (i, j) -> {
            List<Integer> tm = new ArrayList<>();
            for (int k = 0; k < j; ++k) {
                tm.add(i);
            }
            return tm.stream();
        }).limit(20000).collect(Collectors.toList());
        var md5r = md5.digest();
        var hex = bytesToHex(md5r);

        Assertions.assertEquals("d41d8cd98f00b204e9800998ecf8427e", hex);
    }

}