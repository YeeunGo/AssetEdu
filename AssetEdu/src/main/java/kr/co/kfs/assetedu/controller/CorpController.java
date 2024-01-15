package kr.co.kfs.assetedu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com01CorpService;
import kr.co.kfs.assetedu.service.Com02CodeService;

@Controller
@RequestMapping("/corp")
public class CorpController {
	@Autowired
	private Com01CorpService service;
	@Autowired
	private Com02CodeService codeService;
	
	@GetMapping("list")
	public String list (String searchText, Model model, String com01CorpType
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber){
		model.addAttribute("pageTitle","기관정보-리스트");
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = service.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		queryAttr.put("com01CorpType", com01CorpType);
		List<Com01Corp> list  = service.selectList(queryAttr);
		List<Com01Corp> corpType = service.selectType();
		model.addAttribute("pageAttr", pageAttr);
		model.addAttribute("corpTypeList",corpType);
		model.addAttribute("list", list);
		return "/corp/list"; 
	}
	
	@GetMapping("insert")
	public String insert(Model model) {
		model.addAttribute("corp", new Com01Corp());
		model.addAttribute("corpTypeList",codeService.codeList("CorpType"));
		return "/corp/insert_form" ;
	}
	
	@PostMapping("insert")
	public String insertPost(@Valid @ModelAttribute("corp") Com01Corp corp, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException{
		if(bindingResult.hasErrors()) {
			model.addAttribute("corpTypeList", codeService.codeList("CorpType"));
			return "/corp/insert_form";	
		}
		String msg;
		Com01Corp checkCorp = service.selecOne(corp);
		if(checkCorp != null) {
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 으로 등록되어있습니다.", checkCorp.getCom01CorpCd(), checkCorp.getCom01CorpNm());
			bindingResult.addError(new FieldError("", "", msg));
			model.addAttribute("corpTypeList", codeService.codeList("CorpType"));
			return "/corp/insert_form";
		}else {
			int affectedCount = service.insert(corp);
			msg = String.format("\"%s\" 기관정보가 등록되었습니다", corp.getCom01CorpNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/corp/success" ;
		}
	}
	
	@GetMapping("update")
	public String update(@ModelAttribute("corp") Com01Corp corp, Model model) throws UnsupportedEncodingException {
		model.addAttribute("corpTypeList", codeService.codeList("CorpType"));
		corp = service.selectOne(corp);
		model.addAttribute("corp", corp);
		return "/corp/update_form";
	}
	
	@PostMapping("update")
	public String update_form(@ModelAttribute("corp") Com01Corp corp, Model model) throws UnsupportedEncodingException {
		service.update(corp);	
		String msg = String.format("\"%s\" 기관정보가 수정되었습니다.", corp.getCom01CorpNm());
		return "redirect:/corp/success?mode=update&corpCd=" + corp.getCom01CorpCd()+"&msg=" + URLEncoder.encode(msg,"UTF-8");
	}
	
	@GetMapping("delete")
	public String delete(String com01CorpCd) {
		System.out.println("ss");
		service.delete(com01CorpCd);
		return "redirect:/corp/list"; 
	}
	
	@GetMapping("success")
	public String success(String msg, String mode, String corpCd, Model model) {
		model.addAttribute("pageTitle","기관정보등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("corpCd", corpCd);
		return "/corp/success";
	}
}
