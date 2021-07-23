package com.stonefacesoft.ottaa.Interfaces;

public interface RemoteConfigListener {
    public interface PriceListener{
        public boolean isRegionPriceEnabled();
        public String changePrice(String text,String name);
        public String paymentUtri();
        public String paymentUtriPremium();
    }
}
