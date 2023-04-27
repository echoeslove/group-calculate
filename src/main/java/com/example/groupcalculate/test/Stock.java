package com.example.groupcalculate.test;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Stock {

    public String code;

    public Map<LocalDate, StockCandleData> map = new TreeMap<>();
}
