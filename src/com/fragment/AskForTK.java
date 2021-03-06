package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.base.BaseActivity;
import com.lease.R;
import com.util.Util;

public class AskForTK extends BaseActivity {
	Message msg;
	TextView tkcontent, title;
	String content = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.askfor_tk_activity);

		creatMsg(0, 0);
	}

	void init() {
		tkcontent = (TextView) findViewById(R.id.tk_content);
		title = (TextView) findViewById(R.id.tk_title);
		findViewById(R.id.back_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;

		default:
			break;
		}
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void setcontent() {
		if (Util.tktag) {
			title.setText("优尼斯租赁业务提示函");
			content = "尊敬的客户："
					+ "\n\u3000\u3000"
					+ "为了让您更好地享受优尼斯融资租赁（上海）有限公司（下称“优尼斯租赁”）推出的租赁服务，特将该业务需要注意的事项提示如下："
					+ "\n\u3000\u3000"
					+ "一、如您申请租赁业务，无论分时计费或固定计费模式，请确保设备在整个租赁期间可接入互联网。请您务必保持设备处于实时联网状态，否则该设备的使用可能受到限制。"
					+ "\n\u3000\u3000"
					+ "二、请务必以承租人的名义支付租赁合同项下的相应款项。自然人客户申请租赁业务，优尼斯租赁将向实际签署租赁合同的自然人承租人开具租金发票。如需向企业开具发票，请以企业名义申请租赁业务。"
					+ "\n\u3000\u3000"
					+ "三、自然人客户请优先使用右手食指在合同相应处按手印。"
					+ "\n\u3000\u3000"
					+ "四、企业客户申请租赁业务时，请提交声明本企业及其实际控制人征信无异常的《声明书》，并提供授权查询本企业及其实际控制人征信情况的授权书。"
					+ "\n\u3000\u3000"
					+ "五、优尼斯租赁将按您申请租赁业务时提供的地址交付设备，请确保您提供的地址与设备实际使用地址一致。"
					+ "\n\u3000\u3000"
					+ "六、设备送达后，请及时签署《租赁物交付确认单》，以确认设备到货时间、质量合格状况。如自设备送达日起7天内您仍未签署《租赁物交付确认单》且未要求调换租赁物，优尼斯租赁将合理推定您已接受租赁合同项下设备，并视为优尼斯租赁已完成设备交付的义务，设备的实际送达日期将被确定为起租日。"
					+ "\n\u3000\u3000"
					+ "七、优尼斯租赁将根据您在租赁合同上注明的通知方式向您发出租金支付提示，请于每月5日前支付优尼斯租赁确认的租金。特别提醒： 每逾期一天，您将承担逾期金额千分之五的违约金。"
					+ "\n\u3000\u3000"
					+ "八、请在租赁合同期限届满15个工作日前，决定是否续租并书面通知优尼斯租赁。"
					+ "\n\u3000\u3000"
					+ "九、分时计费租赁业务注意事项："
					+ "\n\u3000\u3000"
					+ "1. 您在签署《租赁物交付确认单》时，请与服务商共同确认设备的初始计费时间，并拍摄相应的照片。如不能保持联网状态，除不可抗力原因外，优尼斯租赁将以每天20小时为标准收取租金。"
					+ "\n\u3000\u3000"
					+ "2. 如因您原因导致租赁期内连续3个自然月内月平均开机时间不满240小时的，优尼斯租赁有权按固定计费标准收费，或提前收回租赁物并解除租赁合同。"
					+ "\n" + "\n" + "特此提示。";
		} else {
			title.setText("数据采集函");
			content = "\u3000\u3000"
					+ " 感谢您以租赁业务模式使用我司提供的沈阳机床i5系列设备（下称“租赁物”），为便于实时了解、掌握租赁物的现状及其运转和保管情况，以防对租赁物违规操作、违规迁移等情况的发生，提高对租赁物的统一管理效率，我司将对租赁物进行实时在线监管，并需要实时在线采集租赁物使用数据，包括但不限于租赁物的联网情况、位置信息、运转和保管情况等数据。"
					+ "\n\u3000\u3000"
					+ "您在使用租赁物时也使用了有关的软件、服务（以下统称“服务”），并受您与我司签署的合同/协议或出具法律文件的约束。请您务必阅读并理解以下条款，如您签署本文件，则表示您已知晓并同意本政策的全部内容，在使用租赁物时将受到本政策条款的约束："
					+ "\n\u3000\u3000"
					+ "一、 您在租赁物送达前，应按照本政策的要求，完成租赁物联网所需条件的准备，包括但不限于提供租赁物联网所需的场地、电源、网络设备、互联网上网服务及环境等。"
					+ "\n\u3000\u3000"
					+ "二、 我司独家授权委托智能云科信息科技有限公司（以下简称“智能云科”）负责实施租赁物的联网及相关数据采集工作。智能云科可至租赁合同约定的租赁物使用地点完成租赁物联网工作，您应当积极配合并为实施租赁物的联网提供便利。"
					+ "\n\u3000\u3000"
					+ "三、 在租赁物完成联网后，您应确保租赁物处于实时联网状态，以便我司能够实时采集租赁物的地理位置、运行状态和联网状态等数据信息，及完成租赁物的软件系统升级或优化等，同时提高租金结算效率。"
					+ "\n\u3000\u3000"
					+ "四、 如您未能按照本政策的要求配合租赁物实施联网，或在租赁物完成联网后未能保持实时联网状态，导致我司无法采集租赁物数据信息，您将可能违反本政策或您与我司签订的有关合同/协议，并可能面临不利后果，包括但不限于租赁物无法正常使用、承担违约责任等。"
					+ "\n\u3000\u3000"
					+ "五、 您同意我司及我司委托人员在不影响您正常生产经营活动的前提下，随时到租赁物使用地点检查和调查租赁物的现状以及运转和保管情况，您应给予积极配合。如未达到本政策要求的情形，我司可当场要求您整改。若您在接到我们书面通知后5日内仍未完成整改的，将被视为违反了本政策或您与我们签订的有关合同/协议，可能面临对您不利的后果。"
					+ "\n\u3000\u3000"
					+ "六、 您同意智能云科在不泄露您商业秘密的前提下有权使用采集的租赁物数据信息。" + "\n";
		}
		tkcontent.setText(content);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				init();
				setcontent();
				break;

			default:
				break;
			}
		};
	};
}
