package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Jnl11ReprAcntRepository;

@Service
public class Jnl11ReprAcntService {
	
	@Autowired
	Jnl11ReprAcntRepository reprAcntRepository;

	public List<Jnl11ReprAcnt> selectList(QueryAttr queryAttr){
		return reprAcntRepository.selectList(queryAttr);
	}
	
	public Long selectCount(QueryAttr queryAttr) {
		return reprAcntRepository.selectCount(queryAttr);
	}
	
	public Jnl11ReprAcnt selectOne (Jnl11ReprAcnt jnl11ReprAcnt) {
		return reprAcntRepository.selectOne(jnl11ReprAcnt);
	}
	
	public int insert (Jnl11ReprAcnt jnl11ReprAcnt) {
		return reprAcntRepository.insert(jnl11ReprAcnt);
	}
	
	public int update(Jnl11ReprAcnt jnl11ReprAcnt) {
		return reprAcntRepository.update(jnl11ReprAcnt);
	}
	
	public int delete(Jnl11ReprAcnt jnl11ReprAcnt) {
		return reprAcntRepository.delete(jnl11ReprAcnt);
	}
}
