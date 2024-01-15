package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Itm01ItemRepository {
	List<Itm01Item> selectList(QueryAttr queryAttr);
	Itm01Item selectOne(Itm01Item itm01Item);
	int insert(Itm01Item itm01Item);
	int update(Itm01Item itm01Item);
	int delete(Itm01Item itm01Item);
	Long selectCount(QueryAttr queryAttr);
}
