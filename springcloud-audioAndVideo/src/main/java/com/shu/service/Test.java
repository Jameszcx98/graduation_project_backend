package com.shu.service;


public interface Test {
    public void m4aToMp3(String original, String destination);
    public void m4aToWav(String original, String destination);
    public void m4aCompress(String original, String destination, Double compress_rate);
    public void movTomp4(String original, String destination);
    public void audioCompress(String original, String destination);



}
