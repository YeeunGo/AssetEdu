package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl14RealAcntMap;
import kr.co.kfs.assetedu.model.QueryAttr;
/**
 * 
 * 실계정맵핑
 *
 */
public interface Jnl14RealAcntMapRepository {
	
	String selectByReprAcntCd(QueryAttr queryAttr);
	
	// list and totalcount
		List<Jnl14RealAcntMap> selectList(QueryAttr condition);
		Integer selectCount(QueryAttr condition);
	
	// CRUD
		Jnl14RealAcntMap selectOne(Jnl14RealAcntMap acnt);
		String selectRealAcntCode(QueryAttr condition);
		int insert(Jnl14RealAcntMap acnt);
		int update(Jnl14RealAcntMap acnt);
		int delete(Jnl14RealAcntMap acnt);
	
	
}
