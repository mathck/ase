package at.tuwien.ase.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public class DslTemplate {

    private Integer id;

    @NotNull
    @Size(min = 2)
    private String templateCategoryName;

    @NotNull
    @Size(min = 5)
    private String templateCategoryDescription;

    @NotNull
    @Size(min = 5)
    private String title;

    @NotNull
    @Size(min = 5)
    private String description;

    private Date creationDate;

    @NotNull
    @Size(min = 5)
    private String syntax;

    @NotNull
    @Size(min = 2)
    private String user_mail;

    public DslTemplate(String title, String desc, String syntax, String user_mail) {
        this.title = this.templateCategoryName = title;
        this.description = this.templateCategoryDescription = desc;

        this.syntax = syntax;
        this.user_mail = user_mail;
    }

    // Must have no-argument constructor
    public DslTemplate() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateCategoryName() {
        return templateCategoryName;
    }

    public void setTemplateCategoryName(String templateCategoryName) {
        this.templateCategoryName = templateCategoryName;
    }

    public String getTemplateCategoryDescription() {
        return templateCategoryDescription;
    }

    public void setTemplateCategoryDescription(String templateCategoryDescription) {
        this.templateCategoryDescription = templateCategoryDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    @Override
    public String toString() {
        return "DslTemplate{" +
                "id=" + id +
                ", templateCategoryName='" + templateCategoryName + '\'' +
                ", templateCategoryDescription='" + templateCategoryDescription + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", syntax='" + syntax + '\'' +
                ", user_mail='" + user_mail + '\'' +
                '}';
    }
}
