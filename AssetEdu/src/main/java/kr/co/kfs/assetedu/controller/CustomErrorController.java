package kr.co.kfs.assetedu.controller;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.kfs.assetedu.servlet.exception.AssetException;
import kr.co.kfs.assetedu.servlet.view.JsonView;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CustomErrorController extends AbstractErrorController {

	public CustomErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}
	private boolean isAjax(HttpServletRequest request) {
	    String requestedWithHeader = request.getHeader("X-Requested-With");
	    return "XMLHttpRequest".equals(requestedWithHeader);
	}
	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request, Model model) {
		
		ModelAndView mav = null ;
		if(isAjax(request)) {
			System.out.println("이것은 ajax request...................");
			mav = new ModelAndView(new JsonView());
		}else {//ajax가 아니라면 error
			mav = new ModelAndView("error");
		}
		
		String msg = null;
		//서블릿에서 전달된 에러 객체를 가져온다.
		Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		// e가 null이 아니고 그 에러 객체의 원인이 AssetException 이거나 이 하위 클래스인 경우
		if(e != null && e.getCause() instanceof AssetException) {
			//msg에 예외 객체의 매세지를 할당
			msg = e.getMessage();
			// 마지막 콜론 이후의 메세지는 잘라냄다.
			msg = msg.substring(msg.lastIndexOf(":")+1);
		}
		//ErrorAttributeOptions :Spring Boot가 에러 페이지를 생성할 때 사용하는 에러 속성에 대한 옵션을 제어하는 데 사용
        ErrorAttributeOptions options = ErrorAttributeOptions
        	    .defaults() 
        	    .including(ErrorAttributeOptions.Include.STACK_TRACE)
        	;
        //위의 코드는 에러 발생 시 기본 설정에 추가로 스택 트레이스 정보를 포함시키는 설정을 생성
        
        // 현재 요청에 대한 에러와 앞서 설정한 options을 가져옴
        Map<String, Object> errorAttributes = getErrorAttributes(request, options);
        log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		// 해당 에러를 log에 출력
        errorAttributes.forEach((attribute, value) -> {			
			log.debug("attribute : {}, value:{}", attribute, value);
		});
        // 에러에 다양한 정보를 modelandview에 추가 나중에 view에서 사용할 수 있다.
		mav.addObject("timestamp", errorAttributes.get("timestamp"));
		mav.addObject("status", errorAttributes.get("status"));
		mav.addObject("error", errorAttributes.get("error"));
		mav.addObject("path", errorAttributes.get("path"));
		mav.addObject("trace", errorAttributes.get("trace"));
		mav.addObject("msg", msg);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
        
		return mav;
		
	}
}
