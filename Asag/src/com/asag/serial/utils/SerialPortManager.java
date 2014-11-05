package com.asag.serial.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import android.util.Log;

import com.dwin.navy.serialportapi.SerialPortOpt;

public class SerialPortManager {

	private SerialPortOpt serialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	
	private LinkedList<byte[]> byteLinkedList = new LinkedList<byte[]>();
	private String TAG = "SerialPortManager";
	private boolean m_bShowDateType = false;
	private boolean m_bSendDateType = false;
	
	public SerialCallBack listener;
	
	public interface SerialCallBack {
		public void onCallBack(String message);
	}
	
	public SerialPortManager(SerialCallBack listener) {
		initSerial();
		this.listener = listener;
	}
	
	public void sendMsg(String src) {
		Log.d(TAG, "src : " + src );
		if (!m_bSendDateType) {
			src = src.replace(" ", "");
			if (1 == (src.length() % 2)) {
				src += "0";
			}
			if (null != serialPort.mFd) {
				// Log.i(TAG, src);
				serialPort.writeBytes(HexString2Bytes(src));
			}
		} else {
			if (null != serialPort.mFd) {
				// Log.i(TAG, src);
				serialPort.writeBytes(src.getBytes());
			}
		}
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}
	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
	
	
	public static String bytesToHexString(byte[] src, int size) {
		String ret = "";
		if (src == null || size <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			String hex = Integer.toHexString(src[i] & 0xFF);
			// String hex = String.format("%02x", src[i] & 0xFF);
			Log.i("bytesToHexString", hex);
			if (hex.length() < 2) {
				hex = "0" + hex;
			}
			hex += " ";
			ret += hex;
		}
		return ret.toUpperCase();
	}
	
	// 打开端口
	public void openSerialPort() {

		if (serialPort.mFd == null) {
			serialPort.openDev(serialPort.mDevNum);

			Log.i("uart port operate", "Mainactivity.java==>uart open");
			serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);
			Log.i("uart port operate", "Mainactivity.java==>uart set speed..."
					+ serialPort.mSpeed);
			serialPort.setParity(serialPort.mFd, serialPort.mDataBits,
					serialPort.mStopBits, serialPort.mParity);
			Log.i("uart port operate",
					"Mainactivity.java==>uart other params..."
							+ serialPort.mDataBits + "..."
							+ serialPort.mStopBits + "..." + serialPort.mParity);

			mInputStream = serialPort.getInputStream();
			mOutputStream = serialPort.getOutputStream();
			Log.i("uart port operate", "Mainactivity.java==>start ReadThread");
			mReadThread = new ReadThread();
			mReadThread.start();
			//Log.i("uart port operate", "Mainactivity.java==>ReadThread started");
		}
	}
	// 关闭串口 
	public void closeSerialPort() {

		if (mReadThread != null) {
			mReadThread.interrupt();
		}

		if (serialPort.mFd != null) {
			Log.i("uart port operate", "Mainactivity.java==>uart stop");
			serialPort.closeDev(serialPort.mFd);
			Log.i("uart port operate", "Mainactivity.java==>uart stoped");
		}
	}

	private class ReadThread extends Thread {

		byte[] buf = new byte[1024];
		@Override
		public void run() {
			super.run();
			Log.i("SerialPortManger", "ReadThread==>buffer:" + buf.length);
			while (!isInterrupted()) {
				int size;
				if (mInputStream == null)
					return;
				size = serialPort.readBytes(buf);

				if (size > 0) {
					
					// new String(buf, 0, size).getBytes()会造成数据错误
					byte[] dest = new byte[size];
					System.arraycopy(buf, 0, dest, 0, size);
					// 使用队列接受数据，解决上版本串口接受
					// 连续大量数据后出现的数据混乱
//					byteLinkedList.offer(new String(buf, 0, size).getBytes());
					byteLinkedList.offer(dest);
					if (!byteLinkedList.isEmpty()) {
						byte[] buf = byteLinkedList.poll();
						int sizereceiver = buf.length;
//						if (m_bShowDateType) {
							Log.i(TAG,bytesToHexString(buf, size));
							String receiverdata = bytesToHexString(buf, size).replace(" ","").trim();
							if (listener != null) {
								listener.onCallBack(receiverdata);
							}
//						} else {
//							Log.i(TAG,bytesToHexString(buf, size));
//						}
					}
					Log.i(TAG, "ReadThread==>" + size);
				}
			}
			buf =null;
		}
		
	}
	
	public void initSerial() {
		serialPort = new SerialPortOpt();
		// 0 表示com0
		serialPort.mDevNum = 0;
		// 波特率 115200
		serialPort.mSpeed = 115200;
		// 数据位8
		serialPort.mDataBits = 8;
		// 停止位 1
		serialPort.mStopBits = 1;
		// 校验位 'n', 'o', 'e', 'm', 's'
		serialPort.mParity = 'n';
		// 打开串口 COM 0
		openSerialPort();
	}
}
