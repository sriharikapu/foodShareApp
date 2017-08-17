package com.fengnian.smallyellowo.foodie.utils;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.UserInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	 public static String md5s(String plainText) throws Exception {
		 String str = new String();
		 try {   
			 MessageDigest md = MessageDigest.getInstance("MD5");   
			 md.update(plainText.getBytes());   
			 byte b[] = md.digest();   
	  
			 int i;   
	  
			 StringBuffer buf = new StringBuffer("");   
			 for (int offset = 0; offset < b.length; offset++) {   
				 i = b[offset];   
				 if (i < 0)   
					 i += 256;   
				 if (i < 16)   
					 buf.append("0");   
				 buf.append(Integer.toHexString(i));   
			 }   
			 str = buf.toString();   
		 }catch (NoSuchAlgorithmException e) {   
			 e.printStackTrace(); 
			 throw e;
		 }  
		 return str;
	 }
}
