package kr.co.kfs.assetedu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.Fnd01Fund;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Bok01BookService;
import kr.co.kfs.assetedu.service.Com01CorpService;
import kr.co.kfs.assetedu.service.Fnd01FundService;
import kr.co.kfs.assetedu.service.Itm01ItemService;
import kr.co.kfs.assetedu.service.Jnl10AcntService;
import kr.co.kfs.assetedu.service.Jnl11ReprAcntService;
import kr.co.kfs.assetedu.utils.AssetUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/popup")
public class PopupController {
	
	@Autowired
	private Com01CorpService cropservice;
	
	
	@Autowired
	private Fnd01FundService fundService;
	
	@Autowired
	private Jnl11ReprAcntService reprAcntService;
	
	@Autowired
	private Jnl10AcntService acntService;
	
	@Autowired
	private Itm01ItemService itemService;
	
	@Autowired
	private Bok01BookService bookService;
	
	@GetMapping("corp")
	public String corp(
					@RequestParam(value="corpCd", required = false) String corpCd 
					,@RequestParam(value="whichPage", required = false) String whichPage 
					, @RequestParam(value="corpNm", required = false) String corpNm
					, @RequestParam(value="searchText", required = false) String searchText
					,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
					,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
					,Model model) {
		
		//불러온 페이지에 따라 기관구분 다르게 설정
		String com01CorpType = null;
		if("common".equals(whichPage)) {
		    com01CorpType = "01"; // 일반
		} else if("asset".equals(whichPage)) {
		    com01CorpType ="04"; // 자산운용
		} else if("custodial".equals(whichPage)) {
		    com01CorpType ="02"; // 은행
		}else if("Adminis".equals(whichPage)) {
			com01CorpType ="05"; // 사무수탁사
		} else if("brk".equals(whichPage)) {
			com01CorpType = "03"; // 증권사
		}
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		queryAttr.put("com01CorpType", com01CorpType);
		
		Long totalCount = cropservice.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Com01Corp> list = cropservice.selectList(queryAttr);
		
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		model.addAttribute("corpCd", corpCd);
		model.addAttribute("corpNm",corpNm);
		model.addAttribute("whichPage", whichPage);
		
		return "/common/popup_corp";
	}
	
	@GetMapping("fund")
	public String fund(	@RequestParam(value="fundCd", required = false) String fundCd 
			,@RequestParam(value="whatType", required = false) String whatType 
			, @RequestParam(value="fundNm", required = false) String fundNm
			, @RequestParam(value="searchText", required = false) String searchText
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
			,Model model) {
		
		QueryAttr queryAttr = new QueryAttr();
		if("mofund".equals(whatType)) {
			String fnd01ParentCd = "2";
			queryAttr.put("fnd01ParentCd", fnd01ParentCd);
		} else if("notMofund".equals(whatType)) {
			queryAttr.put("notMofund","notMofund");
		}
		
		
		queryAttr.put("searchText", searchText);
		
		
		Long totalCount = fundService.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Fnd01Fund> list = fundService.selectList(queryAttr);
		
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		model.addAttribute("fundCd", fundCd);
		model.addAttribute("fundNm",fundNm);
		model.addAttribute("whatType", whatType);
		
		return "/common/popup_fund";
	}
	
	@GetMapping("jnl/repr-acnt")
	public String jnl(@RequestParam(value="reprAcntCdId", required = false) String reprAcntCdId 
			,@RequestParam(value="reprAcntNmId", required = false) String reprAcntNmId 
			, @RequestParam(value="searchText", required = false) String searchText
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
			,Model model) {
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = reprAcntService.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Jnl11ReprAcnt> list =  reprAcntService.selectList(queryAttr);
		model.addAttribute("list", list);
		model.addAttribute("reprCd",reprAcntCdId);
		model.addAttribute("reprNm",reprAcntNmId);
		model.addAttribute("pageAttr", pageAttr);
		return "/common/popup_jnl_repr_acnt";
	}
	
	@GetMapping("jnl/acnt/parent")
	public String acntParent(
			@RequestParam(value="slipYn", required = false) String slipYn 
			,@RequestParam(value="parentCdId", required = false) String parentCdId 
			,@RequestParam(value="parentNmId", required = false) String parentNmId 
			, @RequestParam(value="searchText", required = false) String searchText
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
			,Model model) {
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		if("F".equals(slipYn)) {
			queryAttr.put("jnl10SlipYn","false");
		} else {
			queryAttr.put("jnl10SlipYn","true");
		}
		
		Long totalCount = acntService.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Jnl10Acnt> list =  acntService.selectList(queryAttr);

		model.addAttribute("list", list);
		model.addAttribute("slipYn", slipYn);
		model.addAttribute("acntCd", parentCdId);
		model.addAttribute("acntNm", parentNmId);
		model.addAttribute("pageAttr", pageAttr);
		return "/common/popup_jnl_acnt";
		
	}
	
	@GetMapping("jnl/acnt/child")
	public String acntChild(
			@RequestParam(value="slipYn", required = false) String slipYn 
			,@RequestParam(value="acntCd", required = false) String acntCd 
			,@RequestParam(value="acntNm", required = false) String acntNm 
			, @RequestParam(value="searchText", required = false) String searchText
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
			,Model model) {
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		if("F".equals(slipYn)) {
			queryAttr.put("jnl10SlipYn","false");
		} else {
			queryAttr.put("jnl10SlipYn","true");
		}
		
		Long totalCount = acntService.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Jnl10Acnt> list =  acntService.selectList(queryAttr);

		model.addAttribute("list", list);
		model.addAttribute("slipYn", slipYn);
		model.addAttribute("acntCd", acntCd);
		model.addAttribute("acntNm", acntNm);
		model.addAttribute("pageAttr", pageAttr);
		return "/common/popup_jnl_acnt";
		
	}
	
	@GetMapping("item")
	public String item(@RequestParam(value="itemCd", required = false) String itemCd 
					  ,@RequestParam(value="itemNm", required = false) String itemNm 
					  , @RequestParam(value="searchText", required = false) String searchText
					  ,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
					  ,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber
					  ,Model model) {
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = itemService.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Itm01Item> list = itemService.selectList(queryAttr);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		return "/common/popup_item";
	}
	
	@GetMapping("book")
	public String book(String searchText, 
			String bookId, 
			String fundCd,
			String fundNm,
			String itemCd,
			String itemNm,
			String holdQty,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		
		QueryAttr condition = new QueryAttr();
		condition.put("searchText", searchText);
		condition.put("holdDate", AssetUtil.ymd());
		
		Long totalCount = bookService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		condition.putClass(pageAttr);
		
		List<Bok01Book> list = bookService.selectList(condition);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_book";
	}
	
}
