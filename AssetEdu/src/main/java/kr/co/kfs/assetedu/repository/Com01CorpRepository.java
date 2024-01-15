package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Com01CorpRepository {
	
	List <Com01Corp> selectList(QueryAttr queryAttr);
	Com01Corp selectOne(Com01Corp com01Corp);
	int insert(Com01Corp com01Corp);
	int update(Com01Corp com01Corp);
	int delete(String com01CorpCd);
	Long selectCount(QueryAttr queryAttr);
	List<Com01Corp> selectType();
}
