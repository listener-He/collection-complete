package cn.hehouhui.funcation.complete;


import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * set get函数
 *
 * @author HeHui
 * @date 2024/11/19
 */
public class SetGet<E, I, N> {

    private final Function<? super E, ? extends I> idGetter;

    private final BiConsumer<? super E, ? super N> nameSetter;

    public SetGet(final Function<? super E, ? extends I> idGetter, final BiConsumer<? super E, ? super N> nameSetter) {
        assert idGetter != null : "idGetter must not be null";
        assert nameSetter != null :  "nameSetter must not be null";
        this.idGetter = idGetter;
        this.nameSetter = nameSetter;
    }


    public I get(E target) {
        return idGetter.apply(target);
    }

    public void set(E target, N value) {
        nameSetter.accept(target, value);
    }
}
