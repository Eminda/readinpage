package ethirium.eth.service;

import ethirium.eth.dto.JobStatusDto;
import ethirium.eth.model.JobStatus;
import ethirium.eth.repository.JobStatusRepo;
import ethirium.eth.utils.DomainConstatns.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapeService {
    @Autowired
    private JobStatusRepo jobStatusRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCurrentStatus(String domainCompleted,Integer jobStatusID){
        JobStatus jobStatus= jobStatusRepo.findByIdValue(jobStatusID);

        jobStatus.setCurrentDomain(domainCompleted);
        jobStatus.incrementCompletedDomainCount();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markJobFailed(Integer jobStatusID){
        JobStatus jobStatus=jobStatusRepo.findByIdValue(jobStatusID);

        jobStatus.setStatus(Status.FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markJobCompleted(Integer jobStatusID){
        JobStatus jobStatus=jobStatusRepo.findByIdValue(jobStatusID);

        jobStatus.setStatus(Status.COMPLETE);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<JobStatusDto> getLastJobsStatus(){
        return jobStatusRepo.findTop100ByOrderByIdValueDesc().stream().map(JobStatus::getJobStatusDto).collect(Collectors.toList());
    }
}
