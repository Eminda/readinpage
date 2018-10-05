package ethirium.eth.dto;

import java.io.Serializable;

public class JobDto implements Serializable{
    private static final long serialVersionUID = -3396682678321217095L;

    private Integer id;
    private String jobName;
    private String description;
    private String email;
    private String password;
    private boolean retrieveEmailOnly;
    private String urlList;
    private String filterList;

    public boolean isRetrieveEmailOnly() {
        return retrieveEmailOnly;
    }

    public void setRetrieveEmailOnly(boolean retrieveEmailOnly) {
        this.retrieveEmailOnly = retrieveEmailOnly;
    }

    public String getUrlList() {
        return urlList;
    }

    public void setUrlList(String urlList) {
        this.urlList = urlList;
    }

    public String getFilterList() {
        return filterList;
    }

    public void setFilterList(String filterList) {
        this.filterList = filterList;
    }

    public JobDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JobDto{");
        sb.append("id=").append(id);
        sb.append(", jobName='").append(jobName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", retrieveEmailOnly=").append(retrieveEmailOnly);
        sb.append(", urlList='").append(urlList).append('\'');
        sb.append(", filterList='").append(filterList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
