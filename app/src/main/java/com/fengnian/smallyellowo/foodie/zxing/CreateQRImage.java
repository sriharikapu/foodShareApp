package com.fengnian.smallyellowo.foodie.zxing;

import android.graphics.Bitmap;

import com.fan.framework.utils.FFUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * @类功能说明: 生成二维码图片示例
 * @作者:
 * @时间:
 * @版本:
 */
public class CreateQRImage
{
//	private ImageView sweepIV;
	private static int QR_WIDTH = FFUtils.getPx(350), QR_HEIGHT = FFUtils.getPx(350);
	/**
	 * @方法功能说明: 生成二维码图片,实际使用时要初始化sweepIV,不然会报空指针错误
	 * @作者:
	 * @时间:
	 * @参数: @param url 要转换的地址或字符串,可以是中文
	 * @return void
	 * @throws
	 */
	public static Bitmap createQRImage(String url)
	{
		try
		{
			//判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1)
			{
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//下面这里按照二维码的算法，逐个生成二维码的图片，
			//两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff666666;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
             //显示到一个ImageView上面
    //			sweepIV.setImageBitmap(bitmap);
			return  bitmap;
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}


}
