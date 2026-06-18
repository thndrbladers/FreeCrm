package com.freecrm.pojo;

import java.util.List;

public class ResponseData {
    private boolean success;
    private String version;
    private int total;
    private int start;
    private int limit;
    private Object filter;
    private List<Company> results;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public List<Company> getResults() {
        return results;
    }

    public void setResults(List<Company> results) {
        this.results = results;
    }

	@Override
	public String toString() {
		return "ResponseData [success=" + success + ", version=" + version + ", total=" + total + ", start=" + start
				+ ", limit=" + limit + ", filter=" + filter + ", results=" + results + "]";
	}

   
}