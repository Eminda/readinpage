package ethirium.eth.repository;

import ethirium.eth.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {

    User findByUserID(Integer userID);

}
