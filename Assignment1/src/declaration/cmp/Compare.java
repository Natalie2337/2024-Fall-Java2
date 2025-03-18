package declaration.cmp;

import java.time.LocalDate;

/**
 * 偏序数据类型的大小比较结果。 <br>
 * Compare result of a partial order data type. <br>
 */
public enum Compare {
    /**
     * 小于 <br>
     * Less than <br>
     */
    Lt,
    /**
     * 等于 <br>
     * Equal to <br>
     */
    Eq,
    /** 
     * 大于 <br>
     * Greater than <br>
     */
    Gt;

    public boolean test(int a, int b) {
        return switch (this) {
            case Lt -> a < b;
            case Eq -> a == b;
            case Gt -> a > b;
            default -> throw new RuntimeException("Unknown Compare value.");
        };
    }

    public boolean test(double a, double b) {
        double max = Math.max(a, b);
        double abs = Math.abs(max);
        double diff = Math.abs(a - b);
        double eps = abs * 1e-6;
        return switch (this) {
            case Lt -> a < b && diff > eps;
            case Eq -> diff <= eps;
            case Gt -> a > b && diff > eps;
        };
    }

    public boolean test(LocalDate a, LocalDate b) {
        return switch (this) {
            case Lt -> a.isBefore(b);
            case Eq -> a.isEqual(b);
            case Gt -> a.isAfter(b);
        };
    }
}
