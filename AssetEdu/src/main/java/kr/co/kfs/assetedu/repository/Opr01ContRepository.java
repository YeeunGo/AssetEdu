package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Opr01ContRepository {
	List<Opr01Cont> selectList(QueryAttr queryAttr);
	int insert(Opr01Cont cont);
	String getNewSeq();
	int update (Opr01Cont cont);
	Opr01Cont selectOne (String contId);
	String newContId();
	List<Opr01Cont> seleceByBookId (QueryAttr queryAttr);
}
