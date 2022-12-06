package ��_5;
import com.fasterxml.jackson.databind.ObjectMapper;
import ��_5.dto.GetCategoryResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
public class Utils {
    @Test
    void test() throws IOException {
        GetCategoryResponse getCategoryResponse = new GetCategoryResponse();
        getCategoryResponse.setId(1);
        getCategoryResponse.setTitle("myTitle");

        StringWriter writer = new StringWriter();

        //��� ������ Jackson, ������� ��������� ������������
        ObjectMapper mapper = new ObjectMapper();

        // ���� ������������: 1-����, 2-���
        mapper.writeValue(writer, getCategoryResponse);

        //��������������� ��� ���������� �� StringWriter � ������
        String result = writer.toString();
        System.out.println(result);

        StringReader reader = new StringReader("{\"id\":1,\"title\":\"myTitle\",\"products\":[]}");

        GetCategoryResponse getCategoryResponseReader = mapper.readValue(reader, GetCategoryResponse.class);
    }
}
