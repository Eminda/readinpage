package ethirium.eth.service;

import ethirium.eth.dto.JobDto;
import ethirium.eth.model.JobStatus;
import ethirium.eth.repository.JobStatusRepo;
import ethirium.eth.scraper.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlerService {
    @Autowired
    private JobStatusRepo jobStatusRepo;

    @Autowired
    private Crawler crawler;

    @Autowired
    private ScrapeService scrapeService;

    @Value("${xls.path}")
    private String xlsPath;

    private static int count=0;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer saveAndStartJob(JobDto jobDto) {
        String urls[] = jobDto.getUrlList().split(",");
        List<String> urlList = new ArrayList<>();
        StringBuffer formattedUrlList = new StringBuffer("");
        for (String url : urls) {
            String urlFormatted = url.trim();
            if (urlFormatted.length() > 0 && url.contains(".")) {
                urlList.add(urlFormatted);
                formattedUrlList.append(urlFormatted).append(",");
            }
        }
        if (formattedUrlList.length() > 0) {
            jobDto.setUrlList(formattedUrlList.toString().substring(0, formattedUrlList.length() - 1));
        }

        String filters[] = jobDto.getFilterList().split(",");
        List<String> filterList = new ArrayList<>();
        StringBuffer filterListFormatted = new StringBuffer("");
        for (String filter : filters) {
            String filterFormatted = filter.trim().toLowerCase();
            if (filterFormatted.length() > 0) {
                filterList.add(filterFormatted);
                filterListFormatted.append(filterFormatted).append(",");
            }
        }
        if (filterListFormatted.length() > 0) {
            jobDto.setFilterList(filterListFormatted.toString().substring(0, filterListFormatted.length() - 1));
        }
        //Remove in production
//        if (urlList.size() > 2 || filterList.size() > 2 || ++count>10) {
//            throw new RuntimeException("Too many variations.");
//        }
        JobStatus jobStatus = new JobStatus(jobDto, urlList.size());

        jobStatusRepo.save(jobStatus);


        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    crawler.scrape(jobDto, urlList, filterList, jobStatus.getJobStatusID());
                } catch (Exception e) {
                    e.printStackTrace();
                    scrapeService.markJobFailed(jobStatus.getJobStatusID());
                }
            }
        };

        jobStatus.setXlsLink(xlsPath + "/" + jobStatus.getJobStatusID() + "_scrape.xlsx");

        new Thread(r).start();

        return jobStatus.getJobStatusID();
    }
}
