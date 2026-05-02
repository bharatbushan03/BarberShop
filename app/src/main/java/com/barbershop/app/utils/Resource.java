package com.barbershop.app.utils;

/**
 * Generic wrapper for data exposed via LiveData.
 * Encapsulates data state: loading, success, error.
 * 
 * @param <T> Type of data being wrapped
 */
public class Resource<T> {
    
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
    
    public final Status status;
    public final T data;
    public final String message;
    
    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
    
    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }
    
    public static <T> Resource<T> error(String msg, T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }
    
    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
    
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
    
    public boolean isLoading() {
        return status == Status.LOADING;
    }
    
    public boolean isError() {
        return status == Status.ERROR;
    }
}
