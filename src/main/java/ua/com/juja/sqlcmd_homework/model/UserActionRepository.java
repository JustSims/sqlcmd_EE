package ua.com.juja.sqlcmd_homework.model;


import org.springframework.data.repository.CrudRepository;
import ua.com.juja.sqlcmd_homework.model.entity.UserAction;

import java.util.List;

/**
 * Created by Sims on 10/01/2016.
 */
public interface UserActionRepository extends CrudRepository<UserAction, Integer> {
    List<UserAction> findByUserName(String userName);
}