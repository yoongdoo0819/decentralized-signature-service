package com.github.fabasset.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabasset.example.chaincode.EnrollUser;
import com.github.fabasset.example.chaincode.function.Custom;
import com.github.fabasset.example.config.ExecutionConfig;
import com.github.fabasset.example.config.NetworkConfig;
import com.github.fabasset.example.model.User;
import com.github.fabasset.example.model.User_Doc;
import com.github.fabasset.example.model.User_Sig;
import com.github.fabasset.example.model.dao.*;
import com.github.fabasset.example.util.RedisEnrollment;
import com.github.fabasset.sdk.chaincode.ChaincodeProxy;
import com.github.fabasset.sdk.chaincode.function.Default;
import com.github.fabasset.sdk.chaincode.function.ERC721;
import com.github.fabasset.sdk.chaincode.function.Extension;
import com.github.fabasset.sdk.chaincode.function.TokenTypeManagement;
import com.github.fabasset.sdk.user.AddressUtils;
import com.github.fabasset.sdk.user.UserContext;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static com.github.fabasset.example.config.NetworkConfig.CHAINCODE_NAME;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Slf4j
@Validated
@Controller
@EnableAutoConfiguration
public class MainController {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Default de;
	@Autowired
	private ERC721 erc721;
	@Autowired
	private Extension extention;
	@Autowired
	private TokenTypeManagement tokenTypeManagement;
	@Autowired
	private Custom custom;
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
	private RedisEnrollment re;



	AddressUtils addressUtils = new AddressUtils();
	String IP = "localhost";

	@GetMapping("/index")
	public String index() {
		log.info("index!");

		return "index";
	}

	public void setOrg0() {
		NetworkConfig.ORG_MSP = "Org0MSP";
		NetworkConfig.ORG = "org0";
		NetworkConfig.CA_ORG_URL = "http://" + IP + ":7054";
		NetworkConfig.ORG_PEER = "peer0.org0.example.com";
		NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":7051";
		NetworkConfig.ADMIN = "admin0";
	}

	public void setOrg1() {
		NetworkConfig.ORG_MSP = "Org1MSP";
		NetworkConfig.ORG = "org1";
		NetworkConfig.CA_ORG_URL = "http://" + IP + ":8054";
		NetworkConfig.ORG_PEER = "peer1.org1.example.com";
		NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":8051";
		NetworkConfig.ADMIN = "admin1";
	}

	public void setOrg2() {
		NetworkConfig.ORG_MSP = "Org2MSP";
		NetworkConfig.ORG = "org2";
		NetworkConfig.CA_ORG_URL = "http://" + IP + ":9054";
		NetworkConfig.ORG_PEER = "peer2.org2.example.com";
		NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":9051";
		NetworkConfig.ADMIN = "admin2";
	}

	public static String createHash(String str) {
		String hashString = "";
		try {
			// MD2, MD4, MD5, SHA-1, SHA-256, SHA-512
			MessageDigest sh = MessageDigest.getInstance("SHA-512");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			hashString = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			hashString = null;
		}
		return hashString;
	}

	public static String merkleRoot(String leaf[], int start, int end) {

		if(start >= end) {
			System.out.println(leaf[start] + " " + createHash(leaf[start]));
			return createHash(leaf[start]);
		}

		int middle = (start+end)/2;
		String left = merkleRoot(leaf, start, middle);
		String right = merkleRoot(leaf, middle+1, end);
		String result = createHash(left + right);

		System.out.println("left + right : " + result);
		return result;
	}

	@GetMapping("/signUpForm")
	public String signUpForm() {
		log.info("signUp form");

		return "signUpForm";
	}

	@PostMapping("/signUp")
	public String signUp(HttpServletRequest req) throws Exception {
		log.info("signUp");
		String userId = req.getParameter("userId");

		EnrollUser newUser = new EnrollUser();
		Enrollment enrollment = null;

		try {

			if(userId.equals("admin0")) {

				try {
					setOrg0();
					enrollment = newUser.enrollAdmin(NetworkConfig.ADMIN, "adminpw");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			else if(userId.equals("admin1")) {

				try {
					setOrg1();
					enrollment = newUser.enrollAdmin(NetworkConfig.ADMIN, "adminpw");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			else if(userId.equals("admin2")) {

				try {
					setOrg2();
					enrollment = newUser.enrollAdmin(NetworkConfig.ADMIN, "adminpw");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			else if(userId.equals("company0")) {
				setOrg0();
				enrollment = newUser.registerUser(userId);
			}

			else if(userId.equals("company1")) {
				setOrg1();
				enrollment = newUser.registerUser(userId);
			}

			else if(userId.equals("company2")) {
				setOrg2();
				enrollment = newUser.registerUser(userId);
			}

			else {
				setOrg0();
				enrollment = newUser.registerUser(userId);
			}

			UserContext userContext = ExecutionConfig.initUserContext(userId, enrollment);
			X509Identity identity = new X509Identity(userContext);
			String addr = addressUtils.getMyAddress(identity);

			// insert user's cert into Redis
			if(!(re.setEnrollment(userId, enrollment)))
				log.info("user register failure");

			// insert user's info into DB
			User user = new User(userId, addr, req.getParameter("userPasswd"), req.getParameter("userEmail"));
			userDao.insert(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}

	@GetMapping("/main")
	public String main(HttpServletRequest req, Model model) {
		log.info("main");

		return "main";
	}

	@ResponseBody
	@PostMapping("/createDigitalContractToken")
	public RedirectView upload(HttpServletRequest req, MultipartHttpServletRequest mre) throws  Exception{

		log.info(req.getSession().getServletContext().getRealPath("/"));
		String userid = req.getParameter("userid");
		String count = req.getParameter("count");
		String[] user = null;
		String signers = "";
		Date today = new Date();

		if(userid.equals("company0"))
			setOrg0();
		else if (userid.equals("company1"))
			setOrg1();
		else if (userid.equals("company2"))
			setOrg2();

		// if signers for document are not only one
		if(!count.equals("")) {

			Map<String, Object> testMap = null;
			user = new String[parseInt(count)];
			for(int i=0; i<user.length; i++) {
				try {
					testMap = userDao.getUserByUserId(req.getParameter("ID"+i));
				} catch (RuntimeException e) {
					if(testMap == null)
						return new RedirectView("main");
				}
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
		String path = "./dSignature-server/workspace/src/main/webapp/";
		String original = "";
		File convFile = null;
		InputStream is = null;

		try {

			/*
			 * getting hash of document
			 */
			final MessageDigest md = MessageDigest.getInstance("SHA-512");

			convFile = new File(mf.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(req.getSession().getServletContext().getRealPath("/") +convFile);	// absolute path needed
			fos.write(mf.getBytes());
			fos.close();

			is = new FileInputStream(req.getSession().getServletContext().getRealPath("/")+convFile);	// absolute path needed
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

			// hash of document
			original = builder.toString();

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

		uploadPath = path + mf.getOriginalFilename();

		// make a one string for 'signers'
		signers += userid;
		if(user != null) {
			for (int i = 0; i < user.length; i++) {

				signers += ",";
				signers += user[i];
			}
		}

		String owner = userid;

		// insert tokenId into DB
		Map<String, Object> testMapForToken = tokenDao.getTokenNum();
		int tokenNum = parseInt(String.valueOf(testMapForToken.get("auto_increment")));
		tokenDao.insert(tokenNum);

		// insert document's info into DB
		docDao.insert(original, mf.getOriginalFilename(), tokenNum, signers);

		// insert key for user and document into DB
		Map<String, Object> testMap = docDao.getDocNum();
		int docNum = parseInt(String.valueOf(testMap.get("auto_increment")));
		docNum--;
		user_docDao.insert(userid, docNum);

		// if signers for document are not only one
		if(user != null) {
			for(int i=0; i<user.length; i++)
				user_docDao.insert(user[i], docNum);
		}

		// create merkleRoot for off-chain data verification
		String merkleLeaf[] = new String[2];
		merkleLeaf[0] = mf.getOriginalFilename();
		merkleLeaf[1] = today.toString();

		String merkleRoot = merkleRoot(merkleLeaf, 0, merkleLeaf.length-1);
		log.info(merkleRoot);

		Enrollment enrollment = re.getEnrollment(userid);
		ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(userid, enrollment);
		extention.setChaincodeProxy(chaincodeProxy);
		extention.setChaincodeName(CHAINCODE_NAME);

		UserContext userContext = ExecutionConfig.initUserContext(userid, enrollment);
		X509Identity identity = new X509Identity(userContext);
		String addr = addressUtils.getMyAddress(identity);

		String docType = "doc";
		ArrayList<String> signer = new ArrayList<String>();
		signer.add(addr);
		if(user != null) {
			String signerAddr = "";
			for (int i = 0; i < user.length; i++) {
				signerAddr = (String)userDao.getUserByUserId(user[i]).get("addr");
				signer.add(signerAddr);
			}
		}

		ArrayList<String> signatures = new ArrayList<String>();
		Map<String, Object> xattr = new HashMap<>();
		xattr.put("hash", original);
		xattr.put("signers", signer);
		xattr.put("signatures", signatures);
		xattr.put("finalized", false);

		Map<String, String> uri = new HashMap<>();
		uri.put("path", "jdbc:log4jdbc:mysql://localhost:3306/hyperledger");
		uri.put("hash", merkleRoot);

		extention.mint(valueOf(tokenNum), docType, xattr, uri);
		return new RedirectView("main");
	}

	@ResponseBody
	@RequestMapping("/createSignatureToken")
	public RedirectView img (HttpServletRequest req, String signer, String strImg) throws Exception {

		log.info(" > " + signer);
		log.info(" > " + strImg);

		if(signer.equals("company0"))
			setOrg0();
		else if (signer.equals("company1"))
			setOrg1();
		else if (signer.equals("company2"))
			setOrg2();

		String folder = req.getServletContext().getRealPath("/"); // uploadpath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		String rstStrImg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String filenm = sdf.format(new Date()).toString() + "_" + signer;

		Date today = new Date();

		BufferedImage image = null;
		byte[] byteImg;

		/*
		 * create sig image
		 */
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

		/*
		 * create image hash
		 */
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
			//partialHash = new byte[hashSum.getDigestLength()];
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

		//String owner = signer;

		// insert tokenId into DB
		Map<String, Object> testMapForToken = tokenDao.getTokenNum();
		int tokenNum = parseInt(String.valueOf(testMapForToken.get("auto_increment")));
		tokenDao.insert(tokenNum);

		// insert sig's info into DB
		sigDao.insert(sigId, filenm, tokenNum, today.toString());
		Map<String, Object> testMap = sigDao.getSigBySigid(sigId);
		int sigNum = (int)testMap.get("sigNum");

		// insert key for user and sig into DB
		user_sigDao.insert(signer, sigNum);

		// create merkleRoot for off-chain data verification
		String merkleLeaf[] = new String[2];
		merkleLeaf[0] = today.toString();
		merkleLeaf[1] = filenm;

		String merkleRoot = merkleRoot(merkleLeaf, 0, merkleLeaf.length-1);
		log.info(merkleRoot);

		String sigType = "sig";
		Map<String, Object> xattr = new HashMap<>();
		xattr.put("hash", sigId);

		Map<String, String> uri = new HashMap<>();
		uri.put("path", "jdbc:log4jdbc:mysql://localhost:3306/hyperledger");
		uri.put("hash", merkleRoot);

		Enrollment enrollment = re.getEnrollment(signer);
		ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(signer, enrollment);
		extention.setChaincodeProxy(chaincodeProxy);
		extention.setChaincodeName(CHAINCODE_NAME);

		extention.mint(valueOf(tokenNum), sigType, xattr, uri);
		return new RedirectView("main");
	}

	@Async
	@ResponseBody
	@RequestMapping("/doSign")
	public RedirectView doSign(HttpServletRequest req, Model model) throws Exception{

		String sigId = req.getParameter("sigId");
		String signer = req.getParameter("signer");
		String tokenId = req.getParameter("tokenId");

		if(signer.equals("company0"))
			setOrg0();
		else if (signer.equals("company1"))
			setOrg1();
		else if (signer.equals("company2"))
			setOrg2();

		List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(signer);
		if(user_sig.size() == 0)
			return new RedirectView("main");

		/*
		 * sign the document
		 */
		Map<String, Object> testMap = sigDao.getSigBySigid(sigId);
		String sigTokenId = valueOf((int)testMap.get("sigtokenid"));

		Enrollment enrollment = re.getEnrollment(signer);
		ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(signer, enrollment);
		custom.setChaincodeProxy(chaincodeProxy);
		custom.setChaincodeName(CHAINCODE_NAME);
		custom.sign(tokenId, sigTokenId);

		return new RedirectView("main");
	}

	@GetMapping("/addUser")
	public String addUser(HttpServletRequest req, Model model) throws Exception{

		return "addUser";
	}

	@GetMapping("/mysign")
	public String mysign(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");

		Map<String, Object> testMap;

		/*
		 * check my signature images
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

		/*
		 * get my document
		 */
		Map<String, Object> docTestMap = docDao.getDocByDocIdAndNum(docId, docNum);
		docPath = (String) docTestMap.get("path");
		model.addAttribute("docPath", docPath);

		/*
		 * get my signature image
		 */
		Map<String, Object> sigTestMap;
		List<User_Sig> user_sig = user_sigDao.listForBeanPropertyRowMapper(userId);

		for (int i = 0; i < user_sig.size(); i++) {
			sigTestMap = sigDao.getSigBySigNum(user_sig.get(i).getSignum());
			sigId = (String) sigTestMap.get("sigid");    // only one sigId
		}

		model.addAttribute("docNum", docNum);
		model.addAttribute("docId", docId);
		model.addAttribute("tokenId", tokenId);
		model.addAttribute("sigId", sigId);

		return "mydoc";
	}

	@ResponseBody
	@RequestMapping("/checkInfo")
	public String checkInfo (HttpServletRequest req, String tokenId) throws Exception {

		log.info(" > " + tokenId);

		String queryResult = null;
		String result = "";
		String signers="";
		String signatures="";
		String owner="";
		String hash="";
		Boolean finalized = null;

		/*
		 * get document info from blockchain
		 */
		queryResult = de.query(tokenId);
		if(queryResult != null) {

			Map<String, Object> map =
					objectMapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

			owner = (String)map.get("owner");
			Map<String, Object> xattr = (HashMap<String, Object>) map.get("xattr");

			List<String> signersList = (ArrayList<String>) xattr.get("signers");
			List<String> signaturesList = (ArrayList<String>) xattr.get("signatures");

			if(signersList != null) {
				for (int i = 0; i < signersList.size(); i++) {
					signers += signersList.get(i);
					if (i + 1 < signersList.size())
						signers += ", ";
				}
			}

			if(signaturesList != null) {
				for (int i = 0; i < signaturesList.size(); i++) {
					signatures += signaturesList.get(i);
					if (i + 1 < signaturesList.size())
						signatures += ", ";
				}
			}
			hash = (String) xattr.get("hash");
			finalized = (Boolean) xattr.get("finalized");
		}

		result += "owner : " + owner + "\n";
		result += "hash : " + hash + "\n";
		result += "signers : " + signers +"\n";
		result += "signatures : " + signatures + "\n";
		result += "finalized : " + finalized.toString();

		log.info(queryResult);
		return result;
	}

	@ResponseBody
	@RequestMapping("/transferFrom")
	public String transferFrom (HttpServletRequest req, String userId, String receiverId, String tokenId) throws Exception {

		log.info("userId > " + userId);
		log.info("receiverId > " + receiverId);
		log.info("tokenId > " + tokenId);

		if(userId.equals("company0"))
			setOrg0();
		else if (userId.equals("company1"))
			setOrg1();
		else if (userId.equals("company2"))
			setOrg2();

		String owner = (String)userDao.getUserByUserId(userId).get("addr");
		String receiver = (String)userDao.getUserByUserId(receiverId).get("addr");

		Enrollment enrollment = re.getEnrollment(userId);
		ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(owner, enrollment);
		erc721.setChaincodeProxy(chaincodeProxy);
		erc721.setChaincodeName(CHAINCODE_NAME);

		if(erc721.transferFrom(owner, receiver, tokenId))
			return "Success";
		else
			return "Failrure";

	}

	@GetMapping("/mydoclist")
	public String mydoclist(HttpServletRequest req, Model model) throws Exception{

		String userId = req.getParameter("userid");
		String docId[];
		String docPath[];
		String docNum[];
		String tokenId[];
		String queryResult = null;
		String sigStatus[];

		if(userId.equals("company0"))
			setOrg0();
		else if (userId.equals("company1"))
			setOrg1();
		else if (userId.equals("company2"))
			setOrg2();

		List<User_Doc> docList = user_docDao.listForBeanPropertyRowMapper(userId);
		docId = new String[docList.size()];
		docNum = new String[docList.size()];
		docPath = new String[docList.size()];
		tokenId = new String[docList.size()];
		sigStatus = new String[docList.size()];

		Enrollment enrollment = re.getEnrollment(userId);
		ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(userId, enrollment);
		de.setChaincodeProxy(chaincodeProxy);
		de.setChaincodeName(CHAINCODE_NAME);

		/*
		 * get my all document list
		 */
		for(int i=0; i<docList.size(); i++) {

			Map<String, Object> testMap = docDao.getDocByDocNum(docList.get(i).getDocnum());
			docId[i] = (String)testMap.get("docid");
			docNum[i] = valueOf(testMap.get("docnum"));
			docPath[i] = (String)testMap.get("path");
			tokenId[i] = valueOf(testMap.get("doctokenid"));
			log.info("tokenId >> " + tokenId[i]);

			queryResult = de.query(tokenId[i]);
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> " + queryResult);
			if(queryResult != null) {
				Map<String, Object> map =
						objectMapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

				Map<String, Object> xattr = (HashMap<String, Object>) map.get("xattr");

				List<String> signersList = (ArrayList<String>) xattr.get("signers");
				List<String> signaturesList = (ArrayList<String>) xattr.get("signatures");

				if(signersList.size() == signaturesList.size())
					sigStatus[i] = "true";
				else
					sigStatus[i] = "false";
			}
		}

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
	public String[] checkStatus (HttpServletRequest req, String tokenId) throws Exception {

		log.info(" > " + tokenId);

		int numOfProperty = 3;
		String queryResult = null;
		String signersResult[] = new String[numOfProperty];
		int sigNum;

		for(int i=0; i<signersResult.length; i++) {
			signersResult[i] = "";
		}
		signersResult[0] = "All participants  ";
		signersResult[1] = "Current signers:  ";

		/*
		 * check current signing status for the document
		 */
		queryResult = de.query(tokenId);
		if(queryResult != null) {
			Map<String, Object> map =
					objectMapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

			Map<String, Object> xattr = (HashMap<String, Object>) map.get("xattr");

			List<String> signersList = (ArrayList<String>) xattr.get("signers");
			List<String> signaturesList = (ArrayList<String>) xattr.get("signatures");

			for(int i=0; i<signersList.size(); i++) {
				signersResult[0] += signersList.get(i);
				if(i+1 < signersList.size())
					signersResult[0] += '\n' ;//"-";
			}

			// all signers have signed or otherwise
			if(signersList.size() == signaturesList.size())
				signersResult[2] = "true";
			else
				signersResult[2] = "false";

			/*
			 * who are current signers?
			 */
			Map<String, Object> sigTestMap;
			Map<String, Object> user_sigTestMap;
			if(signaturesList != null) {
				for (int i = 0; i < signaturesList.size(); i++) {
					sigTestMap = sigDao.getSigBySigTokenId(parseInt((String)signaturesList.get(i)));
					sigNum = (int) sigTestMap.get("signum");
					log.info("tokenId " + signaturesList.get(i) + " , sigNum " + valueOf(sigNum) );

					user_sigTestMap = user_sigDao.getUserid(sigNum);
					signersResult[1] += (String) user_sigTestMap.get("userid");
					if(i+1 < signaturesList.size()) {
						signersResult[1] += " \n ";
					}
				}
			}
		}

		log.info(queryResult);
		return signersResult;
	}

	@GetMapping("/queryDoc")
	public String queryDoc(HttpServletRequest req, Model model) throws Exception{

		String docId = req.getParameter("docid");
		int docNum = parseInt(req.getParameter("docnum"));
		String docPath = "";
		String userId[];
		String sigPathList[];
		String queryResult="";
		String tokenId = req.getParameter("tokenid");

		Map<String, Object> docTestMap = docDao.getDocByDocIdAndNum(docId, docNum);
		List<User_Doc> userList = user_docDao.listForBeanPropertyRowMapperByDocNum((int)docTestMap.get("docnum"));

		sigPathList = new String[userList.size()];
		userId = new String[userList.size()];

		queryResult = de.query(tokenId);

		if(queryResult != null) {
			Map<String, Object> map =
					objectMapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

			Map<String, Object> xattr = (HashMap<String, Object>) map.get("xattr");

			List<String> signersList = (ArrayList<String>) xattr.get("signers");
			List<String> signaturesList = (ArrayList<String>) xattr.get("signatures");

			Map<String, Object> sigTestMap;

			// get signature paths for signers
			if(signaturesList != null) {
				for (int i = 0; i < signaturesList.size(); i++) {
					sigTestMap = sigDao.getSigBySigTokenId(parseInt((String)signaturesList.get(i)));
					sigPathList[i] = (String) sigTestMap.get("path");

					}
			}
		}

		// get document path
		docPath = (String) docTestMap.get("path");

		Document document = new Document(PageSize.A4);
		try {

			// existing pdf
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(req.getSession().getServletContext().getRealPath("/") + "final.pdf")); // absolute path needed
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			// Load existing PDF
			PdfReader reader = new PdfReader(req.getSession().getServletContext().getRealPath("/") +docPath);	// absolute path needed
			for(int i=1; i<=reader.getNumberOfPages(); i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);

			// Copy first page of existing PDF into output PDF
				document.newPage();
				cb.addTemplate(page, 0, 0);
			}

			// Add your new data / text here
			// for example...
			Paragraph title1 = new Paragraph("Signatures");

			Chapter chapter1 = new Chapter(title1, 1);
			chapter1.setNumberDepth(0);

			Section section[] = new Section[sigPathList.length];
			File f;

			/*
			 * insert signatures into PDF document
			 */
			for(int i=0; i<sigPathList.length; i++) {
				section[i] = chapter1.addSection(new Paragraph(userId[i]));
				f = new File(req.getSession().getServletContext().getRealPath("/") +sigPathList[i]);    // absolute path needed

				if(f.isFile()) {
					Image section1Image = Image.getInstance(req.getSession().getServletContext().getRealPath("/") + sigPathList[i]);   // absolute path needed
					section[i].add(section1Image);
				}
			}

			document.add(chapter1);
			document.close();


		} catch (RuntimeException e) {

		}

		log.info(">>>>>>>>>>>>>>>");
		model.addAttribute("finalDocPath", "final.pdf");
		return "finalDoc";
	}

	@ResponseBody
	@RequestMapping("/finalize")
	public String finalize (HttpServletRequest req, String tokenId) throws Exception {

		log.info("tokenId > " + tokenId);

//		if(erc721.finalize(tokenId))
//			return "Success";
//		else
//			return "Failrure";

		return null;
	}
}