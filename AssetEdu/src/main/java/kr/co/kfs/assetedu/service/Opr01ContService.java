package kr.co.kfs.assetedu.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Com03Date;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Bok01BookRepository;
import kr.co.kfs.assetedu.repository.Com03DateRepository;
import kr.co.kfs.assetedu.repository.Opr01ContRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;

@Service
public class Opr01ContService {
	
	@Autowired
	private Opr01ContRepository contRepository;
	
	@Autowired
	private Bok01BookRepository bookRepository;
	
	@Autowired
	private Com03DateRepository dateRepository;
	
	@Autowired
	private Bok01BookService bookService;
	
	public String eval(String procType, String evalDate) throws Exception{
		
		Long checkCnt = 0l;
		
		// 처리할 날의 평가리스트 가져오기
		QueryAttr evalQueryAttr = new QueryAttr();
		evalQueryAttr.put("evalDate", evalDate);
		evalQueryAttr.put("searchText", "%");
		
		List<Bok01Book> evalList = bookRepository.selectEvalList(evalQueryAttr);
		
		String resultmsg = "";
		
		for(Bok01Book book : evalList) {
		
			// 평가 처리 (미평가 건에만 해당)
			if("P".equals(procType) && "false".equals(book.getBok01EvalYn())) {
				
				// 평가내역 생성(opr01 insert)
				Opr01Cont insertModel = new Opr01Cont();
				insertModel.setOpr01ContId(contRepository.getNewSeq());
				insertModel.setOpr01FundCd(book.getBok01FundCd());
				insertModel.setOpr01ItemCd(book.getBok01ItemCd());
				insertModel.setOpr01ContDate(book.getBok01HoldDate());
				insertModel.setOpr01TrCd("3001"); // 평가처리
				insertModel.setOpr01Qty(book.getBok01HoldQty());
				insertModel.setOpr01Price(book.getBok01EvalPrice());
				
				//평가금액 = 보유 수랭 * 평가단가
				Long evalAmt = book.getBok01HoldQty() * book.getBok01EvalPrice();
				insertModel.setOpr01ContAmt(evalAmt);
				
				//평가손익 = 평가금액 - 장부금액
				insertModel.setOpr01TrPl(evalAmt- book.getBok01BookAmt()); 
				
				insertModel.setOpr01BookId(book.getBok01BookId());
				insertModel.setOpr01BookAmt(book.getBok01BookAmt());
				insertModel.setOpr01StatusCd("0");
				
				contRepository.insert(insertModel);
				
				// 처리/취소 Main 호풀
				resultmsg = procMain("P", insertModel);
				checkCnt ++;
			}
			// 평가 취소 (평가완료 건만 해당)
			else if("C".equals(procType) && "true".equals(book.getBok01EvalYn())) {
				
				// 거래내역 가져오기
				Opr01Cont cont = contRepository.selectOne(book.getBok01ContId());
				//처리/취소 Main 호풀
				resultmsg = procMain("C", cont);
				checkCnt++;
			}
		}

		if(checkCnt == 0 ) {
			if("P".equals(procType)) {
				resultmsg = "평가처리대상이 없습니다.";
			} else {
				System.out.println("실행4");
				resultmsg = "평가취소 대상이 없습니다.";
			}
			throw new AssetException(resultmsg);
		}
		
		return resultmsg;
	}
	
	public List<Opr01Cont> selectList(QueryAttr queryAttr){
		return contRepository.selectList(queryAttr);
	}
	
	public Opr01Cont selectOne(String contId) {
		return contRepository.selectOne(contId);
	}
	
	@Transactional
	public String insert(Opr01Cont opr01Cont) throws Exception {
		// 거래 내역 insert 전에 해야할 일들
		//*** 체결아이디 SET 시퀀스를 활용해서 자동으로 생성함 오늘 날짜 + 시퀀스 (f_seq()함수 활용)
		opr01Cont.setOpr01ContId(contRepository.newContId());
		
		
		//*** 보유원장 Id SET

		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("holdDate", opr01Cont.getOpr01ContDate()); //체결일자 = 보유일자 = 오늘날짜
		queryAttr.put("fundCode", opr01Cont.getOpr01FundCd()); // 펀드
		queryAttr.put("itemCode", opr01Cont.getOpr01ItemCd()); // 종목
		
		String bookId = bookRepository.getBookId(queryAttr); //현재거래의 원장 아이디 Get (보유 원장이 있는지 없는 지 확인)
		
		if(bookId == null) { // 현재 보유 원장이 없다면
			bookId = contRepository.getNewSeq(); // 새로운 시퀀스를 가지고와서 
		}
		
		opr01Cont.setOpr01BookId(bookId); // 보유원장 Id로 넣고
		
		//*** 장부 상태 SET
		opr01Cont.setOpr01StatusCd("0"); // 미처리로 설정 (거내내역, 원장 ,분개장 모두 처리를 하면 완료로 변경)
		
		
		// 체결내역 INSERT
		int insertCnt = contRepository.insert(opr01Cont);
		
		// 처리 Main 호출 (보유 원장생성, 분개장 생성)
		String resultMsg = this.procMain("P", opr01Cont);
		
		return resultMsg;
	
	}
	@Transactional
	public String procMain(String procType, Opr01Cont opr01Cont) throws Exception{
		String resultMsg= "Y";
		int procCnt;
		
		//---------------------------------
		//처리(P) & 원장이월(A)
		//---------------------------------
		
		//로그인할 때(A), 거래 처리할 때(P) 부름.
		if("P".equals(procType) || "A".equals(procType)) {
			
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			
			//----------------------------------------
			//원장이월
			//----------------------------------------
			//최근 보유일자 GET
			String lastDateStr = bookRepository.getLastHoldDate();
			if(lastDateStr != null) {
				// ' 최근 보유일자 < 체결일 ' 인 경우에는 원장 이월 처리
				Date lastDate = transFormat.parse(lastDateStr); // 최근 보유일자
				Date contDate = transFormat.parse(opr01Cont.getOpr01ContDate()); // 체결일
				
				// 최근 보유일자가 체결일 보다 낮으면 (이월을 해야한다면)
				if(lastDate.compareTo(contDate) < 0) { 
					
					//원장이월 시작일자 = 최근 보유일자 + 1 
					Calendar cal = Calendar.getInstance();
					cal.setTime(lastDate);
					cal.add(Calendar.DATE,1);
					String startDate = dateFormat.format(cal.getTime());
				
					//원장이월 종료일자 = 체결일자
					String closeDate = opr01Cont.getOpr01ContDate();
					
					// 이월기간 GET
					QueryAttr dateAttr = new QueryAttr();
					dateAttr.put("startDate", startDate);
					dateAttr.put("closeDate", closeDate);
					List<Com03Date> dateList = dateRepository.selectListByPeriod(dateAttr);
					
					for(Com03Date dateModel : dateList) {
						//원장 이월을 날마다 함. 원장이 만들어지지 않은 날짜 모두 만드는 거임
						QueryAttr insertCondition = new QueryAttr();
						insertCondition.put("holdDate", dateModel.getCom03Day());
						procCnt = bookRepository.insertbyDayBefor(insertCondition);
					}
				}
				
				
			} //원장이월 END
			
			if("P".equals(procType)) {
				// 원장 모듈 호출
				resultMsg = bookService.creatBook(opr01Cont);
				//처리상태 '완료' update
				opr01Cont.setOpr01StatusCd("1"); // (1: 처리완료)
				procCnt = contRepository.update(opr01Cont);
			}
			
			
			
		}
	 	
		//---------------------------------
		// 취소
		//---------------------------------
		else if("C".equals(procType)) {
			
			// 취소대상 체결 내역의 체결상태 '9:취소' Update
			opr01Cont.setOpr01StatusCd("9"); // 취소
			procCnt = contRepository.update(opr01Cont); // 취소 반영
			
			// 취소대상 체결 내역의 당일 원장을 삭제
			QueryAttr bookCondition = new QueryAttr();
			bookCondition.put("bookId", opr01Cont.getOpr01BookId());
			bookCondition.put("holdDate",opr01Cont.getOpr01ContDate());
			procCnt = bookRepository.deleteByBookId(bookCondition);
			
			// 전일 원장 이월
			procCnt = bookRepository.insertbyDayBefor(bookCondition);
			System.out.println("실행3");
			// 당일 원장 거래반영 : 같은 원장에 반영된 다른 체결내역들 재처리
			QueryAttr otherCondition = new QueryAttr();
			otherCondition.put("opr01ContDate", opr01Cont.getOpr01ContDate());
			otherCondition.put("opr01BookId",  opr01Cont.getOpr01BookId());
			List<Opr01Cont> otherContList = contRepository.seleceByBookId(otherCondition);
			
			System.out.println("실행4");
			for(Opr01Cont otherCont : otherContList) {
				System.out.println("실행5");
				resultMsg = bookService.creatBook(otherCont);
				System.out.println("실행6");
			}
		
		}
		
		return resultMsg; 
	}
	
	
	//취소 모듈
	@Transactional
	public String delete(Opr01Cont opr01Cont) throws Exception {
		//취소처리전 평가여부 확인
				QueryAttr bookCondition = new QueryAttr();
				bookCondition.put("bookId"  , opr01Cont.getOpr01BookId());
				bookCondition.put("holdDate", opr01Cont.getOpr01ContDate());

				Bok01Book book = bookRepository.selectOneByBookId(bookCondition);
				
				if("true".equals(book.getBok01EvalYn())) {
					throw new AssetException("평가처리된 종목입니다. 평가취소 후 가능합니다.");
				}
	
		// 처리 Main 호출 (보유 원장정리, 분개장은 안건들여)
		String resultMsg = this.procMain("C", opr01Cont);
		return resultMsg;
	
	}

}
