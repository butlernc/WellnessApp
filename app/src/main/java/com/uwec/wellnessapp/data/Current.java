package com.uwec.wellnessapp.data;

import java.util.Date;

/**
 * Created by butlernc on 12/9/2014.
 */
public class Current {

    private Date current_date;
    private int program_week_num;

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    public int getProgram_week_num() {
        return program_week_num;
    }

    public void setProgram_week_num(int program_week_num) {
        this.program_week_num = program_week_num;
    }
}
