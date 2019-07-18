package com.tecneu.tecneu.models;

import java.util.Date;

public class Order {
    public int idOrder;
    public int idPersonReceiving;
    public int idPaymentMethod;
    public String trackingNumber;
    public Date creationDate;
    public Date modificationDate;
    public String orderStatus;
}
