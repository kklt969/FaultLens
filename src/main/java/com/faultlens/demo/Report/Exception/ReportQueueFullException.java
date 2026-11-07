package com.faultlens.demo.Report.Exception;

public class ReportQueueFullException  extends RuntimeException{
    public ReportQueueFullException(String message){
        super(message);
    }
}
