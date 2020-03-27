package edu.nju.se.teamnamecannotbeempty.backend.po;

public interface Aliasable<T> {

    default Aliasable<T> getActual() {
        if (getAlias() == null || getSelf().equals(getAlias()))
            return getSelf();
        else return getAlias().getActual();
    }

    Aliasable<T> getAlias();

    void setAlias(T alias);

    Aliasable<T> getSelf();
}
