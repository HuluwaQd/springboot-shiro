package com.example.springbootshiro.common.paging;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Amin
 */
public class QueryParameters<P> implements Serializable {

    private static final long serialVersionUID = 3664372364345305813L;


    @Valid
    private Paging paging;

    @Valid
    private P parameters;


    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public P getParameters() {
        return parameters;
    }

    public void setParameters(P parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        QueryParameters<?> that = (QueryParameters<?>) o;
        return Objects.equals(paging, that.paging) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paging, parameters);
    }

    @Override
    public String toString() {
        return "QueryParameters{" + "paging=" + paging + ", parameters=" + parameters + '}';
    }
}
