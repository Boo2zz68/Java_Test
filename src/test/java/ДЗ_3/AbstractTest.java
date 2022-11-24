package ��_3;
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

    @BeforeAll //�� ���� ������
    static void initTest() throws IOException{ //����� ��������� ��� ���� ������
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);
        apiKey = prop.getProperty("apiKey"); //���������� � ������
        baseUrl = prop.getProperty("baseUrl"); //���������� � ������� �����
    }
    public static String getApiKey() {
        return apiKey;
    }
    public static String getBaseUrl() {
        return baseUrl;
    }
}
