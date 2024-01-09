/*
 * 文 件 名:  PrintUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  j30008421
 * 修改时间： 2022/12/5
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.util.liquibase;

import java.util.HashSet;
import java.util.Set;

/**
 * provide useful methods for print
 *
 * @author j30008421
 * @version [22.2.0, 2022/12/5]
 * @see [相关类/方法]
 * @since [22.2.0]
 */

public final class PrintUtil {
    private static final String TAB = "\t";
    private static final String SUPPRESS = "Suppressed: ";
    private static final String CAUSE = "Caused by: ";

    /**
     * Print this throwable and its stack trace to console
     *
     * @param throwable throwable to be printed
     */
    public static void printCustomStackTrace(Throwable throwable) {
        Set<Throwable> throwableSet = new HashSet<>();
        printRecursiveStackTrace(throwable, throwableSet, null, "");
    }

    private static void printRecursiveStackTrace(Throwable currentThrowable, Set<Throwable> throwableSet,
            StackTraceElement[] parentTrace, String relation) {
        String printStartWith = SUPPRESS.equals(relation) ? TAB : "";
        if (throwableSet != null && !throwableSet.isEmpty()) {
            // if exist，inform user there is a circular reference in exception list
            if (throwableSet.contains(currentThrowable)) {
                System.err.println(
                        printStartWith + relation + "[CIRCULAR REFERENCE: " + currentThrowable.toString() + "]");
                return;
            } else {
                throwableSet.add(currentThrowable);
            }
        } else {
            throwableSet = new HashSet<>();
            throwableSet.add(currentThrowable);
        }
        StackTraceElement[] currentTrace = currentThrowable.getStackTrace();
        int commonLength = getCommonLength(parentTrace, currentTrace);
        // print throwable itself with error message
        System.err.println(printStartWith + relation + currentThrowable.toString());
        for (int i = 0; i < currentTrace.length - commonLength; i++) {
            // print stack trace of current throwable
            System.err.println(printStartWith + TAB + "at " + currentTrace[i]);
        }
        if (commonLength > 0) {
            // ignore common stack trace
            System.err.println(printStartWith + TAB + "..." + commonLength + " more");
        }
        Throwable[] suppressThrowableList = currentThrowable.getSuppressed();
        if (suppressThrowableList != null && suppressThrowableList.length > 0) {
            // print suppressed throwable of current throwable recursively if any
            for (Throwable suppressedThrowable : suppressThrowableList) {
                printRecursiveStackTrace(suppressedThrowable, throwableSet, currentTrace, SUPPRESS);
            }
        }
        Throwable currentCause = currentThrowable.getCause();
        if (currentCause != null) {
            // print cause of current throwable recursively if any
            printRecursiveStackTrace(currentCause, throwableSet, currentTrace, CAUSE);
        }
    }

    private static int getCommonLength(StackTraceElement[] parentTrace, StackTraceElement[] currentTrace) {
        if (currentTrace == null || currentTrace.length == 0 || parentTrace == null || parentTrace.length == 0) {
            return 0;
        }
        int commonLength = 0;
        // compare current stack trace with parent stack trace in reverse order, find the number of elements in common
        for (int i = 0; i < parentTrace.length; i++) {
            if (i > currentTrace.length - 1) {
                return commonLength;
            }
            if (!parentTrace[parentTrace.length - 1 - i].equals(currentTrace[currentTrace.length - 1 - i])) {
                return commonLength;
            }
            commonLength = commonLength + 1;
        }
        return commonLength;
    }
}