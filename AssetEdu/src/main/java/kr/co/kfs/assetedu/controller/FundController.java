package kr.co.kfs.assetedu.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Fnd01Fund;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Fnd01FundService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/fund")
public class FundController {
	
	@Autowired
	Fnd01FundService service;
	
	@Autowired
	Com02CodeService codeService;
	
	@GetMapping("list")
	public String list(String searchText, Model model
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber){
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("펀드리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","펀드정보-리스트");
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = service.selectCount(queryAttr);
		service.selectList(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Fnd01Fund> list = service.selectList(queryAttr);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		return "/fund/list";
	}
	
	@GetMapping("insert")
	public String insert(@ModelAttribute(value="fund") Fnd01Fund fund, Model model) {
		model.addAttribute("pageTitle","펀드등록");
		model.addAttribute("fundTypeList",codeService.codeList("FundType"));
		model.addAttribute("publicCdList",codeService.codeList("PublicCode"));
		model.addAttribute("unitCdList",codeService.codeList("FundUnitCode"));
		model.addAttribute("parentCdList",codeService.codeList("FundParentCode"));
		return "fund/insert_form";
	}
	
	@PostMapping("insert")
	public String insertPost(@Valid @ModelAttribute(value="fund") Fnd01Fund fund,  BindingResult bindingResult
			,RedirectAttributes redirectAttr, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("pageTitle","펀드등록");
			model.addAttribute("fundTypeList",codeService.codeList("FundType"));
			model.addAttribute("publicCdList",codeService.codeList("PublicCode"));
			model.addAttribute("unitCdList",codeService.codeList("FundUnitCode"));
			model.addAttribute("parentCdList",codeService.codeList("FundParentCode"));
			return "fund/insert_form";
		}
		
		String msg;
		//종목코드 중복체크
		Fnd01Fund chekfund = service.selectOne(fund);
		if(chekfund != null){
			
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 종목으로 등록되어있습니다.", chekfund.getFnd01FundCd(), chekfund.getFnd01FundNm());
			model.addAttribute("fundTypeList",codeService.codeList("FundType"));
			model.addAttribute("publicCdList",codeService.codeList("PublicCode"));
			model.addAttribute("unitCdList",codeService.codeList("FundUnitCode"));
			model.addAttribute("parentCdList",codeService.codeList("FundParentCode"));
			bindingResult.addError(new FieldError("", "", msg));
			
			return "fund/insert_form";
		}else {
			service.insert(fund);
			msg = String.format("\"%s\" 주식종목이 등록되었습니다", fund.getFnd01FundNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg", msg);
			String fundCd = fund.getFnd01FundCd();
			redirectAttr.addAttribute("fundCd", fundCd);
			
		return "redirect:/fund/success";
	}
	

}
	
	@GetMapping("update")
	public String update(String fnd01FundCd, Model model) {
		model.addAttribute("pageTitle","상세/수정");
		model.addAttribute("fundTypeList",codeService.codeList("FundType"));
		model.addAttribute("publicCdList",codeService.codeList("PublicCode"));
		model.addAttribute("unitCdList",codeService.codeList("FundUnitCode"));
		model.addAttribute("parentCdList",codeService.codeList("FundParentCode"));
		Fnd01Fund fund = new Fnd01Fund();
		fund.setFnd01FundCd(fnd01FundCd);
		Fnd01Fund selectFund = service.selectOne(fund);
		model.addAttribute("fund", selectFund);
		return "fund/update_form"; 
	}
	
	@PostMapping("update")
	public String updatePost(@Valid @ModelAttribute(value="fund") Fnd01Fund fund, BindingResult bindingResult, Model model,RedirectAttributes redirectAttr) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("fundTypeList",codeService.codeList("FundType"));
			model.addAttribute("publicCdList",codeService.codeList("PublicCode"));
			model.addAttribute("unitCdList",codeService.codeList("FundUnitCode"));
			model.addAttribute("parentCdList",codeService.codeList("FundParentCode"));
			return "fund/update_form"; 
		}
		String msg;
		service.update(fund);
		msg = String.format("\"%s\" 펀드가 수정되었습니다", fund.getFnd01FundNm());
		redirectAttr.addAttribute("mode", "update");
		redirectAttr.addAttribute("msg", msg);
		String fundCd = fund.getFnd01FundCd();
		redirectAttr.addAttribute("fundCd", fundCd);
		return "redirect:/fund/success";
	}
	
	@GetMapping("delete")
	public String delete (String fnd01FundCd) {
		Fnd01Fund fund = new Fnd01Fund();
		fund.setFnd01FundCd(fnd01FundCd);
		service.delete(fund);
		return "redirect:/fund/list"; 
	}
	
	
	@GetMapping("success")
	public String success(String msg, String mode, String itemCd, Model model) {
		model.addAttribute("msg",msg);
		model.addAttribute("mode",mode);
		model.addAttribute("itemCd",itemCd);
		return "/fund/success";
	}
}