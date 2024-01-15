package kr.co.kfs.assetedu.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.ApiData;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Itm02EvalPrice;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Itm01ItemService;
import kr.co.kfs.assetedu.service.Itm02EvalPriceService;
import kr.co.kfs.assetedu.utils.AssetUtil;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private Itm01ItemService service;
	
	@Autowired
	private Com02CodeService codeService;
	
	@Autowired
	private Itm02EvalPriceService priceService;
	
	@GetMapping("list")
	public String list(String searchText, Model model
			,@RequestParam(value="pageSize", required= false, defaultValue= "10") Integer pageSize
			,@RequestParam(value="currentPageNumber", required= false, defaultValue= "1") Integer currentPageNumber) {
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		Long totalCount = service.selectCount(queryAttr);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		queryAttr.put("pageAttr", pageAttr);
		List<Itm01Item> list = service.selectList(queryAttr);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		return "item/list";
	}
	
	@GetMapping("insert")
	public String insert(@Valid @ModelAttribute(value="item") Itm01Item item, BindingResult bindingResult
			,Model model) {
		model.addAttribute("pageTitle","종목등록");
		model.addAttribute("item",new Itm01Item());
		model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
		model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
		model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
		
		return "item/insert_form"; 
	}
	
	@PostMapping("insert")
	public String insertPost(@Valid @ModelAttribute(value="item") Itm01Item item, BindingResult bindingResult
			,RedirectAttributes redirectAttr, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
			model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
			model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
			return "item/insert_form"; 
		}
		
		String msg;
	
		//종목코드 중복체크
		Itm01Item checkItem = service.selectOne(item);
		if(checkItem != null){
			
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 종목으로 등록되어있습니다.", checkItem.getItm01ItemCd(), checkItem.getItm01ItemNm());
			model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
			model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
			model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
			bindingResult.addError(new FieldError("", "", msg));
			
			return "item/insert_form";
		}else {
			service.insert(item);
			msg = String.format("\"%s\" 주식종목이 등록되었습니다", item.getItm01ItemNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg", msg);
			String itemCd = item.getItm01ItemCd();
			redirectAttr.addAttribute("itemCd", itemCd);
			return "redirect:/item/success";
		}
		
	}
	
	@GetMapping("update")
	public String update(String itm01ItemCd,Model model) {
		model.addAttribute("pageTitle","상세/수정");
		model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
		model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
		model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
		Itm01Item item1 = new Itm01Item();
		item1.setItm01ItemCd(itm01ItemCd);
		Itm01Item selectItem = service.selectOne(item1);
		model.addAttribute("item",selectItem);		
		return "item/update_form"; 
	}
	
	@PostMapping("update")
	public String updatePost(@Valid @ModelAttribute(value="item") Itm01Item item, BindingResult bindingResult, Model model,RedirectAttributes redirectAttr ) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
			model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
			model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
			return "item/update_form"; 
		}
		
		String msg;
			service.update(item);
			msg = String.format("\"%s\" 주식종목이 수정되었습니다", item.getItm01ItemNm());
			redirectAttr.addAttribute("mode", "update");
			redirectAttr.addAttribute("msg", msg);
			String itemCd = item.getItm01ItemCd();
			redirectAttr.addAttribute("itemCd", itemCd);
			return "redirect:/item/success";
		
	}
	
	@GetMapping("delete")
	public String delete(String itm01ItemCd) {
		Itm01Item item = new Itm01Item();
		item.setItm01ItemCd(itm01ItemCd);
		service.delete(item);
		return "redirect:/item/list";
	}
	
	@GetMapping("success")
	public String success(String msg, String mode, String itemCd, Model model) {
		model.addAttribute("msg",msg);
		model.addAttribute("mode",mode);
		model.addAttribute("itemCd",itemCd);
		return "/item/success";
	}
	
	/**
	 * 평가단가 조회
	 */
	@GetMapping("price")
	public String price(String searchText, String stdDate,  Model model) {

		model.addAttribute("pageTitle","평가단가 리스트");
		
		if(Objects.isNull(stdDate)) {
			stdDate = AssetUtil.today();
		} else {
			stdDate = AssetUtil.removeDash(stdDate);
		}
		
		QueryAttr condition = new QueryAttr();
		condition.put("searchText", searchText);
		condition.put("stdDate"   , stdDate);
		
		List<Itm02EvalPrice> list = priceService.selectList(condition);
		
		model.addAttribute("list"   , list);
		model.addAttribute("stdDate", AssetUtil.displayYmd(stdDate));
		
		return "/item/price";
	}
	
	/**
	 * 평가단가 입력&변경 (ajax로 modal에서 호출)
	 * 
	 * @param data
	 * @return
	 */
	@ResponseBody
	@PostMapping("price_update")
	public String price_update(@Valid @RequestBody Itm02EvalPrice itm02EvalPrice) {



		itm02EvalPrice.setItm02ApplyDate (AssetUtil.removeDash (itm02EvalPrice.getItm02ApplyDate()));
		itm02EvalPrice.setItm02ApplyPrice(AssetUtil.removeComma(itm02EvalPrice.getItm02ApplyPrice()+""));
		
	
		ApiData apiData = new ApiData();
		try {
			int count = priceService.update(itm02EvalPrice);
			
			apiData.put("count", count);
			apiData.put("result", "OK");
			apiData.put("itm02EvalPrice", itm02EvalPrice);
			apiData.put("msg", "저장이 완료되었습니다. (수정된 레코드갯수 : "+count+")" );
			
		} catch (Exception e) {
			apiData.put("result", "NK:" + e.getMessage());
		}
		return apiData.toJson();
	}
	

	
	
}
