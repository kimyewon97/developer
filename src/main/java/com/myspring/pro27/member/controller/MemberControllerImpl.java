package com.myspring.pro27.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.myspring.pro27.member.dao.MemberDAO;
import com.myspring.pro27.member.service.MemberService;
import com.myspring.pro27.member.vo.MemberVO;



@Controller("memberController")
@EnableAspectJAutoProxy //aop기능 활성화 
public class MemberControllerImpl   implements MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO ;
	
//	//추가
//	@Autowired
//	private MemberDAO memberDAO ;
	
	@Override
	@RequestMapping(value="/member/listMembers.do" ,method = RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);
//		/String viewName = (String)request.getAttribute("viewName");
		//System.out.println("viewName: " +viewName);
		logger.info("viewName: "+ viewName);
		logger.debug("viewName: "+ viewName);
		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}

	@Override
	@RequestMapping(value="/member/addMember.do" ,method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member,
			                  HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int result = 0;
		result = memberService.addMember(member);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	@Override
	@RequestMapping(value="/member/removeMember.do" ,method = RequestMethod.GET)
	public ModelAndView removeMember(@RequestParam("id") String id, 
			           HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("utf-8");
		memberService.removeMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	


	@RequestMapping(value = { "/member/loginForm.do", "/member/memberForm.do" }, method =  RequestMethod.GET)
//	@RequestMapping(value = "/member/*Form.do", method =  RequestMethod.GET)
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member,
				              RedirectAttributes rAttr,
		                       HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		if(memberVO != null) {
			    HttpSession session = request.getSession();
			    session.setAttribute("member", memberVO);
			    session.setAttribute("isLogOn", true);
			    mav.setViewName("redirect:/member/listMembers.do");
		}else {
			    rAttr.addAttribute("result","loginFailed");
			    mav.setViewName("redirect:/member/loginForm.do");
		}
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/logout.do", method =  RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}	

//	@RequestMapping(value = "/member/*Form.do", method =  RequestMethod.GET)
//	private ModelAndView form(@RequestParam(value= "result", required=false) String result,
//						       HttpServletRequest request, 
//						       HttpServletResponse response) throws Exception {
//		//String viewName = getViewName(request);
//		String viewName = (String)request.getAttribute("viewName");
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("result",result);
//		mav.setViewName(viewName);
//		return mav;
//	}
		//jsp를 새로 만들 때는 타일즈 멤버 설정 파일에 해당 jsp 파일을 등록해줘야함
		//왜냐 타일즈는 공통으로 사용하는 jsp 합쳐서 화면에 그려주기 위해 사용하기 때문에 설정을 해줘야한다.
		//추가
		//수정하기 조회 
		@Override
		@RequestMapping(value = "/member/modMemberForm.do", method =  RequestMethod.GET) // /member/modMemberForm.do으로 넘어오면
		public ModelAndView modMemberForm(@RequestParam(value= "id", required=false) String id, //id로 넘어옴
							       HttpServletRequest request, 
							       HttpServletResponse response) throws Exception {
			String viewName = getViewName(request); //뷰 이름을 가져옴
			
			logger.debug("viewName: "+ viewName);
			ModelAndView mav = new ModelAndView(); //빈 객체 만듦
			
			memberVO = memberService.findMember(id);
			mav.addObject("member", memberVO);
			mav.setViewName(viewName);
			
			return mav;
		}
	
		//회원 수정 후 리스트로 이동
		@Override
		@RequestMapping(value="/member/modMember.do" ,method = RequestMethod.POST) // /member/modMember.do 가 불리면
		public ModelAndView modMember(@ModelAttribute("member") MemberVO member, //회원 정보 수정 창에서 전송된 회원정보를 바로 MemberVO 객체에 설정 
				                  HttpServletRequest request, HttpServletResponse response) throws Exception {
			request.setCharacterEncoding("utf-8");
			
			int result = 0;
			result = memberService.modMember(member); //회원 정보 수정
			ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do"); //수정 후 리스트로 이동
			
			return mav;
		}
		

	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		int begin = 0;
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}

		String viewName = uri.substring(begin, end);
		if (viewName.indexOf(".") != -1) {
			viewName = viewName.substring(0, viewName.lastIndexOf("."));
		}
		if (viewName.lastIndexOf("/") != -1) {
			viewName = viewName.substring(viewName.lastIndexOf("/", 1), viewName.length());
		}
		return viewName;
	}

}