package ua.datalink.jms.server.dao;

import org.springframework.data.repository.CrudRepository;
import ua.datalink.jms.server.entity.User;

/**
 *
 */
public interface UserDAO extends CrudRepository<User, Integer> {
}
