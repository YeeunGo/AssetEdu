package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Com01CorpRepository;

@Service
public class Com01CorpService {
	@Autowired
	private  Com01CorpRepository corpRespository;
	
	public List<Com01Corp> selectList(QueryAttr queryAttr){
		return corpRespository.selectList(queryAttr);
	}
	
	public Com01Corp selecOne(Com01Corp com01Corp ) {
		return corpRespository.selectOne(com01Corp);
	}
	
	public int insert(Com01Corp com01Corp) {
		return corpRespository.insert(com01Corp);
	}
	
	public Com01Corp selectOne (Com01Corp com01Corp) {
		return corpRespository.selectOne(com01Corp);
	}
	
	public int update (Com01Corp com01Corp) {
		return corpRespository.update(com01Corp);
	}
	
	public int delete (String com01CorpCd) {
		return corpRespository.delete(com01CorpCd);
	}
	
	public Long selectCount(QueryAttr queryAttr) {
		return corpRespository.selectCount(queryAttr);
	}
	
	public List<Com01Corp> selectType() {
		return corpRespository.selectType();
	}
}
