package com.asag.serial.tool;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.asag.serial.R;
import com.dwin.navy.serialportapi.SerialPortOpt;

public class ToolApi extends Activity {

	private static final String TAG = "MainActivity";
	private SerialPortOpt serialPort;

	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private SendThread mSendThread;
	private TimerSendThread mTimerSendThread;

	private boolean m_bShowDateType = false;
	private boolean m_bSendDateType = false;

	private static final String[] Sserialport = { "COM0", "COM1", "COM2",
			"COM3", "COM4" };
	private static final int[] m_iSerialPort = { 0, 1, 2, 3, 4 };

	private static final String[] Sbaudrate = { "115200", "57600", "38400",
			"19200", "9600", "4800", "2400", "1200", "300", };
	private static final int[] baudrate = { 115200, 57600, 38400, 19200, 9600,
			4800, 2400, 1200, 300, };

	private static final String[] Sdatabits = { "5", "6", "7", "8" };
	private static final int[] databits = { 5, 6, 7, 8 };

	private static final String[] Sstopbits = { "1", "2", };
	private static final int[] stopbits = { 1, 2, };

	private static final String[] Sparity = { "None", "Odd", "Even", "Mark",
			"Space" };
	private static final int[] parity = { 'n', 'o', 'e', 'm', 's' };

	TextView tViewSerialPort;
	TextView tViewBaudRate;
	TextView tViewDataBits;
	TextView tViewParity;
	TextView tViewComm;
	EditText eTextShowMsg;
	EditText eTextSendMsg;

	private ToggleButton togBtnSerial;
	private ToggleButton togBtnShowDateType;
	private ToggleButton togBtnSendDateType;
	private Button btnClear;
	private Button btnSend;

	private ToggleButton togBtnSendPer100ms;
	private ToggleButton togBtnSendPer500ms;
	private ToggleButton togBtnSendPer1000ms;

	private Spinner spiChooseSerialPort;
	private Spinner spiChooseBaudRate;
	private Spinner spiChooseDataBits;
	private Spinner spiChooseStopBits;
	private Spinner spiChooseParity;

	private ArrayAdapter<String> adapSerialPort;
	private ArrayAdapter<String> adapBaudRate;
	private ArrayAdapter<String> adapDataBits;
	private ArrayAdapter<String> adapStopBits;
	private ArrayAdapter<String> adapParity;

	private static Object lock = new Object();
	private LinkedList<byte[]> byteLinkedList = new LinkedList<byte[]>();

	String m_strReadThread = "readThread";
	String m_strSendThread = "sendThread";

	String strChoosedSerial = "tty0";
	int nChoosedBaud = 115200;

	String m_strHex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		serialPort = new SerialPortOpt();

		eTextShowMsg = (EditText) findViewById(R.id.showmsg);
		eTextSendMsg = (EditText) findViewById(R.id.sendmsg);

		togBtnShowDateType = (ToggleButton) findViewById(R.id.togBtnShowDateType);
		togBtnSendDateType = (ToggleButton) findViewById(R.id.togBtnSendDateType);
		togBtnSerial = (ToggleButton) findViewById(R.id.togBtnSerial);
		btnClear = (Button) findViewById(R.id.clearButton);
		btnSend = (Button) findViewById(R.id.sendButton);

		togBtnSendPer100ms = (ToggleButton) findViewById(R.id.togBtnSendPer100ms);
		togBtnSendPer500ms = (ToggleButton) findViewById(R.id.togBtnSendPer500ms);
		togBtnSendPer1000ms = (ToggleButton) findViewById(R.id.togBtnSendPer1000ms);

		// *****************************************
		// set SerialPort spinner
		// *****************************************
		spiChooseSerialPort = (Spinner) findViewById(R.id.choose_seriaPort_spinner);
		adapSerialPort = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Sserialport);
		adapSerialPort
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spiChooseSerialPort.setAdapter(adapSerialPort);

		// *****************************************
		// set BaudRate spinner
		// *****************************************
		spiChooseBaudRate = (Spinner) findViewById(R.id.choose_baudRate_spinner);
		adapBaudRate = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Sbaudrate);
		adapBaudRate
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spiChooseBaudRate.setAdapter(adapBaudRate);

		// *****************************************
		// set DataBit spinner
		// *****************************************
		spiChooseDataBits = (Spinner) findViewById(R.id.choose_databits_spinner);
		adapDataBits = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Sdatabits);
		adapDataBits
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spiChooseDataBits.setAdapter(adapDataBits);

		// *****************************************
		// set StopBit spinner
		// *****************************************
		spiChooseStopBits = (Spinner) findViewById(R.id.choose_stopbits_spinner);
		adapStopBits = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Sstopbits);
		adapStopBits
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spiChooseStopBits.setAdapter(adapStopBits);

		// *****************************************
		// set Parity spinner
		// *****************************************
		spiChooseParity = (Spinner) findViewById(R.id.choose_parity_spinner);
		adapParity = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Sparity);
		spiChooseParity.setAdapter(adapParity);

		spiChooseSerialPort.setSelection(0);
		spiChooseBaudRate.setSelection(0);
		spiChooseDataBits.setSelection(3);
		spiChooseStopBits.setSelection(1);
		spiChooseParity.setSelection(0);

		serialPort.mDevNum = m_iSerialPort[0];
		serialPort.mSpeed = baudrate[0];
		serialPort.mDataBits = databits[3];
		serialPort.mStopBits = stopbits[1];
		serialPort.mParity = parity[0];

		spiChooseSerialPort
				.setOnItemSelectedListener(new SpiOnItemSelectedListenerImpl());
		spiChooseBaudRate
				.setOnItemSelectedListener(new SpiOnItemSelectedListenerImpl());
		spiChooseDataBits
				.setOnItemSelectedListener(new SpiOnItemSelectedListenerImpl());
		spiChooseStopBits
				.setOnItemSelectedListener(new SpiOnItemSelectedListenerImpl());
		spiChooseParity
				.setOnItemSelectedListener(new SpiOnItemSelectedListenerImpl());

		togBtnShowDateType
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());
		togBtnSendDateType
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());

		togBtnSerial
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());

		btnClear.setOnClickListener(new BtnClearOnClickListener());
		btnSend.setOnClickListener(new BtnSendOnClickListener());

		togBtnSendPer100ms
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());
		togBtnSendPer500ms
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());
		togBtnSendPer1000ms
				.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListenerImpl());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "==>onPause in");
		super.onPause();
		Log.i(TAG, "==>onPause out");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, "==>onStop in");
		super.onStop();
		Log.i(TAG, "==>onStop out");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "==>onDestroy in");
		closeSerialPort();
		serialPort = null;
		super.onDestroy();
		Log.i(TAG, "==>onDestroy out");
	}

	private void openSerialPort() {

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
			Log.i("uart port operate", "Mainactivity.java==>ReadThread started");
		}
	}

	private void closeSerialPort() {

		if (mReadThread != null) {
			mReadThread.interrupt();
			// mReadThread = null;
		}

		if (serialPort.mFd != null) {
			Log.i("uart port operate", "Mainactivity.java==>uart stop");
			serialPort.closeDev(serialPort.mFd);
			Log.i("uart port operate", "Mainactivity.java==>uart stoped");
		}
	}

	private class ReadThread extends Thread {
		byte[] buf = new byte[512];

		@Override
		public void run() {
			super.run();
			Log.i(TAG, "ReadThread==>buffer:" + buf.length);
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
					onDataReceived();
					Log.i(TAG, "ReadThread==>" + size);
				}
			}
		}
	}

	protected void onDataReceived() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (eTextShowMsg != null) {
					if (!byteLinkedList.isEmpty()) {
						byte[] buf = byteLinkedList.poll();
						int size = buf.length;
						if (m_bShowDateType) {
							eTextShowMsg.append(new String(buf, 0, size));
							Log.i("eTextShowMsg ASIIC",
									new String(buf, 0, size));
						} else {
							eTextShowMsg.append(bytesToHexString(buf, size));
							Log.i("eTextShowMsg HEX",
									bytesToHexString(buf, size));
						}
					}
				}
			}
		});
	}

	/*
	 * public static String bytesToHexString(byte[] src,int size){ StringBuilder
	 * stringBuilder = new StringBuilder(""); if (src == null || size <= 0) {
	 * return null; } for (int i = 0; i < size; i++) { int v = src[i] & 0xFF;
	 * String hv = Integer.toHexString(v); if (hv.length() < 2) {
	 * stringBuilder.append(0); } stringBuilder.append(hv); } return
	 * stringBuilder.toString(); }
	 */

	// private static String bytesToHexString(byte[] src, int size) {
	// char[] c = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
	// '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	// String ret = "";
	// if (src == null || size <= 0) {
	// return null;
	// }
	// for (int i = 0; i < size; i++) {
	// char[] sb = new char[2];
	// sb[1] = (c[src[i] & 0x0f]);
	// sb[0] = (c[(src[i] & 0xf0) >>> 4]);
	// String hex = new String(sb);
	// if (hex.length() < 2) {
	// hex = "0" + hex;
	// }
	// hex += " ";
	// ret += hex;
	// }
	// return ret.toUpperCase();
	// }

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

	private class SendThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "SendThread==>run");
			super.run();
			SendMsg();
		}
	}

	private class TimerSendThread extends Thread {

		private long m_lTimer = 100; // default 100ms
		private boolean m_bRunFlag = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "TimerSendThread==>run");
			super.run();
			while (m_bRunFlag) {
				Log.i(TAG, "TimerSendThread==>" + m_lTimer);
				SendMsg();
				if (m_lTimer <= 0) { // must over 0ms
					m_lTimer = 100;
				}
				try {
					Thread.sleep(m_lTimer);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void setSleepTimer(long timer) {
			m_lTimer = timer;
		}

		public void stopThread() {
			m_bRunFlag = false;
		}
	}

	private void SendMsg() {

		String src = eTextSendMsg.getText().toString();
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

	private class SpiOnItemSelectedListenerImpl implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (parent == spiChooseSerialPort) {
				serialPort.mDevNum = m_iSerialPort[position];
				// tViewSerialPort.setText(Sserialport[position]);
				Log.i("uart params set", "ChooseSerialPort_"
						+ serialPort.mDevNum);
			} else if (parent == spiChooseBaudRate) {
				serialPort.mSpeed = baudrate[position];
				// tViewBaudRate.setText(Sbaudrate[position]);
				Log.i("uart params set", "ChooseBaudRate_" + serialPort.mSpeed);

			} else if (parent == spiChooseDataBits) {
				serialPort.mDataBits = databits[position];
				Log.i("uart params set", "ChooseDataBits_"
						+ serialPort.mDataBits);

			} else if (parent == spiChooseStopBits) {
				serialPort.mStopBits = stopbits[position];
				Log.i("uart params set", "ChooseStopBits_"
						+ serialPort.mStopBits);

			} else if (parent == spiChooseParity) {
				serialPort.mParity = parity[position];
				Log.i("uart params set", "ChooseParity_" + serialPort.mParity);

			}

			if (serialPort.mFd != null) {
				serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);
				Log.i("uart port operate", "====>here...uart set speed..."
						+ serialPort.mSpeed);
				serialPort.setParity(serialPort.mFd, serialPort.mDataBits,
						serialPort.mStopBits, serialPort.mParity);
				Log.i("uart port operate", "====>here...uart other params..."
						+ serialPort.mDataBits + "..." + serialPort.mStopBits
						+ "..." + serialPort.mParity);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	private class BtnClearOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			eTextShowMsg.setText("");
		}

	}

	private class BtnSendOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSendThread = new SendThread();
			mSendThread.start();
		}
	}

	private class TogBtnOnCheckedChangeListenerImpl implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Log.i(TAG, "TogBtnOnCheckedChange in");
			// TODO Auto-generated method stub
			if ((ToggleButton) buttonView == togBtnSerial) {
				if (isChecked) {
					openSerialPort();
				} else {
					closeSerialPort();
				}

			} else if ((ToggleButton) buttonView == togBtnShowDateType) {
				if (isChecked) {
					m_bShowDateType = isChecked;
					Log.i(TAG, "TogBtnOnCheckedChange==>m_bShowDateType=true");
				} else {
					m_bShowDateType = isChecked;
					Log.i(TAG, "TogBtnOnCheckedChange==>m_bShowDateType=false");
				}

			} else if ((ToggleButton) buttonView == togBtnSendDateType) {
				if (isChecked) {
					m_bSendDateType = isChecked;
					Log.i(TAG, "TogBtnOnCheckedChange==>m_bSendDateType=true");
				} else {
					m_bSendDateType = isChecked;
					Log.i(TAG, "TogBtnOnCheckedChange==>m_bSendDateType=false");
				}

			} else if ((ToggleButton) buttonView == togBtnSendPer100ms) {
				if (isChecked) {
					mTimerSendThread = new TimerSendThread();
					mTimerSendThread.setSleepTimer(100);
					mTimerSendThread.start();
					btnSend.setEnabled(false);
					togBtnSendPer500ms.setEnabled(false);
					togBtnSendPer1000ms.setEnabled(false);
				} else {
					if (mTimerSendThread != null) {
						mTimerSendThread.stopThread();
						mTimerSendThread = null;
						btnSend.setEnabled(true);
						togBtnSendPer500ms.setEnabled(true);
						togBtnSendPer1000ms.setEnabled(true);
					}
				}
			} else if ((ToggleButton) buttonView == togBtnSendPer500ms) {
				if (isChecked) {
					mTimerSendThread = new TimerSendThread();
					mTimerSendThread.setSleepTimer(500);
					mTimerSendThread.start();
					btnSend.setEnabled(false);
					togBtnSendPer100ms.setEnabled(false);
					togBtnSendPer1000ms.setEnabled(false);
				} else {
					if (mTimerSendThread != null) {
						mTimerSendThread.stopThread();
						mTimerSendThread = null;
						btnSend.setEnabled(true);
						togBtnSendPer100ms.setEnabled(true);
						togBtnSendPer1000ms.setEnabled(true);
					}
				}
			} else if ((ToggleButton) buttonView == togBtnSendPer1000ms) {
				if (isChecked) {
					mTimerSendThread = new TimerSendThread();
					mTimerSendThread.setSleepTimer(1000);
					mTimerSendThread.start();
					btnSend.setEnabled(false);
					togBtnSendPer500ms.setEnabled(false);
					togBtnSendPer100ms.setEnabled(false);
				} else {
					if (mTimerSendThread != null) {
						mTimerSendThread.stopThread();
						mTimerSendThread = null;
						btnSend.setEnabled(true);
						togBtnSendPer500ms.setEnabled(true);
						togBtnSendPer100ms.setEnabled(true);
					}
				}
			}
			Log.i(TAG, "TogBtnOnCheckedChange out");

		}
	}
}
