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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
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

        jobStatus.setStatus(Status.COMPLETE);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<JobStatusDto> getLastJobsStatus() {
        return jobStatusRepo.findTop100ByOrderByJobStatusIDDesc().stream().map(JobStatus::getJobStatusDto).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CompanyDto> getCompanyList(Integer jobStatusID) {
        return companyRepo.findAllByJobStatusIDAndShow(jobStatusID,true).stream().map(Company::getCompanyDto).collect(Collectors.toList());
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
    public synchronized void createExcelDataSheet(Integer jobStatusID,String name){
        List<Company> data = companyRepo.findAllByJobStatusIDAndShow(jobStatusID, true);
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Contacts");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        String columns[] = {"URL", "Company Name", "Name", "Email", "Role"};
        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum=1;
        for(Company company:data){
            for(Contact contact:company.getContacts()){
                Row row=sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(company.getUrlSearched());
                row.createCell(1).setCellValue(company.getName());
                row.createCell(2).setCellValue(contact.getName());
                row.createCell(3).setCellValue(contact.getEmail());
                row.createCell(4).setCellValue(contact.getRole());
            }
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fileOut=null;
        try {
            fileOut = new FileOutputStream("./content/"+name);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        }catch (Exception e){
            try{
                workbook.close();
                fileOut.close();
            }catch (Exception ed){}
        }
    }
}
