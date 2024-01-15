package kr.co.kfs.assetedu.controller.jnl;

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

import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Jnl10AcntService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/jnl/acnt")
public class AcntController {
	
	@Autowired
	Jnl10AcntService service;
	
	@Autowired
	Com02CodeService codeService;
	
	@GetMapping("list")
	public String list(String searchText, Model model
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber) {
		
	
		
		model.addAttribute("pageTitle", "계정목록");
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = service.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List <Jnl10Acnt> list = service.selectList(queryAttr);
		
		
	
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		model.addAttribute(list);
		
		return "/jnl/acnt/list"; 
	}
	
	@GetMapping("insert")
	public String insert(@Valid @ModelAttribute(value="jnl10Acnt") Jnl10Acnt jnl10Acnt, BindingResult bindingResult
			,Model model) {
		
		model.addAttribute("pageTitle","계정과목추가");
		
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
		return "/jnl/acnt/insert"; 
	}
	
	@PostMapping("insert")
	public String postInsert(@Valid @ModelAttribute(value="jnl10Acnt") Jnl10Acnt jnl10Acnt, BindingResult bindingResult
			,Model model,RedirectAttributes redirectAttr) {

		if(bindingResult.hasErrors()) {

			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
			
			return "/jnl/acnt/insert"; 
		}
		
		String msg;
		
		Jnl10Acnt already = service.selectOne(jnl10Acnt);
		if(already != null) {
			
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 계정으로 등록되어있습니다.", already.getJnl10AcntCd(), already.getJnl10AcntNm());
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
			bindingResult.addError(new FieldError("", "", msg));
		
			return "/jnl/acnt/insert"; 
		}
		service.insert(jnl10Acnt);
		msg = String.format("\"%s\" 계정이 등록되었습니다", jnl10Acnt.getJnl10AcntNm());
		redirectAttr.addAttribute("mode", "insert");
		redirectAttr.addAttribute("msg", msg);
		String jnl10AcntCd = jnl10Acnt.getJnl10AcntCd();
		redirectAttr.addAttribute("jnl10AcntCd", jnl10AcntCd);
		
		return "redirect:/jnl/acnt/success";
	}
	
	@GetMapping("update")
	public String update(String jnl10AcntCd, Model model) {
		
		model.addAttribute("pageTitle","상세/수정");
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
		
		Jnl10Acnt  jnl10Acnt = new Jnl10Acnt();
		jnl10Acnt.setJnl10AcntCd(jnl10AcntCd);
		Jnl10Acnt selectjnl10 = service.selectOne(jnl10Acnt);
		
		model.addAttribute("jnl10Acnt",selectjnl10);
		
		return "/jnl/acnt/update_form"; 
	}
	
	@PostMapping("update")
	public String updatePost(@Valid @ModelAttribute(value="jnl10Acnt") Jnl10Acnt jnl10Acnt, BindingResult bindingResult, Model model,RedirectAttributes redirectAttr ) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("pageTitle","상세/수정");
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
			return "/jnl/acnt/update_form"; 
		}
		
		String msg;
		
			service.update(jnl10Acnt);
			msg = String.format("\"%s\" 계정이 수정되었습니다", jnl10Acnt.getJnl10AcntNm());
			redirectAttr.addAttribute("mode", "update");
			redirectAttr.addAttribute("msg", msg);
			String jnl10AcntCd = jnl10Acnt.getJnl10AcntCd();
			redirectAttr.addAttribute("jnl10AcntCd", jnl10AcntCd);
			return "redirect:/jnl/acnt/success";
		
	}
	
	@GetMapping("success")
	public String success(String msg, String mode, String jnl10AcntCd, Model model) {
		model.addAttribute("pageTitle","계정등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("jnl10AcntCd", jnl10AcntCd);
		return "/jnl/acnt/success"; 
	}
	
	@GetMapping("delete")
	public String delete(String jnl10AcntCd) {
		Jnl10Acnt jnl10Acnt = new Jnl10Acnt();
		jnl10Acnt.setJnl10AcntCd(jnl10AcntCd);
		service.delete(jnl10Acnt);
		return "redirect:/jnl/acnt/list";
	}
}
