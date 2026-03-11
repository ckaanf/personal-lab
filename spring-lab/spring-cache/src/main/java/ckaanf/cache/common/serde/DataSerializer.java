package ckaanf.cache.common.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DataSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeOrException(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("[DataSerializer.serializeOrException] data = {} ", data, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeOrNull(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);

        } catch (Exception e) {
            log.error("[DataSerializer.deserializeOrNull] data = {} ", data, e);
            return null;
        }
    }
}
