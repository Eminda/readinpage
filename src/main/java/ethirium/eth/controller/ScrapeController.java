package ethirium.eth.controller;

import ethirium.eth.dto.CompanyDto;
import ethirium.eth.dto.JobDto;
import ethirium.eth.dto.JobStatusDto;
import ethirium.eth.service.CrawlerService;
import ethirium.eth.service.ScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/scrape")
public class ScrapeController {

    @Autowired
    private ScrapeService scrapeService;

    @Autowired
    private CrawlerService crawlerService;

    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public Boolean createAdvancePayment(@RequestBody JobDto jobDto){
        Integer jobID=crawlerService.saveAndStartJob(jobDto);
        return jobID != null;
    }

    @RequestMapping(value = "/retrieve",method = RequestMethod.GET)
    public List<JobStatusDto> retrieve(){
        return scrapeService.getLastJobsStatus();
    }

    @RequestMapping(value = "/retrieve-company-list/{id}",method = RequestMethod.GET)
    public List<CompanyDto> retrieveCompanyList(@PathVariable("id") String jobStatusID){
        return scrapeService.getCompanyList(Integer.parseInt(jobStatusID));
    }

    @RequestMapping(path = "/retrieve-xls/{name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("name") String fileName) throws IOException {

        // ...
        File file=new File("./content/"+fileName);
        Path path = Paths.get(file.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));


        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename="+fileName );

        ResponseEntity res= ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

        return res;
    }
}
