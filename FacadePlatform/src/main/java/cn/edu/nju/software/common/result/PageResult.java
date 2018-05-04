package cn.edu.nju.software.common.result;


import javafx.scene.control.Pagination;

import java.util.List;

/**
 * Created by mengf on 2018/4/6 0006.
 */
public class PageResult {
    private boolean success = true;
    private List data;
    private Integer draw;
    private int currentPage = 1;//当前页
    private long totalRows = -1;
    private int pageSize = 20;//默认
    private int pages;

    private String message;
    private String errorMessage;

    public PageResult(PageInfo pageInfo){
        success=true;
        data = pageInfo.getList();
        currentPage = pageInfo.getPageNum();
        totalRows = pageInfo.getTotal();
        pageSize = pageInfo.getPageSize();
        pages = pageInfo.getPages();
    }

    public PageResult(PageInfo pageInfo,Integer draw){
        success=true;
        data = pageInfo.getList();
        currentPage = pageInfo.getPageNum();
        totalRows = pageInfo.getTotal();
        pageSize = pageInfo.getPageSize();
        pages = pageInfo.getPages();
        this.draw = draw;
    }

    private PageResult(boolean success) {
        this.success = success;
    }


    public static PageResult error(String errorMessage) {
        PageResult result = new PageResult(false);
        result.errorMessage = errorMessage;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }
    public List getData() {
        return data;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public long getTotalRows() {
        return totalRows;
    }
    public int getPageSize() {
        return pageSize;
    }
    public String getMessage() {
        return message;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

}
