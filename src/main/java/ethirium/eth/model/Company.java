package ethirium.eth.model;

import ethirium.eth.dto.CompanyDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "T_COMPANY")
public class Company implements Serializable{
    @Id
    @Column(name = "COMPANY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyID;

    @Column(name = "JOB_STATUS_ID")
    private Integer jobStatusID;

    @Column(name = "URL_SEARCHED")
    private String urlSearched;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHOW_COMPANY",columnDefinition = "TINYINT", length = 1)
    private boolean show;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMPANY_ID",referencedColumnName = "COMPANY_ID")
    private List<Contact> contacts;

    public Company() {
    }

    public Company(CompanyDto companyDto,Integer jobStatusID){
        this.urlSearched=companyDto.getUrlSearched();
        this.name=companyDto.getCompanyName();
        this.jobStatusID=jobStatusID;
        this.show=true;
    }

    public CompanyDto getCompanyDto(){
        CompanyDto companyDto=new CompanyDto();
        companyDto.setUrlSearched(this.urlSearched);
        companyDto.setCompanyName(this.name);
        companyDto.setContactDtoList(this.getContacts().stream().map(Contact::getContactDto).collect(Collectors.toList()));

        return companyDto;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Integer getJobStatusID() {
        return jobStatusID;
    }

    public void setJobStatusID(Integer jobStatusID) {
        this.jobStatusID = jobStatusID;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getUrlSearched() {
        return urlSearched;
    }

    public void setUrlSearched(String urlSearched) {
        this.urlSearched = urlSearched;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Company{");
        sb.append("companyID=").append(companyID);
        sb.append(", jobStatusID=").append(jobStatusID);
        sb.append(", urlSearched='").append(urlSearched).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", show=").append(show);
        sb.append(", contacts=").append(contacts);
        sb.append('}');
        return sb.toString();
    }
}
