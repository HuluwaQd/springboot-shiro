package com.example.springbootshiro.common.paging;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Amin
 */
public class QueryResults<T> implements Serializable {

    private static final long serialVersionUID = -5492796698773858925L;

    @Valid
    private Paging paging;

    private List<T> results;


    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryResults<?> that = (QueryResults<?>) o;
        return Objects.equals(paging, that.paging) && Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paging, results);
    }

    @Override
    public String toString() {
        return "QueryResults{" + "paging=" + paging + ", results=" + results + '}';
    }
}
