package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Sys01User;
import kr.co.kfs.assetedu.repository.Sys01UserRepository;

@Service
public class Sys01UserService  {

	@Autowired
	private Sys01UserRepository userRepository;

	
//	public Sys02Dict selectOne(Sys02Dict user) {
//		return dictRepository.selectOne(user);
//	}
	
	public List<Sys01User> selectList(Condition condition){
		return userRepository.selectList(condition);
	}
	
//	public Integer selectCount(Condition condition){
//		return dictRepository.selectCount(condition);
//	}
//	
//	public String getDictId() {
//		return dictRepository.getDictId();
//	}
//	
	@Transactional
	public int insert(Sys01User user) {
		return userRepository.insert(user);
	}
//	@Transactional
//	public int update(Sys02Dict user) {
//		return dictRepository.update(user);
//	}
//	@Transactional
//	public int delete(Sys02Dict  user ) {
//		return dictRepository.delete(user);
//	}
}
