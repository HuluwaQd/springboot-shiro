package com.example.springbootshiro.common.paging;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Amin
 */
public class Paging implements Serializable {
    private static final long serialVersionUID = 3862532812976510098L;
    /**每页显示数*/
    @Max(value = 200,message = "每页设置显示数量最大不能超过200")
    @Min(value = 1,message = "每页设置显示数量最小值位1")
    private int pageSize;
    /**当前页数*/
    @Max(value = Integer.MAX_VALUE)
    @Min(value = 0,message = "当前页数值不能为负")
    private int pageNumber;
    /**总页数*/
    @Max(Integer.MAX_VALUE)
    @Min(value = 0,message = "总页数值不能为负")
    private int totalPages;
    /**记录总数*/
    @Max(Integer.MAX_VALUE)
    @Min(value = 0,message = "记录总数值不能为负")
    private int totalNumber;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Paging paging = (Paging) o;
        return pageSize == paging.pageSize && pageNumber == paging.pageNumber && totalPages == paging.totalPages && totalNumber == paging.totalNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageSize, pageNumber, totalPages, totalNumber);
    }

    @Override
    public String toString() {
        return "Paging{" + "pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", totalPages=" + totalPages + ", " +
                "totalNumber=" + totalNumber + '}';
    }
}
