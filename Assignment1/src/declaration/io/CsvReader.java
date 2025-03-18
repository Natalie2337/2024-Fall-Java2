package declaration.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * csv 文件读取器接口 <br>
 * Csv file reader interface <br>
 * <br>
 */
public interface CsvReader {
    /**
     * 编码解析异常 <br>
     * Encoding parsing exception <br>
     * <br>
     */
    final class UnsupportEncodingException extends Exception {}
    /**
     * 读取 csv 文件中的所有数据 <br>
     * Read all data in the csv file <br>
     * @param path csv 文件路径 <br> csv file path <br>
     * @return csv 文件中的所有数据，以行的形式整理返回 <br> All data in the csv file, returned in the form of lines <br>
     * @throws IOException 读取文件过程及处理中出现的 IO 异常 <br> IO exception that occurs during file reading and processing <br>
     * @throws UnsupportEncodingException 读取文件过程中出现的编码解析异常 <br> Encoding parsing exception that occurs during file reading <br>
     */
    List<String> csvRead(Path path) throws IOException, UnsupportEncodingException;
}
