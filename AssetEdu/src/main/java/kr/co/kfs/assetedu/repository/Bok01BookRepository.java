package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Bok01BookRepository {
	List<Bok01Book> 	selectList(QueryAttr queryAttr);
	String				getBookId(QueryAttr queryAttr);
	int 				insert(Bok01Book bok01Book);
	String 				getLastHoldDate();
	int 				insertbyDayBefor(QueryAttr queryAttr);
	Bok01Book 			selectByBookId(QueryAttr queryAttr);
	int upsert(Bok01Book bok01Book);
	int deleteByBookId(QueryAttr queryAttr);
	Long selectCount(QueryAttr queryAttr);
	
	List<Bok01Book> selectEvalList(QueryAttr queryAttr);
	Bok01Book selectOneByBookId(QueryAttr queryAttr);
}
