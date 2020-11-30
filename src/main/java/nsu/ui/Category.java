package nsu.ui;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Calendar;

public class Category {
    private Long id;

    @NotEmpty(message = "Summary is required.")
    private String summary;

    @NotEmpty(message = "Message is required.")
    private String text;

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
}
