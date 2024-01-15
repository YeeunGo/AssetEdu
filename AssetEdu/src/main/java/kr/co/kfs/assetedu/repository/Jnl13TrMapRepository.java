package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl13TrMap;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Jnl13TrMapRepository {
	public List<Jnl13TrMap> selectList(QueryAttr queryAttr);
	public Jnl13TrMap selectOne (Jnl13TrMap jnl13TrMap);
	public int insert(Jnl13TrMap jnl13TrMap);
	public int update(Jnl13TrMap jnl13TrMap);
	public int delete(Jnl13TrMap jnl13TrMap);
	
	public List<Jnl13TrMap> selectByTrCd(String trCd);
	
}
