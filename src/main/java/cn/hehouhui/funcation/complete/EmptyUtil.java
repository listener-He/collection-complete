package cn.hehouhui.funcation.complete;

import java.util.*;

/**
 * 判断空工具
 *
 * @author HeHui
 * @date 2024-11-26 09:38
 */
public class EmptyUtil {

    private EmptyUtil() {
        throw new AssertionError();
    }

    /**
     * 集合是否为空
     *
     * @param collection
     *            集合
     *
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合不为空
     *
     * @param collection
     *            集合
     *
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 为空
     *
     * @param arrays
     *            集合
     *
     * @return boolean
     */
    public static boolean isEmpty(Object[] arrays) {
        return arrays == null || arrays.length == 0;
    }

    /**
     * 对象不为空
     *
     * @param arrays
     *            集合
     *
     * @return boolean
     */
    public static boolean isNotEmpty(Object[] arrays) {
        return !isEmpty(arrays);
    }

    /**
     * 集合是否为空
     *
     * @param map
     *            map集合
     *
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 集合不为空
     *
     * @param map
     *            map集合
     *
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 字符串是否为空
     *
     * @param str
     *            字符串
     *
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 集合不为空
     *
     * @param str
     *            字符串
     *
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 为空
     *
     * @param bean
     *            对象
     *
     * @return boolean
     */
    public static boolean isEmpty(Object bean) {
        return Objects.isNull(bean);
    }

    /**
     * 对象不为空
     *
     * @param bean
     *            豆
     *
     * @return boolean
     */
    public static boolean isNotEmpty(Object bean) {
        return !isEmpty(bean);
    }

    /**
     * 空集合
     *
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 空集合
     *
     * @return {@link Set }<{@link T }>
     */
    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 空map
     *
     * @return {@link Map }<{@link K },{@link V }>
     */
    public static <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    };
}
