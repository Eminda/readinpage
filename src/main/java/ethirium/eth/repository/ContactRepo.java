package ethirium.eth.repository;

import ethirium.eth.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepo extends CrudRepository<Contact, Integer> {
}
