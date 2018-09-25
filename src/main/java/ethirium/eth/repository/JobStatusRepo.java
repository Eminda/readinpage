package ethirium.eth.repository;

import ethirium.eth.model.JobStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatusRepo extends CrudRepository<JobStatus, Integer> {

    JobStatus findByJobStatusID(Integer id);

    List<JobStatus> findTop100ByOrderByJobStatusIDDesc();
}
