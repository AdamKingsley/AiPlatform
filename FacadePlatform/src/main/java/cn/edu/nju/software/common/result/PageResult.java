package cn.edu.nju.software.common.result;


import javafx.scene.control.Pagination;

import java.util.List;

/**
 * Created by mengf on 2018/4/6 0006.
 */
public class PageResult {
    private boolean success = true;
    private List data;
    private int curPage = 1;//当前页
    private long totalRows = -1;
    private int pageSize = 20;//默认

    private String message;
    private String errorMessage;

    private PageResult(boolean success) {
        this.success = success;
    }

//    private PageResult(List data, Pagination page) {
//        this(true);
//
//        this.data = data;
//
//        this.curPage = page.getCurPage();
//        this.pageSize = page.getPageSize();
//        this.totalRows = page.getTotalRows();
//    }
//
//    public static PageResult success(List data, Pagination page) {
//        return new PageResult(data, page);
//    }

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
    public int getCurPage() {
        return curPage;
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
