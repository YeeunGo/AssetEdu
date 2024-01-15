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

import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Jnl11ReprAcntService;

@Controller
@RequestMapping("/jnl/repr_acnt/")
public class ReprAcntController {
	
	@Autowired
	Jnl11ReprAcntService service;
	
	@Autowired
	Com02CodeService codeService;
	
	@GetMapping("list")
	public String list (String searchText, Model model
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber) {
		
		
		model.addAttribute("pageTitle", "대표계정목록");
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = service.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List <Jnl11ReprAcnt> list = service.selectList(queryAttr);
		
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		model.addAttribute(list);
		return "/jnl/repr_acnt/list"; 
	}
	
	@GetMapping("insert")
	public String insert(@Valid @ModelAttribute(value="jnl11ReprAcnt") Jnl11ReprAcnt jnl11ReprAcnt, BindingResult bindingResult
			,Model model) {
		
		model.addAttribute("pageTitle","대표계정추가");
		
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		
		return "/jnl/repr_acnt/insert_form"; 
	}
	
	@PostMapping("insert")
	public String postInsert(@Valid @ModelAttribute(value="jnl11ReprAcnt") Jnl11ReprAcnt jnl11ReprAcnt, BindingResult bindingResult
			,Model model,RedirectAttributes redirectAttr) {

		if(bindingResult.hasErrors()) {

			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			
			return "/jnl/repr_acnt/insert_form"; 
		}
		
		String msg;
		
		Jnl11ReprAcnt already = service.selectOne(jnl11ReprAcnt);
		if(already != null) {
			
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 대표계정으로 등록되어있습니다.", already.getJnl11ReprAcntCd(), already.getJnl11ReprAcntNm());
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			bindingResult.addError(new FieldError("", "", msg));
		
			return "/jnl/repr_acnt/insert_form"; 
		}
		service.insert(jnl11ReprAcnt);
		msg = String.format("\"%s\" 대표계정코드가 등록되었습니다", jnl11ReprAcnt.getJnl11ReprAcntNm());
		redirectAttr.addAttribute("mode", "insert");
		redirectAttr.addAttribute("msg", msg);
		String jnl11ReprAcntCd = jnl11ReprAcnt.getJnl11ReprAcntCd();
		redirectAttr.addAttribute("jnl11ReprAcntCd", jnl11ReprAcntCd);
		
		return "redirect:/jnl/repr_acnt/success";
	}
	
	@GetMapping("success")
	public String success(String msg, String mode, String jnl11ReprAcntCd, Model model) {
		model.addAttribute("pageTitle","대표계정코드등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("jnl11ReprAcntCd", jnl11ReprAcntCd);
		return "/jnl/repr_acnt/success"; 
	}
	
	@GetMapping("update")
	public String update(String jnl11ReprAcntCd, Model model) {
		model.addAttribute("pageTitle","상세/수정");
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		
		Jnl11ReprAcnt  jnl11ReprAcnt = new Jnl11ReprAcnt();
		jnl11ReprAcnt.setJnl11ReprAcntCd(jnl11ReprAcntCd);
		Jnl11ReprAcnt selectjnl11 = service.selectOne(jnl11ReprAcnt);
		model.addAttribute("jnl11ReprAcnt",selectjnl11);		
		return "/jnl/repr_acnt/update_form"; 
	}
	
	@PostMapping("update")
	public String updatePost(@Valid @ModelAttribute(value="jnl11ReprAcnt") Jnl11ReprAcnt jnl11ReprAcnt, BindingResult bindingResult, Model model,RedirectAttributes redirectAttr ) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("pageTitle","상세/수정");
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			return "/jnl/repr_acnt/update_form"; 
		}
		
		String msg;
		
			service.update(jnl11ReprAcnt);
			msg = String.format("\"%s\" 대표계정코드가 수정되었습니다", jnl11ReprAcnt.getJnl11ReprAcntNm());
			redirectAttr.addAttribute("mode", "update");
			redirectAttr.addAttribute("msg", msg);
			String jnl11ReprAcntCd = jnl11ReprAcnt.getJnl11ReprAcntCd();
			redirectAttr.addAttribute("jnl11ReprAcntCd", jnl11ReprAcntCd);
			return "redirect:/jnl/repr_acnt/success";
		
	}
	
	@GetMapping("delete")
	public String delete(String jnl11ReprAcntCd) {
		Jnl11ReprAcnt jnl11ReprAcnt = new Jnl11ReprAcnt();
		jnl11ReprAcnt.setJnl11ReprAcntCd(jnl11ReprAcntCd);
		service.delete(jnl11ReprAcnt);
		return "redirect:/jnl/repr_acnt/list";
	}
}
