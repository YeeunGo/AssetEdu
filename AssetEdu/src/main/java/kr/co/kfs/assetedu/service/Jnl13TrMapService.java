package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Jnl13TrMap;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Jnl13TrMapRepository;

@Service
public class Jnl13TrMapService {
	
	@Autowired
	Jnl13TrMapRepository jnl13Repository;
	
	public List<Jnl13TrMap> selectList (QueryAttr queryAttr) {
		return jnl13Repository.selectList(queryAttr);
	}
	
	public Jnl13TrMap selectOne (Jnl13TrMap jnl13TrMap) {
		return jnl13Repository.selectOne(jnl13TrMap);
	}
	
	@Transactional
	public int insert(Jnl13TrMap jnl13TrMap) {
		return jnl13Repository.insert(jnl13TrMap);
	}
	
	@Transactional
	public int update(Jnl13TrMap jnl13TrMap) {
		return jnl13Repository.update(jnl13TrMap);
	}
	
	public int delete(Jnl13TrMap jnl13TrMap) {
		return jnl13Repository.delete(jnl13TrMap);
	}
	
}
