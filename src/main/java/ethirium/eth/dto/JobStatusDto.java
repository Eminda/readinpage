package ethirium.eth.dto;

import ethirium.eth.utils.DomainConstatns.Status;

import java.io.Serializable;

public class JobStatusDto implements Serializable{
    private static final long serialVersionUID = -2124581806695246638L;

    private Integer id;
    private String name;
    private String description;
    private Status status;
    private String xlsLink;
    private Integer totalDomains;
    private Integer completedDomains;
    private String domainList;
    private String filterList;

    public Integer getTotalDomains() {
        return totalDomains;
    }

    public void setTotalDomains(Integer totalDomains) {
        this.totalDomains = totalDomains;
    }

    public Integer getCompletedDomains() {
        return completedDomains;
    }

    public void setCompletedDomains(Integer completedDomains) {
        this.completedDomains = completedDomains;
    }

    public String getDomainList() {
        return domainList;
    }

    public void setDomainList(String domainList) {
        this.domainList = domainList;
    }

    public String getFilterList() {
        return filterList;
    }

    public void setFilterList(String filterList) {
        this.filterList = filterList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getXlsLink() {
        return xlsLink;
    }

    public void setXlsLink(String xlsLink) {
        this.xlsLink = xlsLink;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JobStatusDto{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", status=").append(status);
        sb.append(", xlsLink='").append(xlsLink).append('\'');
        sb.append(", totalDomains=").append(totalDomains);
        sb.append(", completedDomains=").append(completedDomains);
        sb.append(", domainList='").append(domainList).append('\'');
        sb.append(", filterList='").append(filterList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
