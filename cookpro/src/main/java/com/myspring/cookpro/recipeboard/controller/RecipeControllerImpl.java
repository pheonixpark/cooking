package com.myspring.cookpro.recipeboard.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.gson.JsonObject;
import com.myspring.cookpro.member.dto.MemberDTO;
import com.myspring.cookpro.recipeboard.dto.ImageDTO;
import com.myspring.cookpro.recipeboard.dto.RecipeDTO;
import com.myspring.cookpro.recipeboard.service.RecipeService;


@Controller
@Repository
public class RecipeControllerImpl implements RecipeController{

	private static final String CURR_IMAGE_REPO_PATH = "C:\\Users\\tmdwn\\git\\Cooking-Recipe\\cookpro\\recipe_imageFile";

	@Autowired
	RecipeService recipeService;

	@Override
	@RequestMapping("/recipeboard/recipeList.do")
	public ModelAndView listRecipe(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName= request.getParameter("viewName");
		List<RecipeDTO> recipesList = recipeService.recipesList();

		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value="/recipeboard/*Form.do", method=RequestMethod.GET)
	public ModelAndView r_form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String viewName= request.getParameter("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value="/recipeboard/recipeView.do", method=RequestMethod.GET)
	public ModelAndView viewRecipe(int recipeNo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName= (String)request.getAttribute("viewName");
		Map recipeMap = recipeService.viewRecipe(recipeNo);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("recipeeMap", recipeMap);
		return mav;
	}

	@Override
	@RequestMapping(value="/recipeboard/addRecipe.do", method=RequestMethod.POST)
	public ResponseEntity addNewRecipe(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> recipeMap = new HashMap<String, Object>();
		Enumeration<String> enu = multipartRequest.getParameterNames();

		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			recipeMap.put(name, value);
		}

		//		List<String> fileList = upload(multipartRequest);
		//		List<ImageDTO> imageFileList= new ArrayList<ImageDTO>();
		//		if(fileList != null && fileList.size() != 0) {
		//			for(String fileName : fileList) {
		//				ImageDTO image = new ImageDTO();
		//				image.setImageFileName(fileName);
		//				imageFileList.add(image);
		//			}
		//			recipeMap.put("recipe_imageList", imageFileList);
		//		}

		HttpSession session = multipartRequest.getSession();
		MemberDTO member = (MemberDTO)session.getAttribute("member");



		String id = member.getId();
		recipeMap.put("parentNo", 0);
		recipeMap.put("id",id);

		String data = multipartRequest.getParameter("recipe_detail");

		recipeMap.put("recipe_detail", data);
		System.out.println(recipeMap.get("recipe_detail"));
		System.out.println(recipeMap.get("recipe_image"));
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type", "text/html;charset=utf-8");

		try {

			int recipeNo = recipeService.addNewRecipe(recipeMap);
			//			if(imageFileList != null && imageFileList.size() != 0) {
			//				for(ImageDTO imageDTO : imageFileList) {
			//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\temp\\"+imageDTO.getImageFileName());
			//					File destDir = new File(CURR_IMAGE_REPO_PATH+"\\"+recipeNo);
			//					FileUtils.moveFileToDirectory(srcFile, destDir, true);
			//				}
			//			}
			message = "<script>";
			message += "alert('레시피 추가!.');";
			message += "location.href='" + multipartRequest.getContextPath()
			+"/recipeboard/recipeList.do';";
			message += "</script>";
			resEnt = new ResponseEntity(message, responseHeader, HttpStatus.CREATED);

		} catch (Exception e) {
			// TODO: handle exception
			//			if(imageFileList != null && imageFileList.size() != 0) {
			//				for(ImageDTO imageDTO : imageFileList) {
			//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\temp\\"
			//							+imageDTO.getImageFileName());
			//					srcFile.delete();
			//				}
			//			}
			message = "<script>";
			message += "alert('오류가 발생했습니다. 다시 시도해 주세요.');";
			message += "location.href='" + multipartRequest.getContextPath()
			+"/recipeboard/recipeForm.do';";
			message += "</script>";
			resEnt = new ResponseEntity(message, responseHeader, HttpStatus.CREATED);
			e.printStackTrace();
			e.printStackTrace();
		}
		return resEnt;
	}





	@Override
	@RequestMapping(value="/recipeboard/removeRecipe", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity removceRecipe(int recipe_no, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Contetn-Type", "text/html; charset=utf-8");
		try {

			File destDir = new File(CURR_IMAGE_REPO_PATH + "\\" + recipe_no);
			FileUtils.deleteDirectory(destDir);

			message = "<script>";
			message += "alert('삭제가 완료 되었습니다.');";
			message += "location.href='"+request.getContextPath()+"/recipeboard/reicpeList.do';";
			message += "</script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			message = "<script>";
			message += "alert('삭제에 실패 하었습니다. 다시 시도해 주세요.');";
			message += "location.href='"+request.getContextPath()+"/reciepboard/reciepForm.do';";
			message += "</script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}

		return resEnt;
	}

	//	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
	//		List<String> fileList = new ArrayList<String>();
	//		Map<String, String> recipeMap = new HashMap<String, String>();
	//		Iterator<String> fileNames = multipartRequest.getFileNames();
	//		
	//		while(fileNames.hasNext()) {
	//			String fileName = fileNames.next();
	//			MultipartFile mFile = multipartRequest.getFile(fileName);
	//			String originalFileName = mFile.getOriginalFilename();
	//			fileList.add(originalFileName);
	//			File file = new File(CURR_IMAGE_REPO_PATH+ "\\" + fileName);
	//			if(mFile.getSize()!=0) {
	//				if(!file.exists()) {
	//					if(file.getParentFile().mkdirs()) {
	//						file.createNewFile();
	//					}
	//				}
	//				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH+"\\temp\\"+originalFileName));
	//			}
	//		}
	//		
	//		return fileList;
	//			
	//	}

	@Override
	public ResponseEntity modRecipe(MultipartHttpServletRequest multiRequest, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping(value="/recipeboard/imageUpload.do")
	public void imageUpload(MultipartHttpServletRequest multipartRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonObject jsonObject = new JsonObject();
		PrintWriter printWriter = null;
		OutputStream out = null;
		MultipartFile file = multipartRequest.getFile("upload");

		if(file != null) {
			if(file.getSize() >0 && !StringUtils.isEmpty(file.getName())) {
				if(file.getContentType().toLowerCase().startsWith("image/")) {
					try{

						String fileName = file.getOriginalFilename();
						byte[] bytes = file.getBytes();

						String uploadPath = "C:\\Users\\tmdwn\\git\\Cooking-Recipe\\cookpro\\src\\main\\webapp\\resources\\image\\upload"; //저장경로
						System.out.println("uploadPath:"+uploadPath);
						

						String fileName2 = UUID.randomUUID().toString();
						uploadPath = uploadPath + "/" + fileName2 + fileName;

						out = new FileOutputStream(new File(uploadPath));
						out.write(bytes);

						printWriter = response.getWriter();
						String fileUrl = request.getContextPath() + "/resources/image/upload/" +fileName2 +fileName; //url경로
						System.out.println("fileUrl :" + fileUrl);
						JsonObject json = new JsonObject();
						json.addProperty("uploaded", 1);
						json.addProperty("fileName", fileName);
						json.addProperty("url", fileUrl);
						printWriter.print(json);
						System.out.println(json);

					}catch(IOException e){
						e.printStackTrace();
					} finally {
						if (out != null) {
							out.close();
						}
						if (printWriter != null) {
							printWriter.close();
						}
					}
				}


			}

		}

	}

//	//@Override
//	@RequestMapping("/recipeboard/imageUpload.do")
//	public void imageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload)
//	//MultipartFile 타입은 ckedit에서 upload란 이름으로 저장하게 된다
//			throws Exception {
//
//		// 한글깨짐을 방지하기위해 문자셋 설정
//		response.setCharacterEncoding("utf-8");
//
//		// 마찬가지로 파라미터로 전달되는 response 객체의 한글 설정
//		response.setContentType("text/html; charset=utf-8");
//
//		// 업로드한 파일 이름
//		String fileName = upload.getOriginalFilename();
//
//		// 파일을 바이트 배열로 변환
//		byte[] bytes = upload.getBytes();
//
//		// 이미지를 업로드할 디렉토리(배포 디렉토리로 설정)
//		String uploadPath = "C:\\Users\\tmdwn\\git\\Cooking-Recipe\\cookpro\\recipe_imageFile";
//
//
//		OutputStream out = new FileOutputStream(new File(uploadPath + fileName));
//
//		// 서버로 업로드
//		// write메소드의 매개값으로 파일의 총 바이트를 매개값으로 준다.
//		// 지정된 바이트를 출력 스트립에 쓴다 (출력하기 위해서)
//		out.write(bytes);
//
//		// 클라이언트에 결과 표시
//		String callback = request.getParameter("CKEditorFuncNum");
//
//		// 서버=>클라이언트로 텍스트 전송(자바스크립트 실행)
//		PrintWriter printWriter = response.getWriter();
//		String fileUrl = request.getContextPath() + "/images/" + fileName;
//		printWriter.println("<script>window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + fileUrl
//				+ "','이미지가 업로드되었습니다.')" + "</script>");
//		printWriter.flush();
//	}
}
