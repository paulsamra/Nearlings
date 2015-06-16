package com.edbert.library.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;

public class DialogManager {

	public static AlertDialog createSingleButtonDialogWithIntent(final Context ctx,
			String buttonText, String title, String messageTxt,
			final Intent intent, final boolean finishCurrentActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								ctx.startActivity(intent);
								if(ctx instanceof Activity && finishCurrentActivity){
									Activity act = (Activity) ctx;
									act.finish();
								}
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	public static AlertDialog createDialogWithIntent(final Context ctx,
			String buttonText, String title, String messageTxt,
			final Intent intent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								ctx.startActivity(intent);
								if(ctx instanceof Activity){
									Activity act = (Activity) ctx;
									act.finish();
								}
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
	public static AlertDialog createDialogWithSingleButtonCustomClick(final Context ctx,
			String buttonText, String title, String messageTxt,
			DialogInterface.OnClickListener ocl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText,
						ocl);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}

	public static AlertDialog createOKDialogWithClicker(final Context ctx,
			String buttonText, String title, String messageTxt, DialogInterface.OnClickListener ocListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText,
						ocListener);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	public static AlertDialog.Builder partialBuild(Context ctx,
			String messageTxt, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt).setTitle(title).setCancelable(false);

		return builder;
	}

	public static AlertDialog createDialogWithCancel(final Context ctx,
			String buttonText1, String buttonText2, String title,
			String messageTxt, DialogInterface.OnClickListener ocListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText1, ocListener)
				.setNegativeButton(buttonText2,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();

							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}

	public static AlertDialog createDialogPositiveIntentNegativeOnClickListener(final Context ctx,
			String buttonPositive, String buttonNegative, String title,
			String messageTxt, final Intent intent, DialogInterface.OnClickListener ocListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setNegativeButton(buttonNegative, ocListener)
				.setPositiveButton(buttonPositive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								ctx.startActivity(intent);
								if(ctx instanceof Activity){
									Activity act = (Activity) ctx;
									act.finish();
								}
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	public static AlertDialog createDialogWithTwoIntent(final Context ctx,
			String buttonText1, final Intent intent1, String buttonText2,
			final Intent intent2, String title, String messageTxt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								ctx.startActivity(intent1);
								if(ctx instanceof Activity){
									Activity act = (Activity) ctx;
									act.finish();
								}
							}
						})
				.setNegativeButton(buttonText2,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								ctx.startActivity(intent2);
								if(ctx instanceof Activity){
									Activity act = (Activity) ctx;
									act.finish();
								}
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}

	public static AlertDialog showOkDialog(Context ctx, String buttonText,
			String messageTxt) {
		return showOkDialog(ctx, buttonText, "", messageTxt);
	}

	public static AlertDialog showOkDialog(Context ctx, String buttonText,
			String title, String messageTxt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(messageTxt)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(buttonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
}