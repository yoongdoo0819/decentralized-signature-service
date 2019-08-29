package com.poscoict.posledger.assets.service;

import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
	 * 사용자 저장
	 */
    public void addUser(User user) {
    	if (!userRepository.existsById(user.getId())) {
    		userRepository.save(user);
    	}
    }
    
    /**
	 * 사용자 조회
	 */
    public User getUser(String id) {
    	return userRepository.findById(id).orElse(null);
    }
    
    /**
	 * 사용자 조회
	 */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<User> getAllUsers() {
    	return userRepository.findAll(new Sort(Sort.Direction.DESC, "createDate"));
    }
}