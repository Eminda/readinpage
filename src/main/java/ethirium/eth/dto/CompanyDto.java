package ethirium.eth.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {
    private String urlSearched;
    private String companyName;
    private List<ContactDto> contactDtoList =new ArrayList<>();

    public void addContact(ContactDto contactDto){
        this.contactDtoList.add(contactDto);
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

    public List<ContactDto> getContactDtoList() {
        return contactDtoList;
    }

    public void setContactDtoList(List<ContactDto> contactDtoList) {
        this.contactDtoList = contactDtoList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CompanyDto{");
        sb.append("urlSearched='").append(urlSearched).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", contactDtoList=").append(contactDtoList);
        sb.append('}');
        return sb.toString();
    }
}
