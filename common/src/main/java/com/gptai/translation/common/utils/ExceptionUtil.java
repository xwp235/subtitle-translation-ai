package com.gptai.translation.common.utils;

import com.gptai.translation.common.constants.StringConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常工具类
 *
 * @author Looly
 */
public class ExceptionUtil {

    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable e) {
        if (null == e) {
            return StringConstants.NULL;
        }
        return String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
    }

    /**
     * 获得消息，调用异常类的getMessage方法
     *
     * @param e 异常
     * @return 消息
     */
    public static String getSimpleMessage(Throwable e) {
        return (null == e) ? StringConstants.NULL : e.getMessage();
    }

    /**
     * 获取当前栈信息
     *
     * @return 当前栈信息
     */
    public static StackTraceElement[] getStackElements() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 获取指定层的堆栈信息
     *
     * @param i 层数
     * @return 指定层的堆栈信息
     */
    public static StackTraceElement getStackElement(int i) {
        return Thread.currentThread().getStackTrace()[i];
    }

    /**
     * 获取指定层的堆栈信息
     *
     * @param fqcn 指定类名为基础
     * @param i    指定类名的类堆栈相对层数
     * @return 指定层的堆栈信息
     */
    public static StackTraceElement getStackElement(String fqcn, int i) {
        final StackTraceElement[] stackTraceArray = Thread.currentThread().getStackTrace();
        final int index = ArrayUtil.matchIndex(ele -> StringUtils.equals(fqcn, ele.getClassName()), stackTraceArray);
        if (index > 0) {
            return stackTraceArray[index + i];
        }
        return null;
    }

    /**
     * 获取入口堆栈信息
     *
     * @return 入口堆栈信息
     * @since 4.1.4
     */
    public static StackTraceElement getRootStackElement() {
        final StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
        return Thread.currentThread().getStackTrace()[stackElements.length - 1];
    }

    /**
     * 堆栈转为单行完整字符串
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(Throwable throwable) {
        return stacktraceToOneLineString(throwable, 3000);
    }

    /**
     * 堆栈转为单行完整字符串
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(Throwable throwable, int limit) {
        Map<Character, String> replaceCharToStrMap = new HashMap<>();
        replaceCharToStrMap.put(StringConstants.C_CR, StringConstants.SPACE);
        replaceCharToStrMap.put(StringConstants.C_LF, StringConstants.SPACE);
        replaceCharToStrMap.put(StringConstants.C_TAB, StringConstants.SPACE);

        return stacktraceToString(throwable, limit, replaceCharToStrMap);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(Throwable throwable) {
        return stacktraceToString(throwable, 3000);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(Throwable throwable, int limit) {
        return stacktraceToString(throwable, limit, null);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable           异常对象
     * @param limit               限制最大长度，&lt;0表示不限制长度
     * @param replaceCharToStrMap 替换字符为指定字符串
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(Throwable throwable, int limit, Map<Character, String> replaceCharToStrMap) {
        final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));

        final String exceptionStr = baos.toString();
        final int length = exceptionStr.length();
        if (limit < 0 || limit > length) {
            limit = length;
        }

        if (MapUtil.isNotEmpty(replaceCharToStrMap)) {
            final StringBuilder sb = new StringBuilder();
            char c;
            String value;
            for (int i = 0; i < limit; i++) {
                c = exceptionStr.charAt(i);
                value = replaceCharToStrMap.get(c);
                if (null != value) {
                    sb.append(value);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            if (limit == length) {
                return exceptionStr;
            }
            return StringUtils.substringBefore(exceptionStr, limit);
        }
    }

    /**
     * 判断是否由指定异常类引起
     *
     * @param throwable    异常
     * @param causeClasses 定义的引起异常的类
     * @return 是否由指定异常类引起
     * @since 4.1.13
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
        return null != getCausedBy(throwable, causeClasses);
    }

    /**
     * 获取由指定异常类引起的异常
     *
     * @param throwable    异常
     * @param causeClasses 定义的引起异常的类
     * @return 是否由指定异常类引起
     * @since 4.1.13
     */
    @SuppressWarnings("unchecked")
    public static Throwable getCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
        Throwable cause = throwable;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeClasses) {
                if (causeClass.isInstance(cause)) {
                    return cause;
                }
            }
            cause = cause.getCause();
        }
        return null;
    }

    /**
     * 判断指定异常是否来自或者包含指定异常
     *
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @return true 来自或者包含
     * @since 4.3.2
     */
    public static boolean isFromOrSuppressedThrowable(Throwable throwable, Class<? extends Throwable> exceptionClass) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, true) != null;
    }

    /**
     * 判断指定异常是否来自或者包含指定异常
     *
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @param checkCause     判断cause
     * @return true 来自或者包含
     * @since 4.4.1
     */
    public static boolean isFromOrSuppressedThrowable(Throwable throwable, Class<? extends Throwable> exceptionClass, boolean checkCause) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, checkCause) != null;
    }

    /**
     * 转化指定异常为来自或者包含指定异常
     *
     * @param <T>            异常类型
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @return 结果为null 不是来自或者包含
     * @since 4.3.2
     */
    public static <T extends Throwable> T convertFromOrSuppressedThrowable(Throwable throwable, Class<T> exceptionClass) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, true);
    }

    /**
     * 转化指定异常为来自或者包含指定异常
     *
     * @param <T>            异常类型
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @param checkCause     判断cause
     * @return 结果为null 不是来自或者包含
     * @since 4.4.1
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T convertFromOrSuppressedThrowable(Throwable throwable, Class<T> exceptionClass, boolean checkCause) {
        if (throwable == null || exceptionClass == null) {
            return null;
        }
        if (exceptionClass.isAssignableFrom(throwable.getClass())) {
            return (T) throwable;
        }
        if (checkCause) {
            Throwable cause = throwable.getCause();
            if (cause != null && exceptionClass.isAssignableFrom(cause.getClass())) {
                return (T) cause;
            }
        }
        Throwable[] throwables = throwable.getSuppressed();
        if (ArrayUtils.isNotEmpty(throwables)) {
            for (Throwable throwable1 : throwables) {
                if (exceptionClass.isAssignableFrom(throwable1.getClass())) {
                    return (T) throwable1;
                }
            }
        }
        return null;
    }

    /**
     * 获取异常链上所有异常的集合，如果{@link Throwable} 对象没有cause，返回只有一个节点的List<br>
     * 如果传入null，返回空集合
     *
     * <p>
     * 此方法来自Apache-Commons-Lang3
     * </p>
     *
     * @param throwable 异常对象，可以为null
     * @return 异常链中所有异常集合
     * @since 4.6.2
     */
    public static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        while (throwable != null && false == list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

    /**
     * 获取异常链中最尾端的异常，即异常最早发生的异常对象。<br>
     * 此方法通过调用{@link Throwable#getCause()} 直到没有cause为止，如果异常本身没有cause，返回异常本身<br>
     * 传入null返回也为null
     *
     * <p>
     * 此方法来自Apache-Commons-Lang3
     * </p>
     *
     * @param throwable 异常对象，可能为null
     * @return 最尾端异常，传入null参数返回也为null
     */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.size() < 1 ? null : list.get(list.size() - 1);
    }

    /**
     * 获取异常链中最尾端的异常的消息，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param th 异常
     * @return 消息
     * @since 4.6.2
     */
    public static String getRootCauseMessage(final Throwable th) {
        return getMessage(getRootCause(th));
    }
}
