package com.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.InOrderDto;
import com.dto.OrderDto;
import com.dto.ResultDto;
import com.lease.R;
import com.net.Config;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.SelectPicActivity;
import com.tools.UploadUtil;
import com.util.Util;

public class AskFor2Fragment extends BaseFragment implements OnClickListener {
	Message msg;
	Dialog dialog;
	static TextView person, company, person_uptxt1, person_uptxt2,
			person_uptxt3, company_busiuptxt, company_formuptxt,
			company_certiuptxt1, company_certiuptxt2, company_certiuptxt3;
	View view, showview;
	LinearLayout contentlay;
	LayoutInflater inflater;
	CheckBox cb;
	String type;
	EditText person_name, person_phone, person_email, person_address,
			person_posital, friend, relation, fricall, company_name,
			company_address, company_linkaddress, company_posital,
			company_legal, company_legalcall, company_legal_email;
	boolean isPerson, personPage, companyPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.askfor2_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();
		creatMsg(0, 0);
		return view;
	}

	void init() {
		checkType();
		person = (TextView) view.findViewById(R.id.askfor2_person);
		company = (TextView) view.findViewById(R.id.askfor2_company);
		contentlay = (LinearLayout) view.findViewById(R.id.askfor2_content);
		person.setOnClickListener(this);
		company.setOnClickListener(this);
	}

	public static void setUpTxt() {
		if (person_uptxt1 != null && Util.imgPath.containsKey("idCardFrontImg"))
			person_uptxt1.setText("已上传");
		if (person_uptxt2 != null && Util.imgPath.containsKey("idCardBackImg"))
			person_uptxt2.setText("已上传");
		if (person_uptxt3 != null && Util.imgPath.containsKey("idCardHandImg"))
			person_uptxt3.setText("已上传");
		if (company_busiuptxt != null
				&& Util.imgPath.containsKey("businessLicensePath"))
			company_busiuptxt.setText("已上传");
		if (company_formuptxt != null
				&& Util.imgPath.containsKey("lastYearApplicationFormPath"))
			company_formuptxt.setText("已上传");
		if (company_certiuptxt1 != null
				&& Util.imgPath.containsKey("idCardFrontImg"))
			company_certiuptxt1.setText("已上传");
		if (company_certiuptxt2 != null
				&& Util.imgPath.containsKey("idCardBackImg"))
			company_certiuptxt2.setText("已上传");
		if (company_certiuptxt3 != null
				&& Util.imgPath.containsKey("idCardHandImg"))
			company_certiuptxt3.setText("已上传");
	}

	View showView(int layout) {
		contentlay.removeAllViews();
		showview = inflater.inflate(layout, null);
		if (!personPage) {
			person_uptxt1 = (TextView) showview
					.findViewById(R.id.page_person_card_front);
			person_uptxt2 = (TextView) showview
					.findViewById(R.id.page_person_card_back);
			person_uptxt3 = (TextView) showview
					.findViewById(R.id.page_person_card_hand);
			person_name = (EditText) showview
					.findViewById(R.id.page_person_name);
			person_phone = (EditText) showview
					.findViewById(R.id.page_person_phone);
			person_email = (EditText) showview
					.findViewById(R.id.page_person_email);
			person_address = (EditText) showview
					.findViewById(R.id.page_person_address);
			person_posital = (EditText) showview
					.findViewById(R.id.page_person_posital);
			showview.findViewById(R.id.page_upload_1).setOnClickListener(this);
			showview.findViewById(R.id.page_upload_2).setOnClickListener(this);
			showview.findViewById(R.id.page_upload_3).setOnClickListener(this);
		} else {
			company_busiuptxt = (TextView) showview
					.findViewById(R.id.page_company_business);
			company_formuptxt = (TextView) showview
					.findViewById(R.id.page_company_taxes);
			company_certiuptxt1 = (TextView) showview
					.findViewById(R.id.page_company_legal_card1);
			company_certiuptxt2 = (TextView) showview
					.findViewById(R.id.page_company_legal_card2);
			company_certiuptxt3 = (TextView) showview
					.findViewById(R.id.page_company_legal_card3);
			company_name = (EditText) showview
					.findViewById(R.id.page_company_name);
			company_address = (EditText) showview
					.findViewById(R.id.page_company_address);
			company_linkaddress = (EditText) showview
					.findViewById(R.id.page_company_linkaddress);
			company_posital = (EditText) showview
					.findViewById(R.id.page_company_posital);
			company_legal = (EditText) showview
					.findViewById(R.id.page_company_legal);
			company_legalcall = (EditText) showview
					.findViewById(R.id.page_company_legal_call);
			company_legal_email = (EditText) showview
					.findViewById(R.id.page_company_legal_email);
			showview.findViewById(R.id.page_upload1).setOnClickListener(this);
			showview.findViewById(R.id.page_upload2).setOnClickListener(this);
			showview.findViewById(R.id.page_upload_11).setOnClickListener(this);
			showview.findViewById(R.id.page_upload_22).setOnClickListener(this);
			showview.findViewById(R.id.page_upload_33).setOnClickListener(this);
		}
		cb = (CheckBox) showview.findViewById(R.id.page_cb);
		friend = (EditText) showview.findViewById(R.id.page_friend);
		relation = (EditText) showview.findViewById(R.id.page_relation);
		fricall = (EditText) showview.findViewById(R.id.page_fricall);
		showview.findViewById(R.id.mind_sure).setOnClickListener(this);
		showview.findViewById(R.id.mind_return).setOnClickListener(this);
		return showview;
	}

	boolean checkinfo() {
		if (friend.getText().toString().equals("")
				|| relation.getText().toString().equals("")
				|| fricall.getText().toString().equals(""))
			return true;
		if (!personPage) {
			if (person_uptxt1.getText().equals("")
					|| person_uptxt2.getText().equals("")
					|| person_uptxt3.getText().equals("")
					|| person_name.getText().toString().equals("")
					|| person_phone.getText().toString().equals("")
					|| person_email.getText().toString().equals("")
					|| person_address.getText().toString().equals("")
					|| person_posital.getText().toString().equals(""))
				return true;
		} else {
			if (company_busiuptxt.getText().equals("")
					|| company_formuptxt.getText().equals("")
					|| company_certiuptxt1.getText().equals("")
					|| company_certiuptxt2.getText().equals("")
					|| company_certiuptxt3.getText().equals("")
					|| company_name.getText().toString().equals("")
					|| company_address.getText().toString().equals("")
					|| company_linkaddress.getText().toString().equals("")
					|| company_posital.getText().toString().equals("")
					|| company_legal.getText().toString().equals("")
					|| company_legalcall.getText().toString().equals("")
					|| company_legal_email.getText().toString().equals(""))
				return true;
		}
		return false;
	}

	void setDto() {
		Util.orderdto.needInvoices = cb.isChecked() ? "1" : "0";
		Util.inorderdto.emergencyContact = friend.getText().toString();
		Util.inorderdto.relation = relation.getText().toString();
		Util.inorderdto.emergencyContactMobile = fricall.getText().toString();
		if (!personPage) {
			Util.orderdto.leaseType = "0";
			Util.inorderdto.name = person_name.getText().toString();
			Util.inorderdto.mobile = person_phone.getText().toString();
			Util.inorderdto.email = person_email.getText().toString();
			Util.inorderdto.address = person_address.getText().toString();
			Util.inorderdto.postcode = person_posital.getText().toString();
		} else {
			Util.orderdto.leaseType = "1";
			Util.inorderdto.name = company_name.getText().toString();
			Util.inorderdto.address = company_address.getText().toString();
			Util.inorderdto.postalAddress = company_linkaddress.getText()
					.toString();
			Util.inorderdto.postcode = company_posital.getText().toString();
			Util.inorderdto.legalName = company_legal.getText().toString();
			Util.inorderdto.legalMobile = company_legalcall.getText()
					.toString();
			Util.inorderdto.legalEmail = company_legal_email.getText()
					.toString();
			if (Util.imgPath.containsKey("businessLicensePath"))
				Util.inorderdto.businessLicensePath = Util.imgPath
						.get("businessLicensePath");
			if (Util.imgPath.containsKey("lastYearApplicationFormPath"))
				Util.inorderdto.lastYearApplicationFormPath = Util.imgPath
						.get("lastYearApplicationFormPath");
			if (Util.imgPath.containsKey("certificatePath"))
				Util.inorderdto.certificatePath = Util.imgPath
						.get("certificatePath");
		}
		if (Util.imgPath.containsKey("idCardFrontImg"))
			Util.inorderdto.idCardFrontImg = Util.imgPath.get("idCardFrontImg");
		if (Util.imgPath.containsKey("idCardBackImg"))
			Util.inorderdto.idCardBackImg = Util.imgPath.get("idCardBackImg");
		if (Util.imgPath.containsKey("idCardHandImg"))
			Util.inorderdto.idCardHandImg = Util.imgPath.get("idCardHandImg");
		Util.orderdto.rentSideInfo = Util.inorderdto;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.askfor2_person:
			personPage = false;
			companyPage = false;
			person.setTextColor(Color.parseColor("#1ea439"));
			company.setTextColor(Color.parseColor("#333333"));
			contentlay.addView(showView(R.layout.page_person_fragment));
			break;

		case R.id.askfor2_company:
			if (isPerson) {
				Util.showMsg(this.getActivity(), "个人用户不能为企业申请！");
				return;
			}
			personPage = true;
			companyPage = true;
			person.setTextColor(Color.parseColor("#333333"));
			company.setTextColor(Color.parseColor("#1ea439"));
			contentlay.addView(showView(R.layout.page_company_fragment));
			break;

		case R.id.mind_return:
			Jpage(new AskForFragment());
			break;

		case R.id.mind_sure:
			if(!companyPage){
				//个人信息验证
				if(!checkPersonInfo()){		break;		}
			}else{
				if(!checkCompanyInfo()){	break;		}
			}
			
			new AlertDialog.Builder(this.getActivity())
					.setTitle("提示")
					.setMessage("请确认您填写的信息是真实、有效的")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									sureDialog();
								}
							}).setNegativeButton("取消", null).show();
			break;

		case R.id.dialog_sure_btn:
			if (dialog != null)
				dialog.dismiss();
			Util.imgPath.clear();
			Util.orderdto = new OrderDto();
			Util.inorderdto = new InOrderDto();
			Jpage(new IndexFragment());
			break;

		case R.id.page_upload_1:
			type = "idCardFrontImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload_2:
			type = "idCardBackImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload_3:
			type = "idCardHandImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload1:
			type = "businessLicensePath";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload2:
			type = "lastYearApplicationFormPath";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload_11:
			type = "idCardFrontImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload_22:
			type = "idCardBackImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		case R.id.page_upload_33:
			type = "idCardHandImg";
			startActivityForResult(new Intent(this.getActivity(),
					SelectPicActivity.class), 3);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String picPath = null;
		String requestURL = Config.Url + "common/statics/upload";
		if (resultCode == Activity.RESULT_OK && requestCode == 3) {
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
		}
		if (picPath != null) {
			String fileKey = "pic";
			UploadUtil.uploadFile(picPath, fileKey, requestURL,
					this.getActivity(), type);
		} else {
			Util.showMsg(this.getActivity(), "请选择上传文件！");
		}
	}

	public void showdialog() {
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_sure, null);
		layout.findViewById(R.id.dialog_sure_btn).setOnClickListener(this);
		dialog = new AlertDialog.Builder(this.getActivity()).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void checkType() {
		if (Util.userlist.size() != 0) {
			if (Util.userlist.get(3).equals("40"))
				isPerson = true;
			else
				isPerson = false;
		}
	}

	void sureDialog() {
		Util.showProssbar(this.getActivity());
		creatMsg(1, 0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				contentlay.addView(showView(R.layout.page_person_fragment));
				Util.closeProssbar();
				break;

			case 1:
				Util.bundle.clear();
				setDto();
				ResultDto repho = (ResultDto) NetWork.NetResult(
						"order/saveOrder/" + Util.params[0] + Util.params[1],
						ResultDto.class, Util.orderdto);
				if (repho != null) {
					if (repho.errorCode.equals("0")) {
						showdialog();
					} else
						Util.showMsg(AskFor2Fragment.this.getActivity(),
								repho.errorMsg);
				} else
					Util.showMsg(AskFor2Fragment.this.getActivity(),
							"服务器或网络异常！");
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
	
	//Start: New Info Validation, check every field, SUNZHE, 2017-03-04
	
	boolean checkPersonInfo() {
		if(person_name.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写承租人信息！");	
			person_name.requestFocus();
			return false;
		}
	
		if (person_phone.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写承租人手机信息！");	
			person_phone.requestFocus();
			return false;
		}	
		if(!Util.isMobileNo(person_phone.getText().toString())){
			Util.showMsg(this.getActivity(), "承租人手机号码验证失败！");
			person_phone.requestFocus();
			return false;
		}		
		if (person_email.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写Email信息！");	
			person_email.requestFocus();
			return false;
		}	
		if(!Util.isEmail(person_email.getText().toString())){
			Util.showMsg(this.getActivity(), "Email验证失败！");
			person_email.requestFocus();
			return false;
		}			
		if (person_uptxt1.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传证件正面照片！");			
			return false;
		}
		if (person_uptxt2.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传证件反面照片！");			
			return false;
		}	
		if (person_uptxt3.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传手持证件照片！");			
			return false;
		}	
		if (person_address.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写通讯地址信息！");	
			person_address.requestFocus();
			return false;
		}
		if (person_posital.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写邮编信息！");	
			person_posital.requestFocus();
			return false;
		}			
		if (person_posital.getText().toString().length() != 6)
		{
			Util.showMsg(this.getActivity(), "邮编为六位数字！");	
			person_posital.requestFocus();
			return false;
		}
		//联系人信息判断
		if (friend.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写联系人信息！");	
			friend.requestFocus();
			return false;
		}	
		if (friend.getText().toString().equals(person_name.getText().toString()))
		{
			Util.showMsg(this.getActivity(), "紧急联系人不能是承租人本人！");	
			friend.requestFocus();
			return false;
		}		
		if (relation.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写与联系人的关系！");	
			relation.requestFocus();
			return false;
		}
		if (fricall.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写联系人手机信息！");	
			fricall.requestFocus();
			return false;
		}	
		
		if (fricall.getText().toString().equals(person_phone.getText().toString()))
		{
			Util.showMsg(this.getActivity(), "紧急联系人手机号码不能与承租人手机号码相同！");	
			fricall.requestFocus();
			return false;
		}		
		return true;

	}		
	
	boolean checkCompanyInfo() {
		//企业信息验证	
		if (company_name.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写企业名称信息！");	
			company_name.requestFocus();
			return false;
		}
		if (company_address.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写企业地址信息！");	
			company_address.requestFocus();
			return false;
		}	
		if (company_linkaddress.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写通讯地址信息！");	
			company_linkaddress.requestFocus();
			return false;
		}	
		if (company_posital.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写邮编信息！");	
			company_posital.requestFocus();
			return false;
		}	
		if (company_posital.getText().toString().length() != 6)
		{
			Util.showMsg(this.getActivity(), "邮编为六位数字！");	
			company_posital.requestFocus();
			return false;
		}	
	
		if (company_busiuptxt.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传营业执照照片！");	
			return false;
		}
		if (company_formuptxt.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传纳税申请表照片！");	
			return false;
		}		
		
		if (company_legal.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写法人代表信息！");	
			company_legal.requestFocus();
			return false;
		}		
		if (company_legalcall.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写法人代表手机信息！");	
			company_legalcall.requestFocus();
			return false;
		}			
		if(!Util.isMobileNo(company_legalcall.getText().toString())){
			Util.showMsg(this.getActivity(), "法人代表手机号码验证失败！");
			company_legalcall.requestFocus();
			return false;
		}		
		if (company_legal_email.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写Email信息！");	
			company_legal_email.requestFocus();
			return false;
		}	
		if(!Util.isEmail(company_legal_email.getText().toString())){
			Util.showMsg(this.getActivity(), "Email验证失败！");
			company_legal_email.requestFocus();
			return false;
		}			
		
		if (company_certiuptxt1.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传法人证件正面照片！");	
			return false;
		}
		if (company_certiuptxt2.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上法人证件反面照片！");	
			return false;
		}			
		if (company_certiuptxt3.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请上传法人手持证件照片！");	
			return false;
		}		

		//联系人信息判断
		if (friend.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写联系人信息！");	
			friend.requestFocus();
			return false;
		}	
		if (friend.getText().toString().equals(company_legal.getText().toString()))
		{
			Util.showMsg(this.getActivity(), "紧急联系人不能是法人代表本人！");	
			friend.requestFocus();
			return false;
		}		
		if (relation.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写与联系人的关系！");	
			relation.requestFocus();
			return false;
		}
		if (fricall.getText().toString().equals(""))
		{
			Util.showMsg(this.getActivity(), "请填写联系人手机信息！");	
			fricall.requestFocus();
			return false;
		}	
		if (fricall.getText().toString().equals(company_legalcall.getText().toString()))
		{
			Util.showMsg(this.getActivity(), "紧急联系人手机号码不能与法人代表手机号码相同！");	
			fricall.requestFocus();
			return false;
		}		
		return true;
	}		
	//End: Check PhoneNo and Email, SUNZHE, 2017-03-04	
}
