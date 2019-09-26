package com.poscoict.posledger.assets.controller;

//import com.itextpdf.layout.Doc;

import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.User_Doc;
import com.poscoict.posledger.assets.model.User_Sig;
import com.poscoict.posledger.assets.org.app.chaincode.invocation.*;
import com.poscoict.posledger.assets.service.RedisService;
import com.poscoict.posledger.assets.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

//import com.poscoict.posledger.assets.org.app.chaincode.invocation.*;

//import com.lowagie.text;
//import com.itextpdf.*;
//import com.itextpdf.pdfa.PdfADocument;
//import com.itextpdf.signatures.

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
	@Autowired
	private TokenDao tokenDao;
	@Autowired
	private RedisService redisService;

	@GetMapping("/redis")
	public String redis(HttpServletRequest req, Model model) {

		registerUser newUser = new registerUser();
		try {
			//User user = new User(req.getParameter("userId"), req.getParameter("userPasswd"));
			//userDao.insert(user);

			String certificate = newUser.registerNewUser(req.getParameter("userId"));
			if(!(redisService.storeUser(req.getParameter("userId"), certificate)))
				log.info("user register failure");

		} catch (Exception e) {
			e.printStackTrace();
		}

		//model.addAttribute("count", redisService.getVisitCount());

		return "redis";
	}

	@GetMapping("/index")
	public String index() {
		log.info("index!");

		return "index";
	}

	@GetMapping("/signUpForm")
	public String signUpForm() {
		log.info("signUp form");

		return "signUpForm";
	}

	@PostMapping("/signUp")
	public String signUp(HttpServletRequest req) {
		log.info("signUp");

		registerUser newUser = new registerUser();
		try {
			User user = new User(req.getParameter("userId"), req.getParameter("userPasswd"));
			userDao.insert(user);

			String certificate = newUser.registerNewUser(req.getParameter("userId"));
			if(!(redisService.storeUser(req.getParameter("userId"), certificate)))
				log.info("user register failure");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
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
		String signers = "";

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
		String path = "/home/yoongdoo0819/dSignature-server/src/main/webapp/";
		//String original = mf.getOriginalFilename();
		String original = "";
		File convFile = null;
		InputStream is = null;

		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-512");

		//	RandomAccessFile file = new RandomAccessFile("/home/yoongdoo0819/dSignature-server/"+mf.getOriginalFilename(), "r");
			log.info(mf.getOriginalFilename());
		/*	byte[] buffer = new byte[1024];
			byte[] partialHash = null;

			long read = 0;
			long offset = file.length();
			int unitsize;
			while (read < offset) {
				unitsize = (int) (((offset - read) >= 1024) ? 1024 : (offset - read));
				file.read(buffer, 0, unitsize);

				md.update(buffer, 0, unitsize);
				read += unitsize;
			}

			file.close();
			partialHash = new byte[md.getDigestLength()];
			partialHash = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < partialHash.length; i++) {
				sb.append(Integer.toString((partialHash[i] & 0xff) + 0x100, 16).substring(1));
			}
			original = sb.toString();

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
			convFile = new File(mf.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+convFile);
			fos.write(mf.getBytes());
			fos.close();
			//mf.transferTo(convFile);
			//mf = mre.getFile("file");
			//mf.transferTo(convFile);

			is = new FileInputStream("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+convFile);
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

		signers += userid;
		if(user != null) {
			for (int i = 0; i < user.length; i++) {

				signers += ",";
				signers += user[i];
			}
		}
		log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + signers);
		String owner = userid;

		Map<String, Object> testMapForToken = tokenDao.getTokenNum();
		int tokenNum = parseInt(String.valueOf(testMapForToken.get("auto_increment")));
		tokenDao.insert(tokenNum);

		docDao.insert(original, mf.getOriginalFilename(), tokenNum, signers);
		//Map<String, Object> testMap = docDao.getDocByDocId(original);
		//int docNum = (int)testMap.get("docNum");
		//user_docDao.insert(userid, docNum);
		Map<String, Object> testMap = docDao.getDocNum();
		int docNum = parseInt(String.valueOf(testMap.get("auto_increment")));
		docNum--;
		user_docDao.insert(userid, docNum);

		if(user != null) {
			for(int i=0; i<user.length; i++)
				user_docDao.insert(user[i], docNum);
		}

		String merkleLeaf[] = new String[4];
		merkleLeaf[0] = original;
		merkleLeaf[1] = mf.getOriginalFilename();
		merkleLeaf[2] = valueOf(tokenNum);
		merkleLeaf[3] = signers;

		String merkleRoot = MerkleTree.merkleRoot(merkleLeaf, 0, merkleLeaf.length-1);
		log.info(merkleRoot);

		mintDocNFT mintNFT = new mintDocNFT();
		String result = mintNFT.mint(tokenNum, owner, original, signers, mf.getOriginalFilename(), merkleRoot);

		//_transfertoken.transferToken(userid);

		return new RedirectView("main"); //null;//"redirect:/main";
	}

	@ResponseBody
	@RequestMapping("/img")
	public RedirectView img (/*@RequestBody String test,*/ HttpServletRequest req, String signer, String strImg) throws Exception {

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

		// image resize
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		int newWidth = 200;
		int newHeight = 50;
		double  widthtRatio = (double)newWidth/(double)imageWidth;
		double heightRatio = (double)newHeight/(double)imageHeight;
		int w = (int)(imageWidth * widthtRatio);
		int h = (int)(imageWidth * heightRatio);

		java.awt.Image resizeImage = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImage.getGraphics();
		g.drawImage(resizeImage, 0, 0, null);
		g.dispose();
		bis.close();

		fullpath = folder + filenm;
		File folderObj = new File(folder);
		if(!folderObj.isDirectory())
			folderObj.mkdir();
		File outputFile = new File(fullpath);
		if(outputFile.exists())
			outputFile.delete();
		ImageIO.write(newImage, "png", outputFile);

		String sigId = "";
		int buff = 16384;
		try {
			RandomAccessFile file = new RandomAccessFile(fullpath, "r");

			MessageDigest hashSum = MessageDigest.getInstance("SHA-256");

			byte[] buffer = new byte[buff];
			byte[] partialHash = null;

			long read = 0;

			// calculate the hash of the hole file for the test
			long offset = file.length();
			int unitsize;
			while (read < offset) {
				unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
				file.read(buffer, 0, unitsize);

				hashSum.update(buffer, 0, unitsize);

				read += unitsize;
			}

			file.close();
			partialHash = new byte[hashSum.getDigestLength()];
			partialHash = hashSum.digest();

			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < partialHash.length ; i++){
				sb.append(Integer.toString((partialHash[i]&0xff) + 0x100, 16).substring(1));
			}
			sigId = sb.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		log.info(sigId);

		String owner = signer;

		Map<String, Object> testMapForToken = tokenDao.getTokenNum();
		int tokenNum = parseInt(String.valueOf(testMapForToken.get("auto_increment")));
		tokenDao.insert(tokenNum);

		sigDao.insert(sigId, filenm, tokenNum);
		Map<String, Object> testMap = sigDao.getSigBySigid(sigId);
		int sigNum = (int)testMap.get("sigNum");

		user_sigDao.insert(signer, sigNum);

		String merkleLeaf[] = new String[3];
		merkleLeaf[0] = sigId;
		merkleLeaf[1] = filenm;
		merkleLeaf[2] = valueOf(tokenNum);

		String merkleRoot = MerkleTree.merkleRoot(merkleLeaf, 0, merkleLeaf.length-1);
		log.info(merkleRoot);

		mintSigNFT mintNFT = new mintSigNFT();
		mintNFT.mint(tokenNum, owner, sigId, filenm, merkleRoot);

		return new RedirectView("main");
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
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/yoongdoo0819/dSignature-server/src/main/webapp/test.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

// Load existing PDF
			PdfReader reader = new PdfReader("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+filenm);
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
			Image section1Image = Image.getInstance("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+signm);
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

	@Async
	@ResponseBody
	@RequestMapping("/doSign")
	public RedirectView doSign(HttpServletRequest req, Model model) throws Exception{

		int docNum = parseInt(String.valueOf(req.getParameter("docNum")));
		String docId = req.getParameter("docId");
		String sigId = req.getParameter("sigId");
		String signer = req.getParameter("signer");
		String tokenId = req.getParameter("tokenId");

		List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(signer);
		if(user_sig.size() == 0)
			return new RedirectView("main");

		Map<String, Object> testMap = sigDao.getSigBySigid(sigId);
		String sigTokenId = valueOf((int)testMap.get("sigtokenid"));

		updateDocNFT updateNFT = new updateDocNFT();
		updateNFT.update(tokenId, "2", sigTokenId);
		//updateNFT.update(docId, signer, tokenId);

		/*
		Map<String, Object> testMap = docDao.getDocByDocNum(docNum);
		String signersArray = (String)testMap.get("signers");
		String signers[] = signersArray.split(",");
		String approved = "";
		log.info("******************************" + signersArray.split(",")[0]);


		for(int i=0; i<signers.length; i++) {
			log.info("++++++++++++++++++++++"+signers[i]);
			if(signer.equals(signers[i]) && i+1 < signers.length) {
				sleep(2000);
				approved = signers[i + 1];
				approveDocNFT approveNFT = new approveDocNFT();
				approveNFT.approve(approved, tokenId);
			}
		}
		*/

		return new RedirectView("main");
	}

	@GetMapping("/addUser")
	public String addUser(HttpServletRequest req, Model model) throws Exception{


		return "addUser";
	}

	@GetMapping("/mysign")
	public String mysign(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String sigId = "";

		Map<String, Object> testMap;// = (user_sigDao.getUserSig(userId));

		/*
		sigId = (String)testMap.get("sigid");
		model.addAttribute("sigId", sigId);
		*/

		List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(userId);
		if(user_sig.size() > 0) {
			log.info(valueOf(user_sig.get(0).getUserid()));
			String pathList[] = new String[user_sig.size()];

			for (int i = 0; i < user_sig.size(); i++) {
				testMap = sigDao.getSigBySigNum(user_sig.get(i).getSignum());
				pathList[i] = (String) testMap.get("path");

			}

			model.addAttribute("path", pathList);
		}

		return "mysign";
	}

	@GetMapping("/mydoc")
	public String mydoc(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String docId = req.getParameter("docid");
		int tokenId = parseInt(req.getParameter("tokenid"));
		int docNum = parseInt(req.getParameter("docnum"));
		String docPath = "";
		String sigId = null;

		// doc
		/*Map<String, Object> testMap = (user_docDao.getUserDoc(userId));

		docId = (String)testMap.get("docid");*/
		//docId += ".pdf";
		//model.addAttribute("docId", docId);

		Map<String, Object> docTestMap = docDao.getDocByDocIdAndNum(docId, docNum);
		docPath = (String) docTestMap.get("path");
		model.addAttribute("docPath", docPath);

		log.info("#######################################" + userId);
		// sig
		Map<String, Object> sigTestMap;// = (user_sigDao.getUserSig(userId));
		List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(userId);
		//log.info(user_sig);
		//if(user_sig.get(0) != null) {

			//log.info(valueOf(user_sig.get(0).getUserid()));
			//sigId = new String[user_sig.size()];

		for (int i = 0; i < user_sig.size(); i++) {
			sigTestMap = sigDao.getSigBySigNum(user_sig.get(i).getSignum());
			sigId = (String) sigTestMap.get("sigid");    // only one sigId
		}
		//}

		//sigId = (String)sigTestMap.get("sigid");
		model.addAttribute("docNum", docNum);
		model.addAttribute("docId", docId);
		model.addAttribute("tokenId", tokenId);
		model.addAttribute("sigId", sigId);

		return "mydoc";
	}

	@ResponseBody
	@RequestMapping("/checkInfo")
	public String checkInfo (/*@RequestBody String test,*/ HttpServletRequest req, String tokenId) throws Exception {

		log.info(" > " + tokenId);
		//String uploadpath="uploadfile\\";

		String queryResult = null;
		String result = "";
		String XAttr;
		String signers="";
		String tokenIds;
		String owner="";
		String hash="";
		//String signersArray[];
		String tokenIdsArray[];
		int sigNum;

		queryNFT querynft = new queryNFT();
		queryResult = querynft.query(tokenId);

		if(queryResult != null) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(queryResult);

			owner = (String)jsonObj.get("owner");
			XAttr = (String)jsonObj.get("xattr");
			JSONObject tempObj = (JSONObject) jsonParser.parse(XAttr);

			//signers = (String) tempObj.get("signers");
			JSONArray signersArray = (JSONArray) tempObj.get("signers");
			if(signersArray != null) {
				for (int i = 0; i < signersArray.size(); i++) {
					signers += signersArray.get(i);
					if (i + 1 < signersArray.size())
						signers += ", ";
				}
			}
			hash = (String) tempObj.get("hash");
			//tokenIds = (String) tempObj.get("signatures");
			//log.info(tokenIds);

			//signersArray = signers.split(",");
			/*
			if(tokenIds.contains(",")) {
				tokenIdsArray = tokenIds.split(",");
				Map<String, Object> sigTestMap;
				Map<String, Object> user_sigTestMap;

				for (int i = 0; i < tokenIdsArray.length; i++) {
					sigTestMap = sigDao.getSigBySigTokenId(parseInt(tokenIdsArray[i]));
					sigNum = (int) sigTestMap.get("signum");

					user_sigTestMap = user_sigDao.getUserid(sigNum);
					signersResult += (String) user_sigTestMap.get("userid");
				}
			} else
				signersResult += tokenIds + " ";
				*/
		}

		result += "owner : " + owner + "\n";
		result += "hash : " + hash + "\n";
		result += "signers : " + signers;

		log.info(queryResult);
		return result;
	}

	@GetMapping("/mydoclist")
	public String mydoclist(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String docId[];
		String docPath[];
		String docNum[];
		String tokenId[];
		String sigId = "";
		String queryResult = null;
		String signersResult = "";
		String XAttr;
		String sigStatus[];
		int sigNum;

		queryNFT querynft = new queryNFT();


		List<User_Doc> docList = user_docDao.listForBeanPropertyRowMapper(userId);
		docId = new String[docList.size()];
		docNum = new String[docList.size()];
		docPath = new String[docList.size()];
		tokenId = new String[docList.size()];
		sigStatus = new String[docList.size()];

		for(int i=0; i<docList.size(); i++) {

			Map<String, Object> testMap = docDao.getDocByDocNum(docList.get(i).getDocnum());
			docId[i] = (String)testMap.get("docid");
			docNum[i] = valueOf(testMap.get("docnum"));
			docPath[i] = (String)testMap.get("path");
			tokenId[i] = valueOf(testMap.get("doctokenid"));

			queryResult = querynft.query(tokenId[i]);
			if(queryResult != null) {
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(queryResult);

				XAttr = (String)jsonObj.get("xattr");
				JSONObject tempObj = (JSONObject) jsonParser.parse(XAttr);

				//signers = (String) tempObj.get("signers");
				//tokenIds = (String) tempObj.get("sigIds");
				JSONArray signersArray = (JSONArray) tempObj.get("signers");
				JSONArray tokenIdsArray = (JSONArray) tempObj.get("sigIds");

				if(signersArray.size() == tokenIdsArray.size())
					sigStatus[i] = "true";
				else
					sigStatus[i] = "false";
			}
		}

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

		model.addAttribute("docIdList", docId);
		model.addAttribute("docNumList", docNum);
		model.addAttribute("docPathList", docPath);
		model.addAttribute("tokenIdList", tokenId);
		model.addAttribute("userId", userId);
		model.addAttribute("sigStatus", sigStatus);

		return "myDocList";
	}

	@ResponseBody
	@RequestMapping("/checkStatus")
	public String[] checkStatus (/*@RequestBody String test,*/ HttpServletRequest req, String tokenId) throws Exception {

		log.info(" > " + tokenId);
		//String uploadpath="uploadfile\\";

		int numOfProperty = 3;
		String queryResult = null;
		String signersResult[] = new String[numOfProperty];
		String XAttr;
		String signers;
		String tokenIds;
		//String signersArray[];
		//String tokenIdsArray[];
		int sigNum;

		queryNFT querynft = new queryNFT();
		queryResult = querynft.query(tokenId);

		for(int i=0; i<signersResult.length; i++) {
			signersResult[i] = "";
		}
		signersResult[0] = "All participants : ";
		signersResult[1] = "Current signers : ";

		if(queryResult != null) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(queryResult);

			XAttr = (String)jsonObj.get("xattr");
			JSONObject tempObj = (JSONObject) jsonParser.parse(XAttr);

			//signers = (String) tempObj.get("signers");
			//tokenIds = (String) tempObj.get("sigIds");
			JSONArray signersArray = (JSONArray) tempObj.get("signers");
			for(int i=0; i<signersArray.size(); i++) {
				signersResult[0] += signersArray.get(i);
				if(i+1 < signersArray.size())
						signersResult[0] += "-";
			}

			JSONArray tokenIdsArray = (JSONArray) tempObj.get("sigIds");

			if(signersArray.size() == tokenIdsArray.size())
				signersResult[2] = "true";
			else
				signersResult[2] = "false";
			//log.info(tokenIdsArray.toJSONString());

			//signersArray = signers.split(",");
			/*
			if(tokenIds.contains(",")) {
				tokenIdsArray = tokenIds.split(",");
			*/
			Map<String, Object> sigTestMap;
			Map<String, Object> user_sigTestMap;
			if(tokenIdsArray != null) {
				for (int i = 0; i < tokenIdsArray.size(); i++) {
					sigTestMap = sigDao.getSigBySigTokenId(parseInt((String)tokenIdsArray.get(i)));
					sigNum = (int) sigTestMap.get("signum");
					log.info("tokenId " + tokenIdsArray.get(i) + " , sigNum " + valueOf(sigNum) );

					user_sigTestMap = user_sigDao.getUserid(sigNum);
					signersResult[1] += (String) user_sigTestMap.get("userid");
					if(i+1 < tokenIdsArray.size()) {
						signersResult[1] += " - ";
					}
				}
			}

		}

		log.info(queryResult);
		return signersResult;
	}

	@GetMapping("/queryDoc")
	public String queryDoc(HttpServletRequest req, Model model) throws Exception{

		//String userId = req.getParameter("userid");
		String docId = req.getParameter("docid");
		int docNum = parseInt(req.getParameter("docnum"));
		String docPath = "";
		int signum;
		String userId[];
		String sigPathList[];
		String queryResult="";
		String tokenId = req.getParameter("tokenid");
		String XAttr = "";
		//model.addAttribute("docList", user_docDao.listForBeanPropertyRowMapper(docId));

		Map<String, Object> docTestMap = docDao.getDocByDocIdAndNum(docId, docNum);
		List<User_Doc> userList = user_docDao.listForBeanPropertyRowMapperByDocNum((int)docTestMap.get("docnum"));

		sigPathList = new String[userList.size()];
		userId = new String[userList.size()];

		queryNFT querynft = new queryNFT();
		queryResult = querynft.query(tokenId);

		if(queryResult != null) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(queryResult);

			XAttr = (String)jsonObj.get("xattr");
			JSONObject tempObj = (JSONObject) jsonParser.parse(XAttr);

			//signers = (String) tempObj.get("signers");
			//tokenIds = (String) tempObj.get("sigIds");
			JSONArray tokenIdsArray = (JSONArray) tempObj.get("sigIds");
			//log.info(tokenIdsArray.toJSONString());

			//signersArray = signers.split(",");
			/*
			if(tokenIds.contains(",")) {
				tokenIdsArray = tokenIds.split(",");
			*/
			Map<String, Object> sigTestMap;
			if(tokenIdsArray != null) {
				for (int i = 0; i < tokenIdsArray.size(); i++) {
					sigTestMap = sigDao.getSigBySigTokenId(parseInt((String)tokenIdsArray.get(i)));
					sigPathList[i] = (String) sigTestMap.get("path");

					}
				}
			}
/*
		for(int i=0; i<userList.size(); i++) {


			List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(userList.get(i).getUserid());
			//log.info(valueOf(user_sig.get(0).getUserid()));
			//sigId = new String[user_sig.size()];
			Map<String, Object> sigTestMap;
			String _sigId = null;
			for(int j=0; j<user_sig.size(); j++) {
				sigTestMap = sigDao.getSigBySigNum(user_sig.get(j).getSignum());
				_sigId = (String) sigTestMap.get("path");    // only one sigId
			}
			userId[i] = userList.get(i).getUserid();
			sigPathList[i] = _sigId;
//			Map<String, Object> sigTestMap = (user_sigDao.getUserSig(userList.get(i).getUserid()));
//			signum = (int)sigTestMap.get("signum");
//			sigId[i] = (String)(sigDao.getSigBySigNum(signum).get("sigid"));
		}
*/
		//Map<String, Object> docTestMap = docDao.getDocByDocId(docId);
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
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/yoongdoo0819/dSignature-server/src/main/webapp/final.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

// Load existing PDF
			PdfReader reader = new PdfReader("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+docPath);
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

			Section section[] = new Section[sigPathList.length];
			File f;
			for(int i=0; i<sigPathList.length; i++) {
				section[i] = chapter1.addSection(new Paragraph(userId[i]));
				f = new File("/home/yoongdoo0819/dSignature-server/src/main/webapp/"+sigPathList[i]);
				if(f.isFile()) {
					Image section1Image = Image.getInstance("/home/yoongdoo0819/dSignature-server/src/main/webapp/" + sigPathList[i]);
					section[i].add(section1Image);
				}
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
			//sigDao.getName(1);
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