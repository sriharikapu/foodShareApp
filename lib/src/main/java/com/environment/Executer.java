package com.environment;

public class Executer {
	public static void Develop() {
		// 更改友盟key
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.key, Constants.Umeng.testKey);
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.secret, Constants.Umeng.testSecret);
		// 更改个推key
		FileModify.replace(Constants.AndroidManifest, Constants.GeTui.Release.PUSH_APPID, Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPID, Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPKEY,Constants.GeTui.Test.PUSH_APPKEY);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPSECRET,Constants.GeTui.Test.PUSH_APPSECRET);

		// 配置地址
		// FileModify.replace(Constants.build_gradle, Constants.Url.develop,
		// Constants.Url.develop);
		FileModify.replace(Constants.Constant, Constants.Url.emulate, Constants.Url.develop);
		FileModify.replace(Constants.Constant, Constants.Url.release, Constants.Url.develop);
		FileModify.replace(Constants.Constant, Constants.Url.test, Constants.Url.develop);

		// 确定版本号
//		FileModify.replace(Constants.build_gradle, Constants.Version.versionCodeOld, Constants.Version.versionCode);

		// 配置是否打日志
		FileModify.replace(Constants.FFConfig, Constants.Log.release, Constants.Log.develop);
	}

	public static void Emulate() {
		// 更改友盟key
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.key, Constants.Umeng.testKey);
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.secret, Constants.Umeng.testSecret);
		// 更改个推key
		FileModify.replace(Constants.AndroidManifest, Constants.GeTui.Release.PUSH_APPID,Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPID, Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPKEY,Constants.GeTui.Test.PUSH_APPKEY);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPSECRET,Constants.GeTui.Test.PUSH_APPSECRET);

		// 配置地址
		FileModify.replace(Constants.Constant, Constants.Url.develop, Constants.Url.emulate);
		FileModify.replace(Constants.Constant, Constants.Url.emulate, Constants.Url.emulate);
		FileModify.replace(Constants.Constant, Constants.Url.release, Constants.Url.emulate);
		FileModify.replace(Constants.Constant, Constants.Url.test, Constants.Url.emulate);
		// FileModify.replace(Constants.build_gradle, Constants.Url.test,
		// Constants.Url.test);

		// 确定版本号
//		FileModify.replace(Constants.build_gradle, Constants.Version.versionCodeOld, Constants.Version.versionCode);

		// 配置是否打日志
		FileModify.replace(Constants.FFConfig, Constants.Log.release, Constants.Log.develop);
	}

	public static void Release() {
		// 更改友盟key
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.testKey, Constants.Umeng.key);
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.testSecret, Constants.Umeng.secret);
		// 更改个推key
		FileModify.replace(Constants.AndroidManifest, Constants.GeTui.Test.PUSH_APPID, Constants.GeTui.Release.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Test.PUSH_APPID, Constants.GeTui.Release.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Test.PUSH_APPKEY, Constants.GeTui.Release.PUSH_APPKEY);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Test.PUSH_APPSECRET, Constants.GeTui.Release.PUSH_APPSECRET);

		// 配置地址
		FileModify.replace(Constants.Constant, Constants.Url.develop, Constants.Url.release);
		FileModify.replace(Constants.Constant, Constants.Url.emulate, Constants.Url.release);
		// FileModify.replace(Constants.Constant, Constants.Url.release,
		// Constants.Url.release);
		FileModify.replace(Constants.Constant, Constants.Url.test, Constants.Url.release);

		// 确定版本号
//		FileModify.replace(Constants.build_gradle, Constants.Version.versionCodeOld, Constants.Version.versionCode);

		// 配置是否打日志
		FileModify.replace(Constants.FFConfig, Constants.Log.develop, Constants.Log.release);
	}

	public static void Test() {
		// 更改友盟key
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.key, Constants.Umeng.testKey);
		FileModify.replace(Constants.AndroidManifest, Constants.Umeng.secret, Constants.Umeng.testSecret);
		// 更改个推key
		FileModify.replace(Constants.AndroidManifest, Constants.GeTui.Release.PUSH_APPID, Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPID, Constants.GeTui.Test.PUSH_APPID);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPKEY, Constants.GeTui.Test.PUSH_APPKEY);
		FileModify.replace(Constants.build_gradle, Constants.GeTui.Release.PUSH_APPSECRET, Constants.GeTui.Test.PUSH_APPSECRET);

		// 配置地址
		FileModify.replace(Constants.Constant, Constants.Url.develop, Constants.Url.test);
		FileModify.replace(Constants.Constant, Constants.Url.emulate, Constants.Url.test);
		FileModify.replace(Constants.Constant, Constants.Url.release, Constants.Url.test);
		// 配置是否打日志
		FileModify.replace(Constants.FFConfig, Constants.Log.release, Constants.Log.develop);
	}

    public static void Shoufa(java.util.Scanner sc) {
		System.out.println("请输入版本号");
		String str = sc.nextLine();

    }
}
