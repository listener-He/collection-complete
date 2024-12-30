package cn.hehouhui.funcation.complete;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * 写入
 *
 * @author HeHui
 * @date 2024/12/29
 */
public class Write<I, N> {

    private final Function<? super List<I>, ? extends Map<? super I, ? extends N>> nameMapCreator;

    private volatile Set<I> ids;

    private Map<? super I, ? extends N> map;

    private final AtomicBoolean isNew = new AtomicBoolean(false);

    protected Write(final Function<? super List<I>, ? extends Map<? super I, ? extends N>> nameMapCreator) {
        this.nameMapCreator = nameMapCreator;
    }


    /**
     * 添加一个ID到集合中
     * 此方法确保相同的ID只会被添加一次，并且在多线程环境下安全地延迟初始化ID集合
     *
     * @param id 要添加的ID，必须是唯一的标识符
     */
    protected void add(final I id) {
        if (id == null) {
            return;
        }
        // 双重检查锁定模式（Double-Checked Locking）初始化ID集合
        // 这种模式用于延迟初始化，并确保在多线程环境下的高效和线程安全
        if (ids == null) {
            synchronized (this) {
                // 在多个线程同时达到此处时，确保只有一个线程会初始化ids
                if (ids == null) {
                    ids = new HashSet<>();
                }
            }
        }
        // 将ID添加到集合中，自动去重
        ids.add(id);
        isNew.set(true);
    }


    /**
     * 获取一个映射，该映射以I的子类型为键，以N的实现类型为值
     * 此方法用于根据当前设置的标识符列表生成一个相应的映射
     * 如果标识符列表为空，则返回一个空映射
     *
     * @return 返回一个映射，如果输入标识符列表为空，则返回空映射
     */
    protected synchronized Map<? super I, ? extends N> get() {
        // 检查是否需要创建新的映射
        if (!isNew.get()) {
            // 如果不需要创建新映射，返回当前映射或空映射
            return map == null ? EmptyUtil.emptyMap() : map;
        }
        // 检查标识符列表是否为空，如果为空则返回一个空映射
        if (EmptyUtil.isEmpty(ids)) {
            return EmptyUtil.emptyMap();
        }
        // 使用当前设置的标识符列表创建一个新的映射
        this.map = nameMapCreator.apply(new ArrayList<>(ids));
        // 检查新创建的映射是否为空，如果为空则返回空映射
        if (this.map == null) {
            return EmptyUtil.emptyMap();
        }
        // 返回新创建的映射
        return map;
    }
}
