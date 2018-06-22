package com.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dto.AlipayInDto;
import com.dto.PayResult;
import com.fragment.HistroyOrderFragment;
import com.fragment.RechargeFragment;
import com.util.MyApplication;

public class PayUtil {
	static AlipayInDto dto;
	static Message msg;

	static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				PayResult payResult = new PayResult((String) msg.obj);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(MyApplication.getAppContext(), "支付成功",
							Toast.LENGTH_SHORT).show();
					RechargeFragment.sucesse();
					HistroyOrderFragment.sucesse();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(MyApplication.getAppContext(),
								"支付结果确认中", Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(MyApplication.getAppContext(), "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;

			default:
				break;
			}
		};
	};

	// call alipay sdk pay. 调用SDK支付
	public static void pay(final Activity activity, AlipayInDto cashdto) {
		dto = cashdto;
		String orderInfo = getOrderInfo(dto.subject, dto.body, dto.total_fee);
		// 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		String sign = dto.sign;// yxd
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type="
				+ "\"" + dto.sign_type + "\"";// yxd

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(activity);
				String result = alipay.pay(payInfo, true);
				msg = new Message();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		new Thread(payRunnable).start();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	static String getOrderInfo(String subject, String body, String price) {
		String orderInfo = "partner=" + "\"" + dto.partner + "\"";
		orderInfo += "&seller_id=" + "\"" + dto.seller_id + "\"";
		orderInfo += "&out_trade_no=" + "\"" + dto.out_trade_no + "\""; // yxd
		orderInfo += "&subject=" + "\"" + subject + "\"";
		orderInfo += "&body=" + "\"" + body + "\"";
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		orderInfo += "&notify_url=" + "\"" + dto.notify_url + "\""; // yxd
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		orderInfo += "&payment_type=\"1\"";
		orderInfo += "&_input_charset=\"utf-8\"";
		orderInfo += "&it_b_pay=\"30m\"";
		return orderInfo;
	}
}