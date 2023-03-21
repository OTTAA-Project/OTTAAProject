package com.stonefacesoft.ottaa.Interfaces;

public interface RemoteConfigListener {
    interface PriceListener{
        boolean isRegionPriceEnabled();
        String changePrice(String text,String name);
        String paymentUtri();
        String paymentUtriPremium();
    }
    interface AvatarListener{
        boolean enableAvatar();
        String avatarMessages();}
}
