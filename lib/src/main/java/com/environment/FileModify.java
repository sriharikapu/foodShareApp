package com.environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * 修改文件
 */
public class FileModify {

	/**
	 * 读取文件内容
	 *
	 * @param filePath
	 * @param replaceMent
	 * @param str
	 * @return
	 */
	public String read(String filePath, String regex, String replacement) {
		BufferedReader br = null;
		String line = null;
		StringBuffer buf = new StringBuffer();

		try {
			// 根据文件路径创建缓冲输入流
			File file = new File(filePath);
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file),"utf-8"));

			// 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
			while ((line = br.readLine()) != null) {
				// 此处根据实际需要修改某些行的内容
				if(line.contains(regex) &&!regex.equals(replacement)){
					line = line.replaceAll(regex, replacement);
					System.out.println(file.getName()+":"+line+":已替换"+replacement);
				}
				buf.append(line);
				buf.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
				}
			}
		}

		return buf.toString();
	}

	/**
	 * 将内容回写到文件中
	 *
	 * @param filePath
	 * @param content
	 */
	public void write(String filePath, String content) {
		BufferedWriter bw = null;

		try {
			// 根据文件路径创建缓冲输出流
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath)), "utf-8"));
			// 将内容写入文件中
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}

	public void fileAppender(String fileName, String content) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		// 一行一行的读
		StringBuilder sb = new StringBuilder();
		sb.append(content);
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		reader.close();

		// 写回去
		RandomAccessFile mm = new RandomAccessFile(fileName, "rw");
		mm.writeBytes(sb.toString());
		mm.close();
	}

	/**
	 * 主方法
	 */
	public static void replace(String path, String regex, String replacement) {
		String filePath = path; // 文件路径
		FileModify obj = new FileModify();
		obj.write(filePath, obj.read(filePath, regex, replacement)); // 读取修改文件
		// try {
		// obj.fileAppender(filePath, "set a=b \n");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

}