package nsu.ui;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Calendar;

public class Category {
    private Long id;

    @NotEmpty(message = "Category name is required.")
    private String categoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
