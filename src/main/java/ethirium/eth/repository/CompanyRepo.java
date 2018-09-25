package ethirium.eth.repository;

import ethirium.eth.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepo extends CrudRepository<Company, Integer> {

    Company findByCompanyID(Integer companyID);

    List<Company> findAllByJobStatusIDAndShow(Integer jobStatusID,boolean show);

    List<Company> findAllByUrlSearchedAndJobStatusID(String url,Integer jobStatusID);

}
