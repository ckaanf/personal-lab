package ckaanf.cache.common.serde;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataSerializerTest {
    @Test
    void serde() {
        MyData myData = new MyData("id", "data");
        String serialized = DataSerializer.serializeOrException(myData);

        MyData deserialized = DataSerializer.deserializeOrNull(serialized, MyData.class);

        assertThat(deserialized).isEqualTo(myData);
    }

    record MyData(
            String id, String data
    ) {

    }

}