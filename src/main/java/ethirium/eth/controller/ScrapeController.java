package ethirium.eth.controller;

import ethirium.eth.dto.CompanyDto;
import ethirium.eth.dto.JobDto;
import ethirium.eth.dto.JobStatusDto;
import ethirium.eth.dto.UserDto;
import ethirium.eth.service.CrawlerService;
import ethirium.eth.service.ScrapeService;
import ethirium.eth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(value = "/scrape")
public class ScrapeController {

    @Autowired
    private ScrapeService scrapeService;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Integer submitJob(@RequestBody JobDto jobDto) {
        Integer jobID = crawlerService.saveAndStartJob(jobDto);
        return jobID;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public Boolean stopJob() {
        crawlerService.stopJob();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    public List<JobStatusDto> retrieve() {
        return scrapeService.getLastJobsStatus();
    }

    @RequestMapping(value = "/retrieve-company-list/{id}", method = RequestMethod.GET)
    public List<CompanyDto> retrieveCompanyList(@PathVariable("id") String jobStatusID) {
        return scrapeService.getCompanyList(Integer.parseInt(jobStatusID));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public boolean login(@RequestBody UserDto userDto) {
        try {
            return userService.isUserCorrect(userDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "update-user", method = RequestMethod.POST)
    public boolean updateUser(@RequestBody UserDto userDto) {
        try {
            return userService.updateUser(userDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(path = "/retrieve-xls/{name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("name") String fileName) throws IOException {
        int jobStatusID = Integer.parseInt(fileName.split("_")[0]);
        scrapeService.createExcelDataSheet(jobStatusID, fileName);
        // ...
        File file = new File("./content/" + jobStatusID+".csv");
        Path path = Paths.get(file.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));


        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        ResponseEntity res = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

        return res;
    }
}
