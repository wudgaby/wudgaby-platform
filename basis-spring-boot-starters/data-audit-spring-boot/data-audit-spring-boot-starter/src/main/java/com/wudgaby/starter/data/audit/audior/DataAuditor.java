package com.wudgaby.starter.data.audit.audior;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.ListCompareAlgorithm;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 15:47
 * @desc :
 */
public class DataAuditor {
    private static class JaversHolder {
        private static final Javers JAVERS = JaversBuilder.javers().withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE).build();
    }
    public static final Javers getJavers() {
        return JaversHolder.JAVERS;
    }

    public static List<Change> compare(Object oldVersion, Object currentVersion){
        return JaversHolder.JAVERS.compare(oldVersion, currentVersion).getChanges();
    }
}
