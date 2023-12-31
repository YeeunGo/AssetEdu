package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Sys01User;
import kr.co.kfs.assetedu.model.Sys02Dict;

public interface Sys01UserRepository {
	// list and totalcount
	List<Sys01User> selectList(Condition condition);
	Integer selectCount(Condition condition);
	
	String getDictId();
	
	// CRUD
	Sys02Dict selectOne(Sys01User user);
	int insert(Sys01User user);
	int update(Sys01User user);
	int delete(Sys01User user);
}
