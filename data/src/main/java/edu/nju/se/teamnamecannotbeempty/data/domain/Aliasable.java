package edu.nju.se.teamnamecannotbeempty.data.domain;

public interface Aliasable<T> {

    /**
     * 获取有别名的对象真正指向的对象
     *
     * @return 对象的真身
     * @前置条件 无
     */
    T getActual();
}
