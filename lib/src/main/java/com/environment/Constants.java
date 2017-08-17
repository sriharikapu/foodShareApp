package com.environment;

import com.environment.Executer;

import java.io.File;
import java.util.Scanner;

public final class Constants {
	public static final String path = ".";
	public static final String build_gradle = path + "/app/build.gradle";
	public static final String AndroidManifest = path + "/app/src/main/AndroidManifest.xml";
	public static final String FFConfig = path + "/app/src/main/java/com/fan/framework/config/FFConfig.java";
	public static final String Constant = path + "/app/src/main/java/com/fengnian/smallyellowo/foodie/appconfig/Constants.java";

	public static final class Umeng {
		public static final String key = "57ccd766e0f55ada5500067c";
		public static final String testKey = "582d69a3a40fa375ba00019b";
		public static final String secret = "27b4553d397971a9180d43a7145173be";
		public static final String testSecret  = "353b829355657b0a27ec2cdcbf9338d5";
	}

	public static final class GeTui {
		public static final class Release {
			public static final String PUSH_APPID = "kh8zmnbbig90lrw8apeuj4";
			public static final String PUSH_APPKEY = "EBTpWy6UCYAZkosdVG50h5";
			public static final String PUSH_APPSECRET = "IuWIOrhVFj71Rwqy4raQQ7";
		}

		public static final class Test {
			public static final String PUSH_APPID = "EtvOACOJjq9VGI4rx1Ktd1";
			public static final String PUSH_APPKEY = "aGLWgI5jUg7pCEEogxVHY3";
			public static final String PUSH_APPSECRET = "13uxdrHzUJ8wokJSyfnIK3";
		}
	}

	public static final class Url {
		public static final String release = "int type = ENV_RELEASE";
		public static final String test = "int type = ENV_TEST";
		public static final String develop = "int type = ENV_DEVELOP";
		public static final String emulate = "int type = ENV_EMULATE";
	}

	public static final class Log {
		public static final String develop = "public static final boolean IS_OFFICIAL = false";
		public static final String release = "public static final boolean IS_OFFICIAL = true";
	}


	public static void main(String[] args) {
		main(new Scanner(System.in));
	}

	public static void main(Scanner sc){
		System.out.println("--------------------------------------------------------");
		System.out.println("请请请输入环境对应的编号\r\n1:开发\r\n2:测试\r\n3:仿真\r\n4:正式\r\n其他任意字符配置首发");
		int a = -1;
//		while(!(a > 0 && a< 5)){
			try{
				a=sc.nextInt();
			}catch (Exception e) {
				sc.next();//遇到异常忽略当前行
//				a = -1;
//				System.out.println("请输入正确编号");
			}
//		}
		switch (a) {
			case 1:
				Executer.Develop();
				System.out.println("开发环境修改完毕 ");
				break;
			case 2:
				Executer.Test();
				System.out.println("测试环境修改完毕 ");
				break;
			case 3:
				Executer.Emulate();
				System.out.println("仿真环境修改完毕 ");
				break;
			case 4:
				Executer.Release();
				System.out.println("正式环境修改完毕 ");
				break;

			default:
				Executer.Shoufa(sc);
				break;
		}
//		System.out.println("输入任意字符退出");
//		sc.next();
//		sc.close();
		main(sc);

	}
}
