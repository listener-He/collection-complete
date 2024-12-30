package cn.hehouhui.funcation.complete;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 准备
 *
 * @author HeHui
 * @date 2024/12/29
 */
public class Prepare<I, N, E> {

    private Set<SetGet<E, I, N>> setGetList;

    private final Write<I, N> write;

    private final AtomicBoolean isPrepare = new AtomicBoolean(false);

    private final Complete<E> father;

    private Predicate<E> filter = Objects::nonNull;

    protected Prepare(final Function<? super List<I>, ? extends Map<? super I, ? extends N>> nameMapCreator, final Complete<E> father) {
        assert nameMapCreator != null : "nameMapCreator can not be null!";
        this.write = new Write<>(nameMapCreator);
        this.father = father;
    }


    /**
     * 还有高手？
     *
     * @return {@link Complete }<{@link E }>
     */
    public Complete<E> then() {
        return father;
    }

    /**
     * 设置过滤器
     *
     * @param filter 过滤器
     *
     * @return 返回当前对象，支持链式调用
     */
    public Prepare<I, N, E> filter(Predicate<E> filter) {
        if (filter != null) {
            this.filter = this.filter.and(filter);
        }
        return this;
    }


    /**
     * 向Prepare对象中添加一个用于设置和获取节点信息的SetGet对象
     * 此方法允许通过提供一个函数和一个双消费者来定义如何从边对象中获取ID以及如何设置名称
     *
     * @param idGetter   一个函数，用于从边对象中获取节点ID
     * @param nameSetter 一个双消费者，用于将名称设置到边对象中
     *
     * @return 返回Prepare对象本身，允许链式调用
     */
    public Prepare<I, N, E> add(Function<? super E, ? extends I> idGetter,
                                BiConsumer<? super E, ? super N> nameSetter) {
        return add(new SetGet<>(idGetter, nameSetter));
    }

    /**
     * 向当前对象中添加一个新的SetGet实例
     * 此方法用于在当前对象的setGetList中添加一个新的SetGet实例，以实现对边相关信息的管理
     *
     * @param setGet 要添加的SetGet实例，用于管理边相关信息
     *
     * @return 返回当前的Complete对象，支持链式调用
     */
    public Prepare<I, N, E> add(final SetGet<E, I, N> setGet) {
        // 检查传入的SetGet实例是否为null，如果为null，则直接返回当前对象
        if (setGet == null) {
            return this;
        }
        // 检查当前对象的setGetList是否为null，如果为null，则进行初始化
        if (this.setGetList == null) {
            // 使用同步代码块确保线程安全，防止多个线程同时初始化setGetList
            synchronized (this) {
                this.setGetList = new HashSet<>();
            }
        }
        // 将传入的SetGet实例添加到setGetList中
        this.setGetList.add(setGet);
        // 返回当前对象，支持链式调用
        return this;
    }

    /**
     * 准备操作
     * 此方法用于在执行实际操作之前进行必要的准备和检查它通过调用关系图中的所有设置方法来收集数据，
     * 并将它们添加到write集合中，以确保所有必要的数据都已准备好
     *
     * @param target 指定的操作目标，用于从setGetList中获取数据
     */
    protected void init(final E target) {
        if (filter.test(target)) {
            // 使用流处理来获取目标数据，并添加到write集合中
            setGetList.stream().map(s -> s.get(target)).forEach(write::add);
            // 设置准备状态为true，表示已执行准备操作
            isPrepare.set(true);
        }
    }

    /**
     * 结束准备状态并返回一个Consumer对象，该对象将在调用时执行定义的操作
     * 此方法确保在调用finish之前，准备阶段已经通过isPrepare标志完成如果未完成准备，则抛出异常
     * 它还从write中获取当前的Map，并返回一个Consumer，该Consumer将在被接受的每个元素上执行在prepare阶段定义的设置操作
     *
     * @return Consumer对象，用于在给定的元素上执行设置操作
     *
     * @throws RuntimeException 如果在调用finish之前没有进行prepare，则抛出运行时异常
     */
    protected Consumer<E> finish() {
        // 检查是否已进行准备操作，如果没有，则抛出异常
        if (!isPrepare.get()) {
            return target -> {};
        }
        // 获取当前的Map对象，用于读取操作
        Map<? super I, ? extends N> map = write.get();
        // 返回一个Consumer对象，该对象将在每个元素上执行设置操作
        return target -> {
            if (!filter.test(target) || EmptyUtil.isEmpty(map)) {
                return;
            }
            // 遍历所有设置操作，并在目标元素上执行
            setGetList.forEach(s -> {
                N n = map.get(s.get(target));
                if (n != null) {
                    s.set(target, n);
                }
            });
        };
    }

}
