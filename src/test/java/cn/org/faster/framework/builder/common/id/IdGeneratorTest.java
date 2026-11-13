package cn.org.faster.framework.builder.common.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("雪花 ID 生成器")
class IdGeneratorTest {

    @Test
    @DisplayName("高并发下不重复且为正数")
    void uniqueAndPositive() {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10_000; i++) {
            long id = IdGenerator.next();
            assertThat(id).isPositive();
            ids.add(id);
        }
        assertThat(ids).hasSize(10_000);
    }

    @Test
    @DisplayName("随调用单调不减")
    void monotonic() {
        long previous = IdGenerator.next();
        for (int i = 0; i < 100; i++) {
            long current = IdGenerator.next();
            assertThat(current).isGreaterThan(previous);
            previous = current;
        }
    }
}
