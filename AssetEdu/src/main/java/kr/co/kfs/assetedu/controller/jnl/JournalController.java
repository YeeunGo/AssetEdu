package kr.co.kfs.assetedu.controller.jnl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.model.Jnl01Journal;
import kr.co.kfs.assetedu.service.Jnl01JournalService;
import kr.co.kfs.assetedu.utils.AssetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 분개장 콘트롤러
 *
 */
@Slf4j
@Controller
@RequestMapping("/jnl/journal")
public class JournalController {
	
	private final String baseUrl = "/jnl/journal";
	@Autowired
	private Jnl01JournalService service;
	
	
	@GetMapping("list")
	public String list(String searchText, String frDate, String toDate,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("분개장리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","분개장");
		
		if(Objects.isNull(frDate)) {
			frDate = AssetUtil.today();
		}else {
			frDate = frDate.replaceAll("\\D", "");
		}
		if(Objects.isNull(toDate)) {
			toDate = AssetUtil.today();
		}else {
			toDate = toDate.replaceAll("\\D", "");
		}
		QueryAttr condition = new QueryAttr();
		condition.put("searchText", searchText);
		condition.put("frDate", frDate);
		condition.put("toDate", toDate);
		List<Jnl01Journal> list = service.selectList(condition);
		model.addAttribute("list", list);
		model.addAttribute("frDate", AssetUtil.displayYmd(frDate));
		model.addAttribute("toDate", AssetUtil.displayYmd(toDate));
		return baseUrl + "/list";
	}
	
}
