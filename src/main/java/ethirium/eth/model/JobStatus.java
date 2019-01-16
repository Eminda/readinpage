package ethirium.eth.model;

import ethirium.eth.dto.JobDto;
import ethirium.eth.dto.JobStatusDto;
import ethirium.eth.utils.DomainConstatns.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "T_JOB_STATUS")
public class JobStatus implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_STATUS_ID")
    private Integer jobStatusID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "XLS_LINK")
    private String xlsLink;

    @Column(name = "CURRENT_DOMAIN")
    private String currentDomain;

    @Column(name = "TOTAL_DOMAIN_COUNT")
    private Integer totalDomainCount;

    @Column(name = "COMPLETED_DOMAIN_COUNT")
    private Integer completedDomainCount;

    @Column(name = "DOMAIN_LIST",length = 20000)
    private String domainList;

    @Column(name = "FILTER_LIST",length = 10000)
    private String filterList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_STATUS_ID",referencedColumnName = "JOB_STATUS_ID")
    private List<Company> companies;

    public JobStatus() {
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public JobStatus(JobDto jobDto, Integer domainCount){
        this.name=jobDto.getJobName();
        this.description=jobDto.getDescription();
        this.status=Status.INPROGRESS;
        this.totalDomainCount=domainCount;
//        this.domainList=jobDto.getUrlList();
        this.filterList=jobDto.getFilterList();
        this.completedDomainCount=0;
    }

    public JobStatusDto getJobStatusDto(){
        JobStatusDto jobStatusDto=new JobStatusDto();

        jobStatusDto.setId(this.jobStatusID);
        jobStatusDto.setName(this.name);
        jobStatusDto.setDescription(this.description);
        jobStatusDto.setStatus(this.status);
        jobStatusDto.setXlsLink(this.xlsLink);
//        jobStatusDto.setDomainList(this.domainList);
        jobStatusDto.setFilterList(this.filterList);
        jobStatusDto.setTotalDomains(this.totalDomainCount);
        jobStatusDto.setCompletedDomains(this.completedDomainCount);
        jobStatusDto.setLastCompletedDomain(this.currentDomain);

        return jobStatusDto;
    }

    public Integer getJobStatusID() {
        return jobStatusID;
    }

    public void setJobStatusID(Integer jobStatusID) {
        this.jobStatusID = jobStatusID;
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

    public String getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(String currentDomain) {
        this.currentDomain = currentDomain;
    }

    public Integer getTotalDomainCount() {
        return totalDomainCount;
    }

    public void setTotalDomainCount(Integer totalDomainCount) {
        this.totalDomainCount = totalDomainCount;
    }

    public Integer getCompletedDomainCount() {
        return completedDomainCount;
    }

    public void setCompletedDomainCount(Integer completedDomainCount) {
        this.completedDomainCount = completedDomainCount;
    }

    public void incrementCompletedDomainCount(){
        this.completedDomainCount++;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JobStatus{");
        sb.append("jobStatusID=").append(jobStatusID);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", status=").append(status);
        sb.append(", xlsLink='").append(xlsLink).append('\'');
        sb.append(", currentDomain='").append(currentDomain).append('\'');
        sb.append(", totalDomainCount=").append(totalDomainCount);
        sb.append(", completedDomainCount=").append(completedDomainCount);
        sb.append(", domainList='").append(domainList).append('\'');
        sb.append(", filterList='").append(filterList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
