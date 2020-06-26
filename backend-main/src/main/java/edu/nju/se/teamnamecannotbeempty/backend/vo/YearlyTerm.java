package edu.nju.se.teamnamecannotbeempty.backend.vo;


import java.util.List;
import java.util.Objects;

public class YearlyTerm {
    private int year;
    private List<TermItem> termItemList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<TermItem> getTermItemList() {
        return termItemList;
    }

    public void setTermItemList(List<TermItem> termItemList) {
        this.termItemList = termItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearlyTerm that = (YearlyTerm) o;
        return year == that.year &&
                Objects.equals(termItemList, that.termItemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, termItemList);
    }
}
