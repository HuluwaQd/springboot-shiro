package com.example.springbootshiro.config.snowflake;



import com.example.springbootshiro.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author wangzw
 */
public abstract class IDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IDGenerator.class);

    private static StandardSnowflakeAlgorithm standardSnowflakeAlgorithm = null;

    private static SnowflakeAlgorithmOf53bits algorithmOf53bits = null;


    public static IDGenerator snowflake53bits(long workerId) {

        workerId = workerId == 0 ? SnowflakeAlgorithmOf53bits.getWorkId() : workerId;

        synchronized (IDGenerator.class) {
            if (algorithmOf53bits == null) {
                algorithmOf53bits = new SnowflakeAlgorithmOf53bits(workerId);
            }
        }

        return algorithmOf53bits;

    }

    public static IDGenerator snowflake(long workerId, long bizId) {

        workerId = workerId == 0 ? StandardSnowflakeAlgorithm.getWorkId() : workerId;
        bizId = bizId == 0 ? StandardSnowflakeAlgorithm.getBizId() : bizId;

        synchronized (IDGenerator.class) {
            if (standardSnowflakeAlgorithm == null)
                standardSnowflakeAlgorithm = new StandardSnowflakeAlgorithm(workerId, bizId);
        }

        return standardSnowflakeAlgorithm;

    }

    public  abstract long nextId();


    private static class StandardSnowflakeAlgorithm extends IDGenerator {

        /**
         * 开始时间截 (2020-03-020)
         */
        private  final long twepoch =
                LocalDateTime.of(2020, 3, 20, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        /**
         * 机器id所占的位数5bit
         */
        private static final long workerIdBits = 5L;

        /**
         * 业务标识id所占的位数 5bit
         */
        private static final long bizIdBits = 5L;

        /**
         * 支持的最大机器id，结果是0 -- 31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
         */
        private static final long maxWorkerId = ~(-1L << workerIdBits);

        /**
         * 支持的最大数据标识id，结果是8 -- 31
         */
        private static final long maxBizId = ~(-1L << bizIdBits);

        /**
         * 序列在id中占的位数 12bit
         */
        private static final long sequenceBits = 12L;

        /**
         * 机器ID向左移12位
         */
        private static final long workerIdShift = sequenceBits;

        /**
         * 数据标识id向左移17位(12+5)
         */
        private static final long bizIdShift = sequenceBits + workerIdBits;

        /**
         * 时间截向左移22位(5 + 5 + 12)
         */
        private static final long timestampLeftShift = sequenceBits + workerIdBits + bizIdBits;

        /**
         * 生成序列的掩码4096
         */
        private static final long sequenceMask = ~(-1L << sequenceBits);

        /**
         * 工作机器ID(0~31)
         */
        private long workerId = getWorkId();

        /**
         * 数据中心ID(0~31)
         */
        private long bizId = getBizId();

        /**
         * 毫秒内序列(0~4096)
         */
        private long sequence = 0L;

        /**
         * 上次生成ID的时间截
         */
        private static long lastTimestamp = -1L;

        //==============================Constructors=====================================

        /**
         * 构造函数
         *
         * @param workerId 工作ID (0~31)
         * @param bizId    业务ID (0~31)
         */
        private StandardSnowflakeAlgorithm(long workerId, long bizId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0",
                        maxWorkerId));
            }
            if (bizId > maxBizId || bizId < 0) {
                throw new IllegalArgumentException(String.format("bizId can't be greater than %d or less than 0",
                        maxBizId));
            }
            this.workerId = workerId;
            this.bizId = bizId;
        }

        // ==============================Methods==========================================

        /**
         * 获得下一个ID (该方法是线程安全的)
         *
         * @return SnowflakeId
         */
        @Override
        public synchronized long nextId() {
            long timestamp = milliTimeGen();

            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d " +
                        "milliseconds", lastTimestamp - timestamp));
            }

            //如果是同一时间生成的，则进行毫秒内序列
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                //毫秒内序列溢出
                if (sequence == 0) {
                    //阻塞到下一个毫秒,获得新的时间戳
                    timestamp = tilNextMillis(lastTimestamp);
                }
            }
            //时间戳改变，毫秒内序列重置
            else {
                sequence = 0L;
            }

            //上次生成ID的时间截
            lastTimestamp = timestamp;

            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twepoch) << timestampLeftShift) | (bizId << bizIdShift) | (workerId << workerIdShift) | sequence;
        }

        /**
         * 阻塞到下一个毫秒，直到获得新的时间戳
         *
         * @param lastTimestamp 上次生成ID的时间截
         * @return 当前时间戳
         */
        private long tilNextMillis(long lastTimestamp) {
            long timestamp = milliTimeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = milliTimeGen();
            }
            return timestamp;
        }

        /**
         * 返回以毫秒为单位的当前时间
         *
         * @return 当前时间(毫秒)
         */
        private static long milliTimeGen() {
            return System.currentTimeMillis();
        }

        private static long getWorkId() {
            try {
                String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                int[] ints = StringUtils.toCodePoints(hostAddress);
                int sums = 0;
                for (int b : ints) {
                    sums += b;
                }
                return sums % 32;
            } catch (Exception e) {
                // 如果获取失败，则使用随机数备用
                return Utils.nextLong(0, 31);
            }
        }

        private static long getBizId() {
            int[] ints = StringUtils.toCodePoints(Utils.getHostName());
            int sums = 0;
            for (int i : ints) {
                sums += i;
            }
            return  sums % 32;
        }

    }



    /**
     * 53 bits unique id:
     *
     * |--------|--------|--------|--------|--------|--------|--------|--------|
     * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
     * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
     * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
     * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
     *
     * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
     *
     * Maximum TS = 11111_11111111_11111111_11111111_111
     *
     * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
     *
     * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
     *
     * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
     */
    private static class SnowflakeAlgorithmOf53bits extends IDGenerator {


        private static final long OFFSET = LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

        private static final long MAX_NEXT = 0b11111_11111111_111L;

        /**
         * 机器id所占的位数6bit,64台机器
         */
        private static final long workerIdBits = 6L;

        private static final long sequenceBits = 15L;

        private static final long timestampLeftShift = sequenceBits + workerIdBits;

        private static final long maxWorkerId = ~(-1L << workerIdBits);

        private long workerId ;

        private static long offset = 0;

        private static long lastEpoch = 0;

        private SnowflakeAlgorithmOf53bits(long workerId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0",
                        maxWorkerId));
            }
            this.workerId = workerId;
        }


        @Override
        public long nextId() {
            return getNextId(timeGen());
        }

        private  long timeGen() {
            return System.currentTimeMillis() / 1000;
        }

        private  synchronized long getNextId(long epochSecond) {
            if (epochSecond < lastEpoch) {
                logger.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
                epochSecond = lastEpoch;
            }
            if (lastEpoch != epochSecond) {
                lastEpoch = epochSecond;
                reset();
            }
            offset++;
            long next = offset & MAX_NEXT;
            if (next == 0) {
                logger.warn("maximum id reached in 1 second in epoch: " + epochSecond);
                return getNextId(epochSecond + 1);
            }
            return generateId(epochSecond, next, workerId);
        }

        private static void reset() {
            offset = 0;
        }

        private long generateId(long epochSecond, long next, long workerId) {
            return ((epochSecond - OFFSET) << timestampLeftShift) | (next << workerIdBits) | workerId;
        }

        private static long getWorkId() {
            try {
                int[] ints = StringUtils.toCodePoints(Inet4Address.getLocalHost().getHostAddress());
                int sums = 0;
                for (int b : ints) {
                    sums += b;
                }
                return sums % 64;
            } catch (Exception e) {
                return Utils.nextLong(0, 63);
            }
        }


    }
}
