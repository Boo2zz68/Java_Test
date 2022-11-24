package ДЗ_3;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest {
    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String apiKey;
    private static String baseUrl;

    @BeforeAll //до всех тестов
    static void initTest() throws IOException{ //общие параметры для всех тестов
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);
        apiKey = prop.getProperty("apiKey"); //переменная с ключем
        baseUrl = prop.getProperty("baseUrl"); //переменная с базовым урлом
    }
    public static String getApiKey() {
        return apiKey;
    }
    public static String getBaseUrl() {
        return baseUrl;
    }
}
