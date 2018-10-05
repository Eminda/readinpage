package ethirium.eth.service;

import ethirium.eth.dto.CompanyDto;
import ethirium.eth.dto.ContactDto;
import ethirium.eth.dto.JobStatusDto;
import ethirium.eth.model.Company;
import ethirium.eth.model.Contact;
import ethirium.eth.model.JobStatus;
import ethirium.eth.repository.CompanyRepo;
import ethirium.eth.repository.ContactRepo;
import ethirium.eth.repository.JobStatusRepo;
import ethirium.eth.utils.DomainConstatns.Status;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapeService {
    @Autowired
    private JobStatusRepo jobStatusRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private ContactRepo contactRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCurrentStatus(String domainCompleted, Integer jobStatusID) {
        JobStatus jobStatus = jobStatusRepo.findByJobStatusID(jobStatusID);

        jobStatus.setCurrentDomain(domainCompleted);
        jobStatus.incrementCompletedDomainCount();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markJobFailed(Integer jobStatusID) {
        JobStatus jobStatus = jobStatusRepo.findByJobStatusID(jobStatusID);

        jobStatus.setStatus(Status.FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markJobCompleted(Integer jobStatusID) {
        JobStatus jobStatus = jobStatusRepo.findByJobStatusID(jobStatusID);
        jobStatus.setCompletedDomainCount(jobStatus.getTotalDomainCount());
        jobStatus.setStatus(Status.COMPLETE);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markJobStopped(Integer jobStatusID) {
        JobStatus jobStatus = jobStatusRepo.findByJobStatusID(jobStatusID);

        jobStatus.setStatus(Status.STOPPED);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<JobStatusDto> getLastJobsStatus() {
        return jobStatusRepo.findTop100ByOrderByJobStatusIDDesc().stream().map(JobStatus::getJobStatusDto).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CompanyDto> getCompanyList(Integer jobStatusID) {
        return companyRepo.findTop1000ByJobStatusIDAndShowOrderByCompanyIDDesc(jobStatusID,true).stream().map(Company::getCompanyDto).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer createCompany(CompanyDto companyDto, Integer jobStatusID) {
        Company company=new Company(companyDto,jobStatusID);
        companyRepo.save(company);
        return company.getCompanyID();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer createContact(ContactDto contactDto,Integer companyID){
        Contact contact=new Contact(contactDto,companyID);
        contactRepo.save(contact);
        return contact.getContactID();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteUrlData(String url,Integer jobStatusID){
        List<Company> companyList=companyRepo.findAllByUrlSearchedAndJobStatusID(url,jobStatusID);
        for(Company company:companyList){
            company.setShow(false);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public synchronized void createExcelDataSheet(Integer jobStatusID,String name) throws IOException {
        List<Company> data = companyRepo.findTop1000ByJobStatusIDAndShowAndCompanyIDGreaterThanOrderByCompanyID(jobStatusID, true,0);

        new File("./content").mkdir();
        File file=new File("./content/"+jobStatusID+".csv");
        file.delete();
        file.createNewFile();
        try {
            Files.write(Paths.get("./content/"+jobStatusID+".csv"), new StringBuffer("NAME,EMAIL,ROLE\n").toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowNum=1;
        while (true) {
            for (Company company : data) {
                for (Contact contact : company.getContacts()) {
                    try {
                        Files.write(Paths.get("./content/"+jobStatusID+".csv"), contact.getCSV().getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(data.size()>=1000){
                data=companyRepo.findTop1000ByJobStatusIDAndShowAndCompanyIDGreaterThanOrderByCompanyID(jobStatusID,true,data.get(data.size()-1).getCompanyID());
            }else{
                break;
            }
        }

    }
}
