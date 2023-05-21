package com.tyd.common.util;


import org.springframework.stereotype.Component;

public class SnowFlake {

    // 数据中心(机房) id
    private long datacenterId;
    // 机器ID
    private long workerId;
    // 同一时间的序列
    private long sequence;

    public SnowFlake(long workerId, long datacenterId) {
        this(workerId, datacenterId, 0);
    }

    public SnowFlake(long workerId, long datacenterId, long sequence) {
        // 合法判断
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    // 开始时间戳（2022-9-27 23:5:7）
    private long twepoch = 1664291107L;

    // 机房号，的ID所占的位数 5个bit 最大:11111(2进制)--> 31(10进制)
    private long datacenterIdBits = 5L;

    // 机器ID所占的位数 5个bit 最大:11111(2进制)--> 31(10进制)
    private long workerIdBits = 5L;

    // 5 bit最多只能有31个数字，就是说机器id最多只能是32以内
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 5 bit最多只能有31个数字，机房id最多只能是32以内
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // 同一时间的序列所占的位数 12个bit 111111111111 = 4095  最多就是同一毫秒生成4096个
    private long sequenceBits = 12L;

    // workerId的偏移量
    private long workerIdShift = sequenceBits;

    // datacenterId的偏移量
    private long datacenterIdShift = sequenceBits + workerIdBits;

    // timestampLeft的偏移量
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 序列号掩码 4095 (0b111111111111=0xfff=4095)
    // 用于序号的与运算，保证序号最大值在0-4095之间
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 最近一次时间戳
    private long lastTimestamp = -1L;


    // 获取机器ID
    public long getWorkerId() {
        return workerId;
    }


    // 获取机房ID
    public long getDatacenterId() {
        return datacenterId;
    }


    // 获取最新一次获取的时间戳
    public long getLastTimestamp() {
        return lastTimestamp;
    }


    // 获取下一个随机的ID
    public synchronized long nextId() {
        // 获取当前时间戳，单位毫秒
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        // 去重
        if (lastTimestamp == timestamp) {

            sequence = (sequence + 1) & sequenceMask;

            // sequence序列大于4095
            if (sequence == 0) {
                // 调用到下一个时间戳的方法
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 如果是当前时间的第一次获取，那么就置为0
            sequence = 0;
        }

        // 记录上一次的时间戳
        lastTimestamp = timestamp;

        // 偏移计算
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        // 获取最新时间戳
        long timestamp = timeGen();
        // 如果发现最新的时间戳小于或者等于序列号已经超4095的那个时间戳
        while (timestamp <= lastTimestamp) {
            // 不符合则继续
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlake worker = new SnowFlake(1, 1);
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            System.out.println(worker.nextId());
        }
        long timer = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - timer);
    }

}
