package edu.nju.se.teamnamecannotbeempty.data.domain;

public interface IDuplication<T extends Aliasable<T>> {
    T getFather();

    T getSon();

    Long getId();

    void setClear(Boolean clear);
}
