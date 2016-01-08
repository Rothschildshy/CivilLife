package com.CivilLife.Widget;

import com.example.post_get.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 自定义的弹出框
 * 
 * @author pwy
 * 
 */
public class AlertDialogEx extends Dialog {
	private Builder builder;

	public Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Builder builder) {
		this.builder = builder;
	}

	public AlertDialogEx(Context context, int theme) {
		super(context, theme);
	}

	public AlertDialogEx(Context context) {
		super(context);
	}

	@Override
	public void dismiss() {
		if (null != builder.clClose) {
			builder.clClose.onClick(null);
		}
		super.dismiss();
	};

	public static class Builder {
		private int titlesize;
		private int titlecolor;
		private int msgsize;
		private int msgcolor;
		private String title;
		private String msg; // 文本内容
		private View vMsg; // 视图内容，与上面不能共存
		private String positiveButtonName;
		private String negativeButtonName;
		private View.OnClickListener clPositive;
		private View.OnClickListener clNegative;
		private View.OnClickListener clClose;
		private boolean closeOnClickPbtn = true;
		private boolean closeOnClickNbtn = true;
		private String[] items;
		private OnItemClickListener iclItems;
		private boolean showCloseBtn = false;

		private Context context;
		private TextView tvMsg;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * 标题 内容 大小 颜色
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setTitleColor(int titlecolor) {
			this.titlecolor = titlecolor;
			return this;
		}

		public Builder setTitleSize(int titlesize) {
			this.titlesize = titlesize;
			return this;
		}

		/**
		 * 文本提示样式－－以下三种样式只能用一种，同时设置时的优先级按以下顺序递增，也就是自定义的最高
		 * 
		 * @param msg
		 * @return
		 */
		public Builder setMessage(String msg) {
			this.msg = msg;
			return this;
		}

		public Builder setMessageColor(int msgcolor) {
			this.msgcolor = msgcolor;
			return this;
		}

		public Builder setMessageSize(int msgsize) {
			this.msgsize = msgsize;
			return this;
		}

		/**
		 * 列表样式
		 * 
		 * @param items
		 *            列表项
		 * @param icl
		 * @return
		 */
		public Builder setItems(String[] items, OnItemClickListener icl) {
			this.items = items;
			this.iclItems = icl;
			return this;
		}

		/**
		 * 自定义样式
		 * 
		 * @param v
		 * @return
		 */
		public Builder setMessage(View v) {
			this.vMsg = v;
			return this;
		}

		/**
		 * 确定按钮
		 * 
		 * @param name
		 * @param cl
		 *            附加处理
		 * @param closeOnClick
		 *            事件处理后是否关闭对话框
		 * @return
		 */
		public Builder setPositiveButton(String name, View.OnClickListener cl, boolean closeOnClick) {
			this.positiveButtonName = name;
			this.clPositive = cl;
			this.closeOnClickPbtn = closeOnClick;
			return this;
		}

		/**
		 * 否定按钮
		 * 
		 * @param name
		 * @param cl
		 *            附加处理
		 * @param closeOnClick
		 *            事件处理后是否关闭对话框
		 * @return
		 */
		public Builder setNegativeButton(String name, View.OnClickListener cl, boolean closeOnClick) {
			this.negativeButtonName = name;
			this.clNegative = cl;
			this.closeOnClickNbtn = closeOnClick;
			return this;
		}

		/**
		 * 右上角关闭按钮附加处理
		 * 
		 * @param cl
		 * @return
		 */
		public Builder setCloseListener(View.OnClickListener cl) {
			clClose = cl;
			return this;
		}

		/**
		 * 是否显示右上角关闭按钮
		 * 
		 * @param show
		 * @return
		 */
		public Builder showCloseButton(boolean show) {
			showCloseBtn = show;
			return this;
		}

		@SuppressLint("NewApi")
		public AlertDialogEx create() {
			// instantiate the dialog with the custom Theme
			final AlertDialogEx dialog = new AlertDialogEx(context, R.style.dialogBaseTheme);
			dialog.setContentView(R.layout.alert_dialog_ex);
			dialog.setBuilder(this);

			// 标题
			TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
			View loTitle = dialog.findViewById(R.id.lo_title);
			if (null == title || "".equals("title")) { // 不设置就不显示
				loTitle.setVisibility(View.GONE);
			} else {
				loTitle.setVisibility(View.VISIBLE);
				tvTitle.setText(title);
			}
			// title设置颜色
			if (titlecolor != 0) {
				tvTitle.setTextColor(titlecolor);
			}
			// title设置颜色
			if (titlesize != 0) {
				tvTitle.setTextSize(titlesize);
			}
			tvMsg = (TextView) dialog.findViewById(R.id.tv_msg);
			// Msg设置颜色
			if (msgcolor != 0) {
				tvMsg.setTextColor(msgcolor);
			}
			// Msg设置颜色
			if (msgsize != 0) {
				tvMsg.setTextSize(msgsize);
			}
			ListView lv = (ListView) dialog.findViewById(R.id.lv_content);
			LinearLayout loContent = (LinearLayout) dialog.findViewById(R.id.lo_view);
			if (null != vMsg) { // 优先显示自定义样式
				loContent.setVisibility(View.VISIBLE);
				loContent.addView(vMsg);
				tvMsg.setVisibility(View.GONE);
				lv.setVisibility(View.GONE);
			} else if (null != items) {
				loContent.setVisibility(View.GONE);
				tvMsg.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.alert_dialog_ex_item,
						R.id.tv_msg);
				adapter.addAll(items);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (null != iclItems) {
							iclItems.onItemClick(arg0, arg1, arg2, arg3);
						}
						dialog.dismiss(); // 这种样式为单选式，选择后自动关闭对话框
					}
				});
			} else {
				loContent.setVisibility(View.GONE);
				tvMsg.setVisibility(View.VISIBLE);
				lv.setVisibility(View.GONE);
				tvMsg.setText(msg);
			}

			if (null == positiveButtonName && null == negativeButtonName) { // 不设置就不显示
				LinearLayout loBtn = (LinearLayout) dialog.findViewById(R.id.lo_btn);
				loBtn.setVisibility(View.GONE);
			} else {
				// 肯定按钮
				Button btnOk = (Button) dialog.findViewById(R.id.btn_positive);
				if (null == positiveButtonName) {
					btnOk.setVisibility(View.GONE);
				} else {
					btnOk.setVisibility(View.VISIBLE);
					btnOk.setText(positiveButtonName);
					btnOk.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View paramView) {
							// TODO Auto-generated method stub
							if (null != clPositive) {
								clPositive.onClick(paramView);
							}
							if (closeOnClickPbtn) {
								dialog.dismiss();
							}
						}
					});
				}

				// 否定按钮
				Button btnCancel = (Button) dialog.findViewById(R.id.btn_negative);
				if (null == negativeButtonName) {
					btnCancel.setVisibility(View.GONE);
				} else {
					btnCancel.setVisibility(View.VISIBLE);
					btnCancel.setText(negativeButtonName);
					btnCancel.setOnClickListener(clNegative);
					btnCancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View paramView) {
							// TODO Auto-generated method stub
							if (null != clNegative) {
								clNegative.onClick(paramView);
							}
							if (closeOnClickNbtn) {
								dialog.dismiss();
							}
						}
					});
				}
			}

			return dialog;
		}
		
		public Builder show() {
			AlertDialogEx dlg = create();
			dlg.setCancelable(false);
			dlg.show(); 
			return  this;
		}

		public void shows() {
			AlertDialogEx dlg = create();
			dlg.setCancelable(false);
			dlg.show();
		}
	}
}
