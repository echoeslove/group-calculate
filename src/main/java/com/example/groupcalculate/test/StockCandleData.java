package com.example.groupcalculate.test;

import java.time.LocalDate;

public class StockCandleData implements Comparable<StockCandleData> {

    public LocalDate date;

    public Double high;

    public Double low;

    public double[] price;

    public double absHighLow;

    @Override
    public int compareTo(StockCandleData o) {
        return date.compareTo(o.date);
    }
}
