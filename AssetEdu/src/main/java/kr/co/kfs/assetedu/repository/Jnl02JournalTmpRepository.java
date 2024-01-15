package kr.co.kfs.assetedu.repository;

import java.util.List;

import kr.co.kfs.assetedu.model.Jnl01Journal;
import kr.co.kfs.assetedu.model.Jnl02JournalTmp;
import kr.co.kfs.assetedu.model.QueryAttr;

/**
 * 분개장 Repository 
 *
 */
public interface Jnl02JournalTmpRepository {

List<Jnl02JournalTmp> selectListByContId(String contId);

int deleteByContid(String contId);
int insert (Jnl02JournalTmp jnl02JournalTmp);

Long selectDiffAmt(String contId);
}
