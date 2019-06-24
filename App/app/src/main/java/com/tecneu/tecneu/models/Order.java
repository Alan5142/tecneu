package com.tecneu.tecneu.models;

import java.util.Date;

public class Order {
    public int idOrder;
    public int idPersonReceiving;
    public int idPaymentMethod;
    public String tracking_number;
    public Date creation_date;
    public Date modification_date;
    public byte[] invoice;
    public String personR;
    public String payment;
}
