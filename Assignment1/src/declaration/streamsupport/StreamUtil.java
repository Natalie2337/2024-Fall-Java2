package declaration.streamsupport;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Stream 处理高级支持 <br>
 * Stream advanced support <br>
 * <br>
 */
public interface StreamUtil {

    /**
     * 流合并组合<br>
     * Stream merge combination <br>
     * <br>
     * @param stream1 待合并流 1 <br> Stream to be merged 1 <br>
     * @param stream2 待合并流 2 <br> Stream to be merged 2 <br>
     * @param map 合并组合函数 <br> Merge combination function <br>
     * @return 合并组合后的流 <br> Stream after merge combination <br>
     * @param <R> 合并组合后的流元素类型 <br> Stream element type after merge combination <br>
     * @param <T> 待合并流 1 元素类型 <br> Stream 1 element type to be merged <br>
     * @param <U> 待合并流 2 元素类型 <br> Stream 2 element type to be merged <br>
     */
    <R, T, U> Stream<R> zip(Stream<T> stream1, Stream<U> stream2, BiFunction<? super T, ? super U, ? extends R> map);

    /**
     * 流合并组合扁平化<br>
     * Stream merge combination flatten <br>
     * <br>
     * @param stream1 待合并流 1 <br> Stream to be merged 1 <br>
     * @param stream2 待合并流 2 <br> Stream to be merged 2 <br>
     * @param flatMap 合并组合扁平化函数 <br> Merge combination flatten function <br>
     * @return 合并组合后的流 <br> Stream after merge combination <br>
     * @param <R> 合并组合后的流元素类型 <br> Stream element type after merge combination <br>
     * @param <T> 待合并流 1 元素类型 <br> Stream 1 element type to be merged <br>
     * @param <U> 待合并流 2 元素类型 <br> Stream 2 element type to be merged <br>
     */
    <R, T, U> Stream<R> flatZip(Stream<T> stream1, Stream<U> stream2, BiFunction<? super T, ? super U, Stream<? extends R>> flatMap);

    /**
     * 空流异常 <br>
     * Empty stream exception <br>
     * <br>
     * 流中无元素且尝试断言获取元素时抛出 <br>
     * No element in stream and throw when trying to assert and get element <br>
     */
    final class NoElementInStreamException extends Exception {}

    /**
     * 流中元素不唯一异常 <br>
     * Element in stream is not unique exception <br>
     * <br>
     * 断言流中元素为一个时，流中元素超出一个时抛出 <br>
     * When asserting that the element in the stream is one, throw when the element in the stream exceeds one <br>
     * <br>
     */
    final class MoreThanOneElementInStreamException extends Exception {}

    /**
     * 流中元素唯一断言 <br>
     * Stream element unique assertion <br>
     * <br>
     *
     * @param stream 待断言流 <br> Stream to be asserted <br>
     * @return 流中唯一元素 <br> Unique element in stream <br>
     * @param <T> 流中元素类型 <br> Stream element type <br>
     * @throws NoElementInStreamException 流中无元素 <br> No element in stream <br>
     * @throws MoreThanOneElementInStreamException 流中元素不唯一 <br> Element in stream is not unique <br>
     */
    <T> T onlyOne(Stream<T> stream) throws NoElementInStreamException, MoreThanOneElementInStreamException;

    /**
     * 映射并过滤流中元素 <br>
     * Map and filter elements in stream <br>
     * <br>
     * @param stream 待映射并过滤流 <br> Stream to be mapped and filtered <br>
     * @param filterMap 映射并过滤函数 <br> Map and filter function <br>
     * @return 映射并过滤后的流 <br> Stream after map and filter <br>
     * @param <T> 待映射并过滤流元素类型 <br> Stream element type to be mapped and filtered <br>
     * @param <U> 映射并过滤后的流元素类型 <br> Stream element type after map and filter <br>
     */
    default <T, U> Stream<U> filterMap(Stream<? extends T> stream, Function<? super T, Optional<? extends U>> filterMap) {
        return stream.flatMap(t -> filterMap.apply(t).stream());
    }

    record Pair<A, B> (A first, B second) {}

    /**
     * 分叉过滤映射流中元素 <br>
     * Fork filter map elements in stream <br>
     * <br>
     * 函数将流中元素分叉为两个流，一个流中元素满足过滤映射函数，另一个流中元素不满足过滤映射函数 <br>
     * The function forks the elements in the stream into two streams, one stream whose elements satisfy the filter map function, and the other stream whose elements do not satisfy the filter map function <br>
     * <br>
     * @param stream 待分叉过滤映射流 <br> Stream to be fork filter map <br>
     * @param filterMap 分叉过滤映射函数 <br> Fork filter map function <br>
     * @return 分叉过滤映射后的流 <br> Stream after fork filter map <br>
     * @param <T> 待分叉过滤映射流元素类型 <br> Stream element type to be fork filter map <br>
     * @param <U> 分叉过滤映射后的流元素类型 <br> Stream element type after fork filter map <br>
     */
    <T, U> Pair<Stream<T>, Stream<U>> split(Stream<T> stream, Function<? super T, Optional<? extends U>> filterMap);

}
