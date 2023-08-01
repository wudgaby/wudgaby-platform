package com.wudgaby.starter.data.audit.audior;

import org.javers.core.diff.Change;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 16:46
 * @desc :
 */

@FunctionalInterface
public interface DataAuditConsumer {
    /**
     * 消费
     * @param paramBiFunction
     */
    void accept(BiFunction<Object, Object, List<Change>> paramBiFunction);
}
