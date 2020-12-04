package nsu.ui;

import java.math.BigDecimal;
import java.util.Calendar;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class Transaction {

    private Long id;

    @NotEmpty(message = "Дата - обязательное поле.")
    private String date;

    @NotNull(message = "Категория - обязательное поле.")
    private Long category_id;

    @NotEmpty(message = "Название - обязательное поле.")
    private String trans_name;


    @NotNull(message = "Сумма - обязательное поле.")
    private int amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }


    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
