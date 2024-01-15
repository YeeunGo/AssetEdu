package kr.co.kfs.assetedu.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Opr01Cont;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Opr01ContService;
import kr.co.kfs.assetedu.servlet.exception.AssetException;
import kr.co.kfs.assetedu.utils.AssetUtil;

@Controller
@RequestMapping("/opr")
public class OprContController {
	
	@Autowired
	private Opr01ContService service;
	
	@Autowired
	private Com02CodeService codeService;
	
	

	@GetMapping("buy_list")
	public String buy_list(String frDate, String toDate, String searchText, Model model) {
		
		if(Objects.isNull(frDate)) {
			frDate = AssetUtil.today();
		}else {
			frDate = AssetUtil.removeDash(frDate);
		}
		
		if(Objects.isNull(toDate)) {
			toDate = AssetUtil.today();
		}else {
			toDate = AssetUtil.removeDash(toDate);
		}
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("frDate", frDate);
		queryAttr.put("searchText", searchText);
		queryAttr.put("toDate", toDate);
		queryAttr.put("pageType", "BUY");
		List <Opr01Cont> list = service.selectList(queryAttr);
		model.addAttribute("list", list);
		model.addAttribute("frDate", AssetUtil.displayYmd(frDate));
		model.addAttribute("toDate",  AssetUtil.displayYmd(frDate));
		return "opr/buy_list";
	}
	
	@GetMapping("buy_insert")
	public String insert(Model model) {
		
		Opr01Cont cont = new Opr01Cont();
		cont.setOpr01ContDate(AssetUtil.today());
		
		model.addAttribute("pageTitle","매수 운용지시 입력처리");
		model.addAttribute("cont",cont);
		model.addAttribute("trCdList", codeService.trCodeList("BUY"));
		
		return "/opr/buy_insert";  
	}
	
	///거래 내역 insert
	@PostMapping("buy_insert")
	public String insertPost(@Valid @ModelAttribute(value="cont") Opr01Cont cont, BindingResult bindingResult
			,RedirectAttributes redirectAttr, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("trCdList", codeService.trCodeList("BUY"));
			return "/opr/buy_insert";
		}
		String msg;
		String resultMsg;
		
		try {
		// insert (거래처리 -> 원장 생성 모듈 -> 분개장 생성 모듈)
		resultMsg = service.insert(cont);
		}
		catch (AssetException e) {
			resultMsg = e.getMessage();
		}catch(Exception e) {
			resultMsg = e.getMessage();
			if(resultMsg == null) {
				resultMsg = e.toString();
			}
		}
		
		if(!"Y".equals(resultMsg)) {
			model.addAttribute("trCdList", codeService.trCodeList("BUY"));
			bindingResult.addError(new FieldError("","",resultMsg));
			return "opr/buy_insert";
		}else {
			msg = String.format("\"%s %s주\" 매수처리가 완료되었습니다", cont.getOpr01ItemNm(), cont.getOpr01Qty());
			redirectAttr.addAttribute("mode","insert");
			redirectAttr.addAttribute("msg",msg);
			return "redirect:/opr/buy_success";
		}
		
	}
	
	@GetMapping("buy_delete")
	public String buy_delte( @ModelAttribute(value="cont") Opr01Cont cont,Model model) throws UnsupportedEncodingException{
		
		cont = service.selectOne(cont.getOpr01ContId());
		model.addAttribute("cont",cont);	
		return "opr/buy_delete"; 
	}
	

	@PostMapping("/buy_delete")
	public String buy_delte_post( @ModelAttribute(value="cont") Opr01Cont cont, BindingResult bindingResult,RedirectAttributes redirectAttr, Model model) {

		
		String msg;
		String resultMsg ="";
		
		try {
			cont = service.selectOne(cont.getOpr01ContId());
	       resultMsg = service.delete(cont);
		}
		catch (AssetException e) {
			resultMsg = e.getMessage();
		}catch(Exception e) {
			resultMsg = e.getMessage();
		}
		
		if(!"Y".equals(resultMsg)) {
			bindingResult.addError(new FieldError("","",resultMsg));
			return "opr/buy_delte";
		}else {
			msg = String.format("\"%s %s주\" 매수 취소처리가 완료되었습니다", cont.getOpr01ItemNm(), cont.getOpr01Qty());
			redirectAttr.addAttribute("mode","delete");
			redirectAttr.addAttribute("msg",msg);
			return "redirect:/opr/buy_success";
		}
		
	}
	
	@GetMapping("sell_list")
	public String sellList (String frDate, String toDate, String searchText,  Model model) {
		if(Objects.isNull(frDate)) {
			frDate = AssetUtil.today();
		} else {
			frDate = AssetUtil.removeDash(frDate);
		}
		
		if(Objects.isNull(toDate)) {
			toDate = AssetUtil.today();
		} else {
			toDate  = AssetUtil.removeDash(toDate);
		}
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("frDate", frDate);
		queryAttr.put("toDate", toDate);
		queryAttr.put("searchText", searchText);
		queryAttr.put("pageType", "SELL");
		
		List<Opr01Cont> list = service.selectList(queryAttr);
		model.addAttribute("list"  , list);
		model.addAttribute("frDate", AssetUtil.displayYmd(frDate));
		model.addAttribute("toDate", AssetUtil.displayYmd(toDate));

		return "/opr/sell_list";
	}
	

	@GetMapping("sell_insert")
	public String sellInsert(Model model) {
		model.addAttribute("pageTitle","주식매도 체결내역 등록 및 처리");
		Opr01Cont cont = new Opr01Cont();
		cont.setOpr01ContDate(AssetUtil.ymd());
		model.addAttribute("cont",cont);
		model.addAttribute("trCdList", codeService.trCodeList("SELL"));

		return "/opr/sell_insert_form";
	}
	
	@PostMapping("sell_insert")
	public String sellInsert_1(@Valid @ModelAttribute("cont") Opr01Cont cont, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException {

		if(bindingResult.hasErrors()) {
			model.addAttribute("trCdList", codeService.trCodeList("SELL"));
			return "/opr/sell_insert_form";			
		}

		String msg;
		String resultMsg;
		
		try {
			resultMsg = service.insert(cont);
		}
		catch (AssetException e) {
			resultMsg = e.getMessage();
		}
		catch (Exception e) {
			resultMsg = e.getMessage();
		}

		if("Y".equals(resultMsg)) {
			msg = String.format("\"%s %s주\" 매도처리가 완료되었습니다.", cont.getOpr01ItemNm(), cont.getOpr01Qty());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/opr/sell_success";
		} else {
			model.addAttribute("trCdList", codeService.trCodeList("SELL"));			
			bindingResult.addError(new FieldError("", "", resultMsg));			
			return "/opr/sell_insert_form";
		}
	}
	
	@GetMapping("sell_success")
	public String sellSuccess(String msg, String mode, Long contId, Model model) {
		model.addAttribute("pageTitle","매도처리");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("contId", contId);
		return "/opr/sell_success";
	}
	
	@GetMapping("buy_success")
	public String buy_success(String msg, String mode, String contId, Model model) {
		model.addAttribute("pageTitle", "매수처리");
		model.addAttribute("msg",msg);
		model.addAttribute("mode",mode);
		model.addAttribute("contId", contId);
		return "/opr/buy_success";
	}
	
	@GetMapping("sell_delete")
	public String sellDelete(@ModelAttribute("cont") Opr01Cont cont, Model model) throws UnsupportedEncodingException {


		cont = service.selectOne(cont.getOpr01ContId());
		model.addAttribute("cont", cont);

		return "/opr/sell_delete_form";
	}
	
	@PostMapping("sell_delete")
	public String sellDelete(@ModelAttribute("cont") Opr01Cont cont, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException {

		String msg;
		String resultMsg;
		
		try {
			cont = service.selectOne(cont.getOpr01ContId());
			resultMsg = service.delete(cont);
		}
		catch (AssetException e) {
			resultMsg = e.getMessage();
		}
		catch (Exception e) {
			resultMsg = e.getMessage();
		}
		
		if("Y".equals(resultMsg)) {
			msg = String.format("\"%s %s주\" 매도취소가 완료되었습니다.", cont.getOpr01ItemNm(), cont.getOpr01Qty());
			redirectAttr.addAttribute("mode", "delete");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/opr/sell_success";
		} else {
    		bindingResult.addError(new FieldError("", "", resultMsg));			
			return "/opr/sell_delete_form";
		}
	}

}
