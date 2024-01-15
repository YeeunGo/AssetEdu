package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Jnl10AcntRepository;

@Service
public class Jnl10AcntService {
	
	@Autowired
	Jnl10AcntRepository acntRepository;
	
	public List<Jnl10Acnt> selectList(QueryAttr queryAttr){
		return acntRepository.selectList(queryAttr);
	}
	
	
	public Long selectCount(QueryAttr queryAttr) {
		return acntRepository.selectCount(queryAttr);
	}
	
	public Jnl10Acnt selectOne(Jnl10Acnt jnl10Acnt) {
		return acntRepository.selectOne(jnl10Acnt);
	}
	
	public int insert (Jnl10Acnt jnl10Acnt) {
		return acntRepository.insert(jnl10Acnt);
	}
	
	public int update(Jnl10Acnt jnl10Acnt) {
		return acntRepository.update(jnl10Acnt);
	}
	
	public int delete (Jnl10Acnt jnl10Acnt) {
		return acntRepository.delete(jnl10Acnt);
	}
	
	

}
