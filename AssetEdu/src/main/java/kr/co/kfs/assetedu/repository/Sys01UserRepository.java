package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.model.Sys01User;

public interface Sys01UserRepository {
	// list and totalcount
	List<Sys01User> selectList(QueryAttr queryAttr);
	Long selectCount(QueryAttr queryAttr);
	
	String getDictId();
	
	// CRUD
	Sys01User selectOne(String sys01UserId);
	int insert(Sys01User user);
	int update(Sys01User user);
	int delete(Sys01User user);

}
