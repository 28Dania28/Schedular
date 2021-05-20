package com.dania.scheduler;

public interface DateChangeListener {
    void OnSelect(String dateLable, int position);
    void OnLongPressed(TimeModel tm);
}
