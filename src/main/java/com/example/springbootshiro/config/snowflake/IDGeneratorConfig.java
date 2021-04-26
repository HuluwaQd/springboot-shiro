package com.example.springbootshiro.config.snowflake;

/**
 * @author wangzw
 */
public class IDGeneratorConfig {
    public static final String STRATEGY_DEFAULT = "53bits";
    public static final String STRATEGY_STANDARD = "standard";
    public static final String WORKER_ID = "workerId";
    public static final String BIZ_ID = "bizId";
    public static final String SNOWFLAKE_STRATEGY = "snowflake_strategy";
    private int workerId = 0;
    private int bizId = 0;
    private String strategy = "53bits";

    public IDGeneratorConfig() {
    }

    public int getWorkerId() {
        return this.workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getBizId() {
        return this.bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
