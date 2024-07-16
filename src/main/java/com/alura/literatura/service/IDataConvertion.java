package com.alura.literatura.service;

public interface IDataConvertion {
    <T> T getData(String json, Class<T> clase);
}