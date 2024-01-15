package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kfs.assetedu.model.Jnl12Tr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Jnl12TrRepository;

@Service
public class Jnl12TrService {
	
	@Autowired
	Jnl12TrRepository jnl12Repository;
	
	public List<Jnl12Tr> selectList (QueryAttr queryAttr){
		return jnl12Repository.selectList(queryAttr);
	}
	
	public Jnl12Tr selectOne (Jnl12Tr jnl12Tr) {
		return jnl12Repository.selectOne(jnl12Tr);
	}
	
	public int insert (Jnl12Tr jnl12Tr) {
		return jnl12Repository.insert(jnl12Tr);
	}
	
	public int update(Jnl12Tr jnl12Tr) {
		return jnl12Repository.update(jnl12Tr);
	}
	
	public int delete(String jnl12TrCd) {
		return jnl12Repository.delete(jnl12TrCd);
	}
}
