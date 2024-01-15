package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Jnl11ReprAcntRepository {
	
	public List<Jnl11ReprAcnt> selectList(QueryAttr queryAttr);
	public Long selectCount(QueryAttr queryAttr);
	public Jnl11ReprAcnt selectOne(Jnl11ReprAcnt jnl11ReprAcnt);
	public int insert (Jnl11ReprAcnt jnl11ReprAcnt);
	public int update(Jnl11ReprAcnt jnl11ReprAcnt);
	public int delete(Jnl11ReprAcnt jnl11ReprAcnt);
}
