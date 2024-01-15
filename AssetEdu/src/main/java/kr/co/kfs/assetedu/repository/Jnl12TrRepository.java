package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl12Tr;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Jnl12TrRepository {
	
	public List<Jnl12Tr> selectList(QueryAttr qureyAttr);
	public Jnl12Tr selectOne(Jnl12Tr jnl12Tr);
	public int insert(Jnl12Tr jnl12Tr);
	public int update(Jnl12Tr jnl12Tr);
	public int delete(String jnl12TrCd);
}
