package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Jnl12Tr;
import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Bok01BookRepository;
import kr.co.kfs.assetedu.repository.Jnl12TrRepository;
import kr.co.kfs.assetedu.repository.Opr01ContRepository;
import kr.co.kfs.assetedu.servlet.exception.AssetException;

@Service
public class Bok01BookService {

	
	@Autowired
	private Bok01BookRepository bookRepository;
	

	@Autowired
	private Jnl12TrRepository jnl12Repository;
	
	@Autowired
	private Opr01ContRepository contRepository;
	
	@Autowired
	private Jnl01JournalService jnlService;
	
	public Long selectCount(QueryAttr condition){
		return bookRepository.selectCount(condition);
	}
	
	public List<Bok01Book> selectList(QueryAttr condition){
		return bookRepository.selectList(condition);
	}
	
	@Transactional
	public String creatBook( Opr01Cont opr01Cont) throws Exception{
		String resultMsg= "Y";
		int procCnt;
		
		//보유원장 GET
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("bookId", opr01Cont.getOpr01BookId());
		
		queryAttr.put("holdDate", opr01Cont.getOpr01ContDate());
		Bok01Book book = bookRepository.selectByBookId(queryAttr);
		if(book == null) { // 보유원장이 없다면
			book = new Bok01Book();
			book.setBok01BookId(opr01Cont.getOpr01BookId());
			book.setBok01HoldDate(opr01Cont.getOpr01ContDate());
			book.setBok01FundCd(opr01Cont.getOpr01FundCd());
			book.setBok01ItemCd(opr01Cont.getOpr01ItemCd());
			book.setBok01HoldQty(0l);
			book.setBok01EvalAmt(0l);
			book.setBok01EvalPl(0l);
			book.setBok01EvalYn("false");

		
		}
		
		//거래유형정보 GET
		Jnl12Tr Jnl12Tr = new Jnl12Tr();
		Jnl12Tr.setJnl12TrCd(opr01Cont.getOpr01TrCd());
		
	      Jnl12Tr trInfo = jnl12Repository.selectOne(Jnl12Tr);
	      if(trInfo.getJnl12InOutType() == null) {
	         resultMsg = "원장입출고구분값이 없습니다. 분개맵핑 정보를 확인하세요.";
	         throw new AssetException(resultMsg);
	      }
		
	    //------------
	      //1:입고 데이터 SET
	      //------------
	      
	      if("1".equals(trInfo.getJnl12InOutType())) {
	    	  
	    	  opr01Cont.setOpr01BookAmt(opr01Cont.getOpr01ContAmt());
	         //보유수량
	         Long holdQty = 0l;
	         
	         if(book.getBok01HoldQty()!=null) { // 보유원장이 있으면 가져오기
	            holdQty = book.getBok01HoldQty();
	         }
	         
	         book.setBok01HoldQty(holdQty+opr01Cont.getOpr01Qty());
	         //장부금액
	         Long bookAmt = 0l;
	         if(book.getBok01BookAmt()!=null) {  // 보유원장이 있으면 가져오기
	            bookAmt = book.getBok01BookAmt();
	         }
	         book.setBok01BookAmt(bookAmt+opr01Cont.getOpr01BookAmt());
	         //취득금액
	         Long purAmt = 0l;
	         if(book.getBok01PurAmt()!=null) { // 보유 원장이 있으면 가져오기
	            purAmt = book.getBok01PurAmt();
	         }
	         book.setBok01PurAmt(purAmt+opr01Cont.getOpr01BookAmt());
	      }
	      
	      
	    // 2 출고 
	      else if("2".equals(trInfo.getJnl12InOutType())) {
	    	  
	    	  //출고가능수량 체크
	    	  if(book.getBok01HoldQty() < opr01Cont.getOpr01Qty()) {
	    		  resultMsg = "매도수량잉 보유 수량보다 많습니다.";
	    		  throw new AssetException(resultMsg);
	    	  }
	    	  Long bookAmt;
	    	  Long purAmt;
	    	  //전액 매도
	    	  if(book.getBok01HoldQty() == opr01Cont.getOpr01Qty()) {
	    		// 장부금액 SET
	    		  bookAmt = book.getBok01BookAmt();
	    		  book.setBok01BookAmt(0l);
	    		//취득금액 SET
	    		  book.setBok01PurAmt(0l);
	    		//보유수량 SET
	    		  book.setBok01HoldQty(0l);
	    		  
	    	  }
	    	  //일부 매도
	    	  else {
	    	  // 장부금액 SET
	       	  bookAmt =roundDown( book.getBok01BookAmt() / book.getBok01HoldQty() * opr01Cont.getOpr01Qty(),1);
	    	  book.setBok01BookAmt(book.getBok01BookAmt()- bookAmt);
	    	  
	    	  //취득금액 SET
	    	   purAmt = roundDown(book.getBok01PurAmt() / book.getBok01HoldQty() * opr01Cont.getOpr01Qty(), 1);
	    	  book.setBok01PurAmt(book.getBok01PurAmt() - purAmt);
	    	  
	    	  //보유수량 SET
	    	  book.setBok01HoldQty(book.getBok01HoldQty() - opr01Cont.getOpr01Qty());
	    	  }
	    	  opr01Cont.setOpr01BookAmt(bookAmt);
	      }
	      
	    // 3: 원장변경(평가) 
	      else if("3".equals(trInfo.getJnl12InOutType())) {
	    	  book.setBok01EvalAmt(opr01Cont.getOpr01ContAmt());
	    	  book.setBok01EvalPl(opr01Cont.getOpr01TrPl());
	    	  book.setBok01EvalYn("true");
	      }
		
	      // 원장반영 없으면 insert/ 있으면 update
	       procCnt = bookRepository.upsert(book);
	       
	       //체결내역 장부금액 update
	       procCnt = contRepository.update(opr01Cont);
		

		// 분개모듈 호출
	       resultMsg = jnlService.createJournal(opr01Cont);
		
		return resultMsg;
	}
	
	private long roundDown(double number, double place) {
		double result = number / place;
		result = Math.floor(result);
		result *= place;
		return (long)result;
	}
	
	public List<Bok01Book> selectEvalList(QueryAttr queryAttr) {
		return bookRepository.selectEvalList(queryAttr);
	}

}
