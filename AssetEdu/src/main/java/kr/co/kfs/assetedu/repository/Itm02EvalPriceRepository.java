package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Itm02EvalPrice;
import kr.co.kfs.assetedu.model.QueryAttr;

public interface Itm02EvalPriceRepository {
	int upsert(Itm02EvalPrice itm02EvalPrice);
	List<Itm02EvalPrice> selectList(QueryAttr condition);
}
