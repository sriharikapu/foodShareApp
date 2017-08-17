package com.fengnian.smallyellowo.foodie.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import com.fan.framework.utils.FileUitl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


public class ImageUtil {


	public static int getBitmapSize(Bitmap bitmap) {
		if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	
	public static Bitmap loadRightDirectioniBitmap(Bitmap bm, String imgpath) {
		int digree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imgpath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}
		if (exif != null) {
			
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:

				break;
			}
		}
		Log.e("ImageUtil", "loadRightDirectioniBitmap degree: " + digree);
		if (digree != 0) {
			
			Matrix m = new Matrix();
			m.postRotate(digree);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					m, true);
		}
		return bm;
	}


	public static Bitmap compressImageFromFile(String srcPath,int deviceWidth,int deviceHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int be = 1;
		if (newOpts.outWidth > newOpts.outHeight) {
			if (newOpts.outWidth > deviceWidth) {
				be = (int) (newOpts.outWidth /deviceWidth);
			} else {
				be = (int) (newOpts.outHeight / deviceHeight);
			}
		} else if (newOpts.outWidth <= newOpts.outHeight) {
			if (newOpts.outHeight > deviceHeight) {
				be = (int) (newOpts.outHeight / deviceHeight);
			} else {
				be = (int) (newOpts.outWidth / deviceWidth);
			}
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//
		newOpts.inPreferredConfig = Config.ARGB_8888;//
		newOpts.inPurgeable = true;//
		newOpts.inInputShareable = true;// 
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	
//	public static String compressBmpToFile(Bitmap bmp, File file) {
//		try {
//			if (null == file) {
//				file = FileUitl.generateFile("image.jpg");//生成带有后缀名的文件
//
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//			} else if (file.exists() && file.length() > 0) {
//				file.delete();
//				file.createNewFile();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int options = 80;//
//		bmp.compress(CompressFormat.JPEG, options, baos);
//		while (baos.toByteArray().length / 1024 > 100) {
//			baos.reset();
//			options -= 10;
//			if (options == 0) { //
//				options = 1;
//				bmp.compress(CompressFormat.JPEG, options, baos);
//				break;
//			}
//			bmp.compress(CompressFormat.JPEG, options, baos);
//		}
//		try {
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(baos.toByteArray());
//			fos.flush();
//			fos.close();
//
//			return file.getAbsolutePath();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	public static int getImageSize(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			return (int) (file.length() / 1024/1024);
		} else {
			return 0;
		}
	}

//	/**
//	 *
//	 *
//	 */
//	public static String compressImage(String sourceImagePath,
//			String outDirectory, int maxWidth, int maxHeight) {
//		BitmapFactory.Options ops = new BitmapFactory.Options();
//		ops.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(sourceImagePath, ops);
//		int imageise = getImageSize(sourceImagePath);
//		if (imageise == 0)
//			return sourceImagePath;
//		if (imageise <= 100) {
//			return sourceImagePath;
//		} else if (100 < imageise && imageise <= 500) {// 100k--500k
//
//		} else if (500 < imageise && imageise <= 1000) {
//			maxWidth = maxWidth + 20;
//			maxHeight = maxHeight + 20;
//		} else if (1000 < imageise && imageise <= 2000) {
//			maxWidth = maxWidth + 50;
//			maxHeight = maxHeight + 50;
//		} else if (2000 < imageise && imageise <= 5000) {
//			maxWidth = maxWidth + 100;
//			maxHeight = maxHeight + 100;
//		} else if (imageise > 5000) {
//			maxWidth = maxWidth + 200;
//			maxHeight = maxHeight + 200;
//		}
//		double ratio = 1.0;
//		if (ops.outWidth > ops.outHeight && ops.outWidth > maxWidth) {
//			ratio = ops.outWidth / maxWidth;
//		} else if (ops.outHeight > ops.outWidth && ops.outHeight > maxHeight) {
//			ratio = ops.outHeight / maxHeight;
//		} else if (ops.outWidth == ops.outHeight && ops.outWidth > maxWidth) {// ԭʼ�����ȣ�ȡ��ı���
//			ratio = ops.outWidth / maxWidth;
//		}
//		BitmapFactory.Options newOps = new BitmapFactory.Options();
//		newOps.inSampleSize = (int) (ratio + 1);
//		newOps.outWidth = (int) (ops.outWidth / ratio);
//		newOps.outHeight = (int) (ops.outHeight / ratio);
//		Bitmap bitmap = BitmapFactory.decodeFile(sourceImagePath, newOps);
//		File dir = new File(outDirectory);
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//		File outFile = new File(dir, new Date().getTime()+"");
//		try {
//			outFile.createNewFile();
//			// outFile.createNewFile();
//			OutputStream os = new FileOutputStream(outFile);
//			bitmap.compress(CompressFormat.JPEG, 100, os);
//			os.close();
//			bitmap.recycle();
//			return outFile.getAbsolutePath();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}

}
