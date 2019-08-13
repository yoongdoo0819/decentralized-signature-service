package com.poscoict.posledger.assets.controller;

//import com.itextpdf.layout.Doc;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.User_Doc;
import com.poscoict.posledger.assets.org.app.chaincode.invocation.queryToken;
import com.poscoict.posledger.assets.org.app.chaincode.invocation.transferToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
//import com.lowagie.text;
//import com.itextpdf.*;
//import com.itextpdf.pdfa.PdfADocument;
//import com.itextpdf.signatures.

import com.poscoict.posledger.assets.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Slf4j
@Validated
@Controller
@EnableAutoConfiguration
public class MainController {

	public static JdbcTemplate jdbcTemplate;

	@Autowired
	private TestDao testDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SigDao sigDao;
	@Autowired
	private DocDao docDao;
	@Autowired
	private User_sigDao user_sigDao;
	@Autowired
	private User_docDao user_docDao;

	@GetMapping("/index")
	public String index() {
		log.info("index!");

		return "index";
	}

	@PostMapping("/signup")
	public String signUp(HttpServletRequest req) {
		log.info("signUp");

		User user = new User(req.getParameter("userId"), req.getParameter("userPasswd"));
		userDao.insert(user);

		return "signup";
	}

	@ResponseBody
	@RequestMapping("/signupInfo")
	public String signUpInfo(HttpServletRequest req) {
		log.info("signUp");

		String id = req.getParameter("userId");
		String passwd = req.getParameter("userPasswd");

		User user = new User(id, passwd);
		userDao.insert(user);


		return "redirect:/assets/index"; //new ModelAndView( "redirect:/index.jsp");
	}

	@GetMapping("/_login")
	public String _login() {
		log.info("login!");

		return "_login";
	}

	/*
	@ResponseBody
	@RequestMapping("/login")
	public String login(HttpServletRequest req) {
		log.info("login!");

		String userId = req.getParameter("userId");
		String passwd = req.getParameter("passwd");

		Map<String, Object> usermap = userDao.getUser(userId, passwd);

		if(userId.equals(usermap.get("id")) && passwd.equals(usermap.get("passwd")))
			return "redirect:/main";
		else
			return "redirect:/index";
	}
*/

	//@ResponseBody
	//@RequestMapping("/main")
	@GetMapping("/main")
	public String main(HttpServletRequest req, Model model) {
		log.info("main");

		return "main";
	}

	@GetMapping("/query")
	public String query() throws Exception {

		queryToken queryToken = new queryToken();
		String result = queryToken.query("token1");
		return "index";
	}
	/**
	 * Welcome 화면 
	 */
	@GetMapping("/welcome")
	public String welcome(Model model) {
		model.addAttribute("now", DateUtil.formatDate(DateUtil.getDateObject(), "yyyy.MM.dd HH:mm:ss"));
		return "welcome";
	}

	@ResponseBody
	@PostMapping("/upload")
	public RedirectView upload(HttpServletRequest req, MultipartHttpServletRequest mre) throws IllegalStateException, IOException, Exception{

		transferToken _transfertoken = new transferToken();
		String userid = req.getParameter("userid");
		String count = req.getParameter("count");
		String[] user = null;

		if(!count.equals("")) {
			user = new String[parseInt(count)];

			for(int i=0; i<user.length; i++) {
				user[i] = (req.getParameter("ID"+i));
				log.info(user[i]);
			}
		}

		log.info(userid);

		Document document = new Document(PageSize.A4);
		MultipartFile mf = mre.getFile("file");
		MultipartFile mf2 = mre.getFile("file");

		if(mf.getSize() != 0)
			log.info("failure");

		String uploadPath = "";
		String path = "/home/yoongdoo0819/assets/src/main/webapp/";
		//String original = mf.getOriginalFilename();
		String original = "";
		File convFile = null;
		InputStream is = null;

		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-512");

			convFile = new File(mf.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream("/home/yoongdoo0819/assets/src/main/webapp/"+convFile);
			fos.write(mf.getBytes());
			fos.close();
			//mf.transferTo(convFile);
			//mf = mre.getFile("file");
			//mf.transferTo(convFile);
			is = new FileInputStream(convFile);
			byte[] buffer = new byte[1024];
			int readBytes = 0;

			while ((readBytes = is.read(buffer)) > -1) {
				md.update(buffer, 0, readBytes);
			}

			StringBuilder builder = new StringBuilder();
			byte[] digest = md.digest();
			for(byte b : digest) {
				builder.append(Integer.toHexString(0xff & b));
			}
			original = builder.toString();
			//File destinationFile = new File(path + original);
			//destinationFile.getParentFile().mkdirs();
			//mf2.transferTo(destinationFile);


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		log.info(original);

		uploadPath = path + mf.getOriginalFilename();//+ original;
		log.info(uploadPath);
		//convFile = new File("/home/yoongdoo0819/assets/src/main/webapp/" + mf.getOriginalFilename());
		//convFile.createNewFile();
		//FileOutputStream fos = new FileOutputStream("/home/yoongdoo0819/assets/src/main/webapp/"+original+".pdf");

		/*
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(uploadPath+".pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			// Load existing PDF
			PdfReader reader = new PdfReader("/home/yoongdoo0819/assets/src/main/webapp/"+mf.getOriginalFilename());//convFile.getName());
			for(int i=1; i<=reader.getNumberOfPages(); i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);

				document.newPage();
				cb.addTemplate(page, 0, 0);
			}

			document.close();

		} catch (RuntimeException e) {
				e.printStackTrace();
		}
		*/


/*
		try {
			//File convFile = new File(uploadPath);
			mf.transferTo(new File(mf.getOriginalFilename()));
			//mf.transferTo(uploadPath);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
*/

		docDao.insert(original, mf.getOriginalFilename());
		user_docDao.insert(userid, original);

		if(user != null) {
			for(int i=0; i<user.length; i++)
				user_docDao.insert(user[i], original);
		}

		//_transfertoken.transferToken(userid);

		return new RedirectView("main"); //null;//"redirect:/main";
	}

	@ResponseBody
	@RequestMapping("/img")
	public String img (/*@RequestBody String test,*/ HttpServletRequest req, String signer, String strImg) throws Exception {

		log.info(" > " + signer);
		log.info(" > " + strImg);
		//String uploadpath="uploadfile\\";

		String folder = req.getServletContext().getRealPath("/");// + uploadpath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		String rstStrImg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String filenm = sdf.format(new Date()).toString() + "_" + signer;//"_testimg2.png";

		BufferedImage image = null;
		byte[] byteImg;

		BASE64Decoder decoder = new BASE64Decoder();
		byteImg = decoder.decodeBuffer(rstStrImg);
		ByteArrayInputStream bis = new ByteArrayInputStream(byteImg);
		image = ImageIO.read(bis);
		bis.close();

		fullpath = folder + filenm;
		File folderObj = new File(folder);
		if(!folderObj.isDirectory())
			folderObj.mkdir();
		File outputFile = new File(fullpath);
		if(outputFile.exists())
			outputFile.delete();
		ImageIO.write(image, "png", outputFile);

		sigDao.insert(filenm, fullpath);
		user_sigDao.insert(signer, filenm);
		return "index";
	}

	@ResponseBody
	@RequestMapping("/itext")
	public String itext(/*@RequestBody String test,*/ HttpServletRequest req, HttpServletResponse resp) throws Exception{
		//Doc document = new Doc(PageSize.A4, 50, 50, 50, 50);

		String signer = req.getParameter("signer");
		String filenm = req.getParameter("docPath");
		String signm = req.getParameter("sigId");

		Document document = new Document(PageSize.A4);
		try {
			/* new pdf creation
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./test.pdf"));//response.getOutputStream());
			document.open();
			document.add(new Paragraph("Image"));
			Image jpg = Image.getInstance("CbnAc0Ab");
			document.add(jpg);

			document.close();

			*/
			/*
			PdfReader readerOriginalDoc = new PdfReader("./sample.pdf");
			PdfStamper stamper = new PdfStamper(
					readerOriginalDoc, new FileOutputStream("./test.pdf"));
			PdfContentByte content = stamper.getOverContent(readerOriginalDoc.getNumberOfPages());
			Image image = Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190704_124457_yoongdoo");
			image.scaleAbsolute(300, 150);
			image.setAbsolutePosition(300, 100);
			image.setAnnotation(new Annotation(0, 0, 0, 0, 3));

			content.addImage(image);

			stamper.close();
			*/

			// existing pdf
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/yoongdoo0819/assets/src/main/webapp/test.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

// Load existing PDF
			PdfReader reader = new PdfReader("/home/yoongdoo0819/assets/src/main/webapp/"+filenm);
			for(int i=1; i<=reader.getNumberOfPages(); i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);

// Copy first page of existing PDF into output PDF
				document.newPage();
				cb.addTemplate(page, 0, 0);
				//Image image = Image.getInstance("CbnAc0Ab");///home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo");
				//Rectangle rect = reader.getPageSize(1);

				//cb.addImage(image, image.getScaledWidth(), 0, 0, image.getScaledHeight(), 0, rect.getHeight() - image.getScaledHeight());
			}

// Add your new data / text here
// for example...
			Paragraph title1 = new Paragraph("Signatures");

			Chapter chapter1 = new Chapter(title1, 1);
			//ChapterAutoNumber chapter = document.getChapter();
			chapter1.setNumberDepth(0);

			//Section section = new Section(new Paragraph("signer"));

			Section section1 = chapter1.addSection(new Paragraph("signer"));
			Image section1Image = Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/"+signm);
			section1.add(section1Image);
			//document.add(new Paragraph("my timestamp"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));

/*
			Section section2 = chapter1.addSection(new Paragraph("signer2"));
			Image section1Image2 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);

			Section section3 = chapter1.addSection(new Paragraph("signer3"));
			Image section1Image3 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);

			Section section4 = chapter1.addSection(new Paragraph("signer4"));
			Image section1Image4 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);
*/
			document.add(chapter1);
			document.close();


/*
			Image image = Image.getInstance("test.jpg");
			PdfReader reader = new PdfReader("sample_watermark.pdf");
			Rectangle rect = reader.getPageSize(1);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("sample_watermark2.pdf"));

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PdfContentByte cb = stamper.getOverContent(i);
				cb.addImage(image, image.getScaledWidth(), 0, 0, image.getScaledHeight(),
						0, rect.getHeight() - image.getScaledHeight());
			}

			stamper.close();
*/
		} catch (RuntimeException e) {

		}
		return "index";
	}

	@GetMapping("/addUser")
	public String addUser(HttpServletRequest req, Model model) throws Exception{


		return "addUser";
	}

	@GetMapping("/mysign")
	public String mysign(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String sigId = "";

		//user_sigDao.getName();

		Map<String, Object> testMap = (user_sigDao.getUserSig(userId));
		//User user = new User();

		sigId = (String)testMap.get("sigid");
		model.addAttribute("sigId", sigId);
		return "mysign";
	}

	@GetMapping("/mydoc")
	public String mydoc(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String docId = req.getParameter("docid");
		String docPath = "";
		String sigId = "";

		// doc
		/*Map<String, Object> testMap = (user_docDao.getUserDoc(userId));

		docId = (String)testMap.get("docid");*/
		//docId += ".pdf";
		//model.addAttribute("docId", docId);

		Map<String, Object> docTestMap = docDao.getDoc(docId);
		docPath = (String) docTestMap.get("path");
		model.addAttribute("docPath", docPath);

		log.info("#######################################" + userId);
		// sig
		Map<String, Object> sigTestMap = (user_sigDao.getUserSig(userId));

		sigId = (String)sigTestMap.get("sigid");
		model.addAttribute("sigId", sigId);

		return "mydoc";
	}

	@GetMapping("/mydoclist")
	public String mydoclist(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String docId = "";
		String sigId = "";

		model.addAttribute("docList", user_docDao.listForBeanPropertyRowMapper(userId));
		//model.addAttribute("docList", docList);
		//log.info(docList.get(0).toString());
		/*
		// doc
		Map<String, Object> testMap = (user_docDao.getUserDoc("yoongdoo"));

		log.info(valueOf(testMap.size()));


		for(int i=0; i<testMap.size(); i++) {
			//docId = (String)testMap.get("docid");
			log.info((String)testMap.get("userid"));
			log.info((String)testMap.get("docid"));
			//docId += ".pdf";
			//model.addAttribute("docId", docId);
		}*/

		// sig
		/*
		Map<String, Object> sigTestMap = (user_sigDao.getUserSig(userId));

		sigId = (String)sigTestMap.get("sigid");
		model.addAttribute("sigId", sigId);
*/
		return "myDocList";
	}

	@GetMapping("/queryDoc")
	public String queryDoc(HttpServletRequest req, Model model) throws Exception{

		//String userId = req.getParameter("userid");
		String docId = req.getParameter("docid");
		String docPath = "";
		String sigId[];
		//model.addAttribute("docList", user_docDao.listForBeanPropertyRowMapper(docId));

		List<User_Doc> userList = user_docDao.listForBeanPropertyRowMapperByDoc(docId);
		sigId = new String[userList.size()];

		for(int i=0; i<userList.size(); i++) {

			Map<String, Object> testMap = (user_sigDao.getUserSig(userList.get(i).getUserid()));
			sigId[i] = (String)testMap.get("sigid");

		}

		Map<String, Object> docTestMap = docDao.getDoc(docId);
		docPath = (String) docTestMap.get("path");

		Document document = new Document(PageSize.A4);
		try {
			/* new pdf creation
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./test.pdf"));//response.getOutputStream());
			document.open();
			document.add(new Paragraph("Image"));
			Image jpg = Image.getInstance("CbnAc0Ab");
			document.add(jpg);

			document.close();

			*/
			/*
			PdfReader readerOriginalDoc = new PdfReader("./sample.pdf");
			PdfStamper stamper = new PdfStamper(
					readerOriginalDoc, new FileOutputStream("./test.pdf"));
			PdfContentByte content = stamper.getOverContent(readerOriginalDoc.getNumberOfPages());
			Image image = Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190704_124457_yoongdoo");
			image.scaleAbsolute(300, 150);
			image.setAbsolutePosition(300, 100);
			image.setAnnotation(new Annotation(0, 0, 0, 0, 3));

			content.addImage(image);

			stamper.close();
			*/

			// existing pdf
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/yoongdoo0819/assets/src/main/webapp/final.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

// Load existing PDF
			PdfReader reader = new PdfReader("/home/yoongdoo0819/assets/src/main/webapp/"+docPath);
			for(int i=1; i<=reader.getNumberOfPages(); i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);

// Copy first page of existing PDF into output PDF
				document.newPage();
				cb.addTemplate(page, 0, 0);
				//Image image = Image.getInstance("CbnAc0Ab");///home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo");
				//Rectangle rect = reader.getPageSize(1);

				//cb.addImage(image, image.getScaledWidth(), 0, 0, image.getScaledHeight(), 0, rect.getHeight() - image.getScaledHeight());
			}

// Add your new data / text here
// for example...
			Paragraph title1 = new Paragraph("Signatures");

			Chapter chapter1 = new Chapter(title1, 1);
			//ChapterAutoNumber chapter = document.getChapter();
			chapter1.setNumberDepth(0);

			//Section section = new Section(new Paragraph("signer"));

			Section section[] = new Section[sigId.length];
			for(int i=0; i<sigId.length; i++) {
				section[i] = chapter1.addSection(new Paragraph("signer"+valueOf(i+1)));
				Image section1Image = Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/"+sigId[i]);
				section[i].add(section1Image);
			}

			//document.add(new Paragraph("my timestamp"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));
			//document.add(Image.getInstance("/home/yoongdoo0819/assets/src/main/webapp/20190703_051729yoongdoo"));

/*
			Section section2 = chapter1.addSection(new Paragraph("signer2"));
			Image section1Image2 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);

			Section section3 = chapter1.addSection(new Paragraph("signer3"));
			Image section1Image3 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);

			Section section4 = chapter1.addSection(new Paragraph("signer4"));
			Image section1Image4 = Image.getInstance("CbnAc0Ab");
			section2.add(section1Image);
*/
			document.add(chapter1);
			document.close();


/*
			Image image = Image.getInstance("test.jpg");
			PdfReader reader = new PdfReader("sample_watermark.pdf");
			Rectangle rect = reader.getPageSize(1);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("sample_watermark2.pdf"));

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PdfContentByte cb = stamper.getOverContent(i);
				cb.addImage(image, image.getScaledWidth(), 0, 0, image.getScaledHeight(),
						0, rect.getHeight() - image.getScaledHeight());
			}

			stamper.close();
*/
		} catch (RuntimeException e) {

		}

		model.addAttribute("finalDocPath", "final.pdf");
		return "finalDoc";
	}

	//@Service
	@RequestMapping("/test")
	public String test(JdbcTemplate template) {


		//@Autowired
		//JdbcTemplate jdbcTemplate;// = new JdbcTemplate(jdbc);
		//TestDao testDao = new TestDao();
		//JdbcTemplate jdbctemplate1 = template;
		try {
			//Collections.singletonMap("test", testDao.getName(1));
			testDao.getName(1);
			userDao.listForBeanPropertyRowMapper();
			sigDao.getName(1);
			//jdbcTemplate.queryForMap("select name from user where id = ?", "test01");
			//return (ArrayList<userDto>) jdbcTemplate.query("select * from user", new BeanPropertyRowMapper(User.class));
		} catch (Exception e) {
			e.printStackTrace();
		}

//		public Map<String, Map<String, Object>> getTest() {
//
//			try {
//				testDao.getName(1);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
		return "/index";

	}
}