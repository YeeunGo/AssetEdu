package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl01Journal;
import kr.co.kfs.assetedu.model.Jnl02JournalTmp;
import kr.co.kfs.assetedu.model.QueryAttr;


public interface Jnl01JournalRepository {

	List<Jnl01Journal> selectList(QueryAttr condition);
	Long selectCount(QueryAttr condition);

	// CRUD
	Jnl01Journal selectOne(Jnl01Journal journal);
	int insert(Jnl01Journal journal);
	int update(Jnl01Journal journal);
	int delete(Jnl01Journal journal);
	int deleteByContId(String contId);
	
	Long getAmt(QueryAttr queryAttr);
	


}
