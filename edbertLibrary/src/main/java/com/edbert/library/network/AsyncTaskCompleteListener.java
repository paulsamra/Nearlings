package com.edbert.library.network;

public interface AsyncTaskCompleteListener<T>
{
  
    public void onTaskComplete(T result);
}