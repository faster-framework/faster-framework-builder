package cn.org.faster.framework.builder.common.id;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 轻量雪花 ID 生成器，用于生成权限 SQL 中的主键。
 *
 * <p>原先依赖框架 {@code Sequence.next()}；这里用 JDK 原生实现，保持全局唯一、趋势递增。</p>
 *
 * @author zhangbowen
 */
public final class IdGenerator {

    /**
     * 起始时间戳：2024-01-01T00:00:00Z。
     */
    private static final long EPOCH = 1704067200000L;
    private static final long NODE_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private static final long NODE_ID = ThreadLocalRandom.current().nextLong(1L << NODE_BITS);

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;

    private IdGenerator() {
    }

    /**
     * @return 全局唯一、随时间递增的正整数 ID
     */
    public static synchronized long next() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            timestamp = lastTimestamp;
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << (NODE_BITS + SEQUENCE_BITS))
                | (NODE_ID << SEQUENCE_BITS)
                | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
