package nsu.ui;

import java.math.BigDecimal;
import java.util.Calendar;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class Transaction {
    private Long id;

    @NotEmpty(message = "Summary is required.")
    private String summary;

    @NotEmpty(message = "Text is required.")
    private String text;

    @NotEmpty(message = "Date is required.")
    private String date;

    @NotNull
    private int category_id;

    @NotEmpty(message = "Name is required.")
    private String trans_name;

    @NotNull
    private int amount;

    private Calendar created = Calendar.getInstance();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }



    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
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
