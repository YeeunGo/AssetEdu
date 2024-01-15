package kr.co.kfs.assetedu.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Jnl01Journal;
import kr.co.kfs.assetedu.model.Jnl02JournalTmp;
import kr.co.kfs.assetedu.model.Jnl13TrMap;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Itm01ItemRepository;
import kr.co.kfs.assetedu.repository.Jnl01JournalRepository;
import kr.co.kfs.assetedu.repository.Jnl02JournalTmpRepository;
import kr.co.kfs.assetedu.repository.Jnl13TrMapRepository;
import kr.co.kfs.assetedu.repository.Jnl14RealAcntMapRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Jnl01JournalService {
	//분개장
	@Autowired
	Jnl01JournalRepository jnlRepository;
	//실계정
	@Autowired
 private Jnl02JournalTmpRepository jnlTmpRepository;
	
	@Autowired
	private Jnl13TrMapRepository trMapRepository;
	
	
	@Autowired
	private Itm01ItemRepository itemRepository;
	
	@Autowired
	private Jnl14RealAcntMapRepository realAcntMapRepository;
	
	public List <Jnl01Journal> selectList(QueryAttr queryAttr){
		return jnlRepository.selectList(queryAttr);
	}
	
	@Transactional
	public String createJournal(Opr01Cont cont) throws Exception{
		
		String resultMsg = "Y";
		int procCnt;
		Integer lastSeq = 0;
		
		// 임시분개장, 실분개 장 정리
		procCnt = jnlTmpRepository.deleteByContid(cont.getOpr01ContId());
		procCnt = jnlRepository.deleteByContId(cont.getOpr01ContId());

		//________________________
		//임시분개장
		//_______________________
		Jnl02JournalTmp jnlTmpModel;
		
		//거래코드별 분개 MAP GET
		List<Jnl13TrMap> trMapList = trMapRepository.selectByTrCd(cont.getOpr01TrCd());

		
		
		for(Jnl13TrMap trMapModel : trMapList) {
			//임시분개장에 insert할 객체
			jnlTmpModel = new Jnl02JournalTmp();
			jnlTmpModel.setJnl02ContId(cont.getOpr01ContId());
			jnlTmpModel.setJnl02Seq(trMapModel.getJnl13Seq());
			jnlTmpModel.setJnl02DrcrType(trMapModel.getJnl13DrcrType());
			jnlTmpModel.setJnl02ReprAcntCd(trMapModel.getJnl13ReprAcntCd());
			//대표계정코드
			// 대표계정코드가 미수(1200)/미지급금(2200)인 경우 '체결일과  = 결제일 '이면, 예금(8888)으로 변경
			if("1200".equals(jnlTmpModel.getJnl02ReprAcntCd()) || "2200".equals(jnlTmpModel.getJnl02ReprAcntCd())) {
				if(cont.getOpr01ContDate().equals(cont.getOpr01SettleDate())) {
					jnlTmpModel.setJnl02ReprAcntCd("8888");
				}
			}
			
			//금액
			QueryAttr queryAttr = new QueryAttr();
			queryAttr.put("formula", trMapModel.getJnl13Formula());
			queryAttr.put("contId",  cont.getOpr01ContId());
			
			Long amt = jnlRepository.getAmt(queryAttr);
			
			log.debug("----amt : {}"+ amt);
			
			jnlTmpModel.setJnl02Amt(amt);
			
			if(amt != 0 ) {	
				procCnt = jnlTmpRepository.insert(jnlTmpModel);
				lastSeq = jnlTmpModel.getJnl02Seq();
			}
		}

		//차대차이 금액 확인 (매수나 평가인 경우 error 처리. 매도인 경우엔 처분손익 생성
		Long diffAmt = jnlTmpRepository.selectDiffAmt(cont.getOpr01ContId());
		if(diffAmt != 0 && diffAmt != null) {
	
			// 차이 금액이 있는데 매도가 아닐때
			if(!"2001".equals(cont.getOpr01TrCd()) && !"2002".equals(cont.getOpr01TrCd())) {
			
				resultMsg = "차/대 금액이 다릅니다.";
				throw new AssetException(resultMsg);
			}
			
			// 처분손익 생성
			jnlTmpModel = new Jnl02JournalTmp();
			jnlTmpModel.setJnl02ContId(cont.getOpr01ContId());
			jnlTmpModel.setJnl02Seq(lastSeq + 1);
			
			//차이금액 > 0 = 처분 이익 발생
			if(diffAmt >0) {
			jnlTmpModel.setJnl02ReprAcntCd("4100");//처분이익
			jnlTmpModel.setJnl02DrcrType("C"); // (C:대변)
			jnlTmpModel.setJnl02Amt(diffAmt);
			}
			//차이 금액 <0 = 처분 손실 
			else {
				jnlTmpModel.setJnl02ReprAcntCd("5100");//처분손실
				jnlTmpModel.setJnl02DrcrType("D"); // (D:차변)
				jnlTmpModel.setJnl02Amt(diffAmt*-1);
			}
			
			jnlTmpRepository.insert(jnlTmpModel);
		}
		
	
		// 실 분개장
		
		// 종목정보 GET
		Itm01Item item = new Itm01Item();
		item.setItm01ItemCd(cont.getOpr01ItemCd());
		Itm01Item itemInfo = itemRepository.selectOne(item);
		
		//상장구분 입력체크
		if(itemInfo.getItm01ListType() == null || "".equals(itemInfo.getItm01ListType())) {
			resultMsg = "상장구분이 없습니다. 종목정보를 확인하세요";
			throw new AssetException(resultMsg);
		}
		//시장구분 입력체크
		if(itemInfo.getItm01MarketType() == null ||  "".equals(itemInfo.getItm01MarketType())) {
			resultMsg = "시장구분이 없습니다. 종목정보를 확인하세요";
			throw new AssetException(resultMsg);
		}
		
		QueryAttr realAcntCondition = new QueryAttr();
		realAcntCondition.put("listType", itemInfo.getItm01ListType());
		realAcntCondition.put("marketType", itemInfo.getItm01MarketType());
		
		// 임시분개장 읽고
		
		Long drSeq = 0l;
		Long crSeq = 0l;
		String dmlType = "I";
		Jnl01Journal jnlModel  ;
		
	
		List<Jnl02JournalTmp> jnlTmpList = jnlTmpRepository.selectListByContId(cont.getOpr01ContId());
		for(Jnl02JournalTmp jnlTmp : jnlTmpList) {
			jnlModel = new Jnl01Journal();
			jnlModel.setJnl01ContId(cont.getOpr01ContId());
			// 대표계정 코드 = > 실계정 코드
			realAcntCondition.put("reprAcntCd", jnlTmp.getJnl02ReprAcntCd());
			String acntCd = realAcntMapRepository.selectByReprAcntCd(realAcntCondition);

			if(acntCd == null || "".equals(acntCd)) {
				resultMsg = "실계정과목을 가져올 수 없습니다. 관리팀에 문의하세요";
				throw new AssetException(resultMsg);
			}
			
			// 차변 / 대변 자리 맞춰서 실분개장 생성하기 (
			if("D".equals(jnlTmp.getJnl02DrcrType())) { //차변먼저
				drSeq++;
				dmlType="I";  //insert인지 update인지
				jnlModel.setJnl01Seq(drSeq);
				jnlModel.setJnl01DrAcntCd(acntCd);
				jnlModel.setJnl01DrAmt(jnlTmp.getJnl02Amt());
			}
			else {
				crSeq ++;
				jnlModel.setJnl01Seq(crSeq);

				if(drSeq >= crSeq) {
					dmlType ="U";
					jnlModel = jnlRepository.selectOne(jnlModel);
	
				} else {
					dmlType ="I";
				}
				jnlModel.setJnl01CrAcntCd(acntCd);
				jnlModel.setJnl01CrAmt(jnlTmp.getJnl02Amt());
			}
			
			if("I".equals(dmlType)) {
				procCnt = jnlRepository.insert(jnlModel);
			} else {
				procCnt = jnlRepository.update(jnlModel);
			}
			
		}
		
		return resultMsg; 
	
		
	}

}
