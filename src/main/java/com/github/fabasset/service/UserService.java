package com.github.fabasset.service;

import com.github.fabasset.model.User;
import com.github.fabasset.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
	 * save user
	 */
    public void addUser(User user) {
    	if (!userRepository.existsById(user.getId())) {
    		userRepository.save(user);
    	}
    }
    
//    /**
//	 * 사용자 조회
//	 */
//    public User getUser(String id) {
//    	return userRepository.findById(id).orElse(null);
//    }
//
//    /**
//	 * 사용자 조회
//	 */
//    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
//    public List<User> getAllUsers() {
//    	return userRepository.findAll(new Sort(Sort.Direction.DESC, "createDate"));
//    }
}