package com.zhangqiang.common.utils;

public class ByteUtils {

	private static String hexStr =  "0123456789ABCDEF";
	private static String[] binaryArray =
			{"0000","0001","0010","0011",
					"0100","0101","0110","0111",
					"1000","1001","1010","1011",
					"1100","1101","1110","1111"};

	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(final char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 数组转成十六进制字符串
	 * @param b
	 * @return HexString
	 */
	public static String toHexString(final byte[] b){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i){
			buffer.append(toHexString1(b[i]));
		}
		return buffer.toString();
	}
	public static String toHexString1(final byte b){
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1){
			return "0" + s;
		}else{
			return s;
		}
	}


	/**
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param src byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(final byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 补位操作
	 * @param src 内容hex
	 * @param da 补位信息
	 * @param nums 长度
	 * @return
	 */
	public static String hexAddData(final String src, String da, final int nums){
		StringBuilder sb = new StringBuilder();
		int num = nums-src.length();
		for(int i=0;i<num;i++){
			sb.append(da);
		}
		sb.append(src);
		return sb.toString();
	}


	/**
	 * 16String-10int
	 * @param hex 内容hex
	 * @return
	 */
	public static int hexToTen(final String hex){
		Long l = Long.parseLong(hex,  16);//高位
		return l.intValue();
	}

	/**
	 *
	 * @param hexString
	 * @return 将十六进制转换为二进制字符串   16-2
	 */
	public static String hexStr2BinStr(String hexString){
		return bytes2BinStr(hexStr2BinArr(hexString));
	}

	/**
	 *
	 * @param hexString
	 * @return 将十六进制转换为二进制字节数组   16-2
	 */
	public static byte[] hexStr2BinArr(String hexString){
		//hexString的长度对2取整，作为bytes的长度
		int len = hexString.length()/2;
		byte[] bytes = new byte[len];
		byte high = 0;//字节高四位
		byte low = 0;//字节低四位
		for(int i=0;i<len;i++){
			//右移四位得到高位
			high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
			low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
			bytes[i] = (byte) (high|low);//高地位做或运算
		}
		return bytes;
	}

	/**
	 *
	 * @param bArray
	 * @return 二进制数组转换为二进制字符串   2-2
	 */
	public static String bytes2BinStr(byte[] bArray){

		String outStr = "";
		int pos = 0;
		for(byte b:bArray){
			//高四位
			pos = (b&0xF0)>>4;
			outStr+=binaryArray[pos];
			//低四位
			pos=b&0x0F;
			outStr+=binaryArray[pos];
		}
		return outStr;
	}

}
