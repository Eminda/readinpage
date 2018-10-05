package ethirium.eth.model;

import ethirium.eth.dto.ContactDto;

import javax.persistence.*;

@Entity
@Table(name = "T_CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTACT_ID")
    private Integer contactID;

    @Column(name = "COMPANY_ID")
    private Integer companyID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "EMAIL")
    private String email;

    public Contact() {
    }

    public Contact(ContactDto contactDto,Integer companyID){
        this.companyID=companyID;
        this.name=contactDto.getName();
        this.role=contactDto.getRole();
        this.email=contactDto.getEmail();
    }

    public ContactDto getContactDto(){
        ContactDto contactDto=new ContactDto();
        contactDto.setName(this.name);
        contactDto.setEmail(this.email);
        contactDto.setRole(this.role);
        return contactDto;
    }

    public String getCSV(){
        final StringBuffer sb = new StringBuffer("");
        sb.append("\""+name+"\"").append(',');
        sb.append("\""+email+"\"").append(',');
        sb.append("\""+role+"\"").append('\n');
        return sb.toString();
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Contact{");
        sb.append("contactID=").append(contactID);
        sb.append(", companyID=").append(companyID);
        sb.append(", name='").append(name).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
