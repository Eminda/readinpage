package ethirium.eth.dto;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String urlSearched;
    private String companyName;
    private List<Contact> contactList=new ArrayList<>();

    public void addContact(Contact contact){
        this.contactList.add(contact);
    }

    public String getUrlSearched() {
        return urlSearched;
    }

    public void setUrlSearched(String urlSearched) {
        this.urlSearched = urlSearched;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Company{");
        sb.append("urlSearched='").append(urlSearched).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", contactList=").append(contactList);
        sb.append('}');
        return sb.toString();
    }
}
