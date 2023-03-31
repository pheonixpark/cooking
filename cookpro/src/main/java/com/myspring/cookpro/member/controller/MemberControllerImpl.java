package com.myspring.cookpro.member.controller;

import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.cookpro.member.dto.MemberDTO;
import com.myspring.cookpro.member.service.MemberService;

@Controller
public class MemberControllerImpl implements MemberController{
	@Autowired
	private MemberService memberService;

	private int randomNum;

	/* 硫붿씤 �럹�씠吏� */
	@RequestMapping("/")
	public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "main";
	}

	/* Form.jsp �떎�뻾 */
	@RequestMapping("/member/*Form.do")
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");

		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}

	/* �븘�씠�뵒 以묐났 泥댄겕 */
	@ResponseBody
	@RequestMapping(value="/member/check.do", method = RequestMethod.POST)
	public int checkId(@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		int result = memberService.checkById(id);

		return result;
	}

	/* �씠硫붿씪 �씤利앸쾲�샇 �쟾�넚 */
	@RequestMapping(value="/member/mail.do", method=RequestMethod.POST)
	public void sendMail(@RequestParam("email") String email,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Random r = new Random();
		randomNum = r.nextInt(888888) + 111111;

		String msg;
		msg = "�븞�뀞�븯�꽭�슂. �씤利앸쾲�샇�뒗 ";
		msg += randomNum;
		msg += " �엯�땲�떎.";

		memberService.sendMail(email, "[CookPro] �씤利앸쾲�샇", msg);
	}

	/* �씠硫붿씪 �씤利앸쾲�샇 �솗�씤 */
	@ResponseBody
	@RequestMapping(value="/member/auth.do", method=RequestMethod.POST)
	public String checkAuth(@RequestParam("authNo") int authNo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(authNo == randomNum) {
			return "Y";
		} else {
			return "N";
		}
	}

	/* �쉶�썝媛��엯 */
	@RequestMapping(value = "/member/addMember.do", method = RequestMethod.POST)
	public void addMember(@ModelAttribute("member") MemberDTO member,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		int result = memberService.addMember(member);

		out.print("<script>");
		if(result == 1) {
			out.print("alert('�쉶�썝媛��엯�뿉 �꽦怨듯븯���뒿�땲�떎. �솚�쁺�빀�땲�떎!');");
			out.print("location.href='"+request.getContextPath()+"/'");
		} else {
			out.print("alert('�쉶�썝媛��엯�뿉 �떎�뙣�븯���뒿�땲�떎. �떎�떆 �떆�룄�빐二쇱꽭�슂.');");
			out.print("location.href='"+request.getContextPath()+"/member/memberForm.do'");
		}
		out.print("</script>");
		out.close();
	}

	/* 濡쒓렇�씤 */
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	public ModelAndView login(MemberDTO member, RedirectAttributes rAttr, 
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		member = memberService.login(member);

		ModelAndView mav = new ModelAndView();

		if(member != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", member);
			session.setAttribute("isLogOn", true);

			rAttr.addAttribute("msg", "login");

			String action = (String) session.getAttribute("action");
			session.removeAttribute("action");

			if(action != null) {
				mav.setViewName("redirect:"+action);
			} else {
				mav.setViewName("redirect:/");
			}

		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}

		return mav;
	}

	/* 濡쒓렇�븘�썐 */
	@RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(RedirectAttributes rAttr, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession(false);

		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");

		ModelAndView mav = new ModelAndView();

		if(session != null && isLogOn != null) {
			session.invalidate();
			rAttr.addAttribute("result", "logout");
		} else {
			rAttr.addAttribute("result", "notLogin");
		}

		mav.setViewName("redirect:/member/loginForm.do");
		return mav;
	}

	//�쉶�썝�깉�눜
	@RequestMapping(value="withdraw")
	public ResponseEntity<String> withdraw
	(@RequestParam("member_id")String member, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResponseEntity<String> resEnt = null;
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","text/html;charset=utf-8");

		try {
			HttpSession session = request.getSession();

			memberService.removeMember(member);	

			message = "<script>";
			message += "alert('�쉶�썝�깉�눜 �븯���뒿�땲�떎.');";
			message += "location.href='"+request.getContextPath()+"/main';";
			message += "</script>";
			session.invalidate();
			resEnt = new ResponseEntity<String>(message,headers,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			message = "<script>";
			message += "alert('�쉶�썝�깉�눜�뿉 �떎�뙣�븯���뒿�땲�떎.');";
			message += "history.go(-1);";
			message += "</script>";

			resEnt = new ResponseEntity<String>(message,headers,HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}

		return resEnt;
	}
}

