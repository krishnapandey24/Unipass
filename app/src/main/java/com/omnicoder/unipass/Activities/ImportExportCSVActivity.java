package com.omnicoder.unipass.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.Others.CSV.CsvReader;
import com.omnicoder.unipass.Others.CSV.CsvRow;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ImportExportCSVActivity extends AppCompatActivity {
	Dialog loadingDialog,confirmPassword;
	TextInputLayout textInputLayout;
	EditText editText;
	MyApplication myApplication;
	DoClass Do;
	DBHandler db;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export_csv);
		Log.d("mylife", "onCreate: ");
		Do= new DoClass();
		db= new DBHandler(ImportExportCSVActivity.this);
		myApplication= (MyApplication) getApplicationContext();
		AlertDialog.Builder importErrorDialog= new AlertDialog.Builder(ImportExportCSVActivity.this);
		importErrorDialog.setTitle("Something went wrong!");
		importErrorDialog.setMessage("Please make sure the selected file is a valid exported file from Unipass.");
		importErrorDialog.setCancelable(false);
		importErrorDialog.setPositiveButton("EXPORT CSV", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog.Builder exportErrorDialog= new AlertDialog.Builder(ImportExportCSVActivity.this);
		exportErrorDialog.setTitle("Something went wrong!");
		exportErrorDialog.setMessage("Please try again...");
		exportErrorDialog.setCancelable(false);
		exportErrorDialog.setPositiveButton("EXPORT CSV", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		//Exporting......

		confirmPassword= Do.createDialog(ImportExportCSVActivity.this,confirmPassword,R.layout.delete_all_dialog_2);
		confirmPassword.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		textInputLayout=confirmPassword.findViewById(R.id.textInputLayout);
		editText= confirmPassword.findViewById(R.id.editText);
		TextView cancel = confirmPassword.findViewById(R.id.cancelButton);
		TextView confirm = confirmPassword.findViewById(R.id.confirmButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmPassword.dismiss();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("imposter", "onClick: ");
				if(new MyApplication().confirmPassword(ImportExportCSVActivity.this,editText.getText().toString())){
					Log.d("imposter", "onClick:vIN ");
					confirmPassword.dismiss();
					loadingDialog.show();
					try {
						exportCSV(editText.getText().toString());
						Log.d("imposter", "onClick: Done ");
						Toast.makeText(ImportExportCSVActivity.this,"Data Exported Successfully",Toast.LENGTH_SHORT).show();


					} catch (Exception e) {
						e.printStackTrace();
						AlertDialog alertDialog= exportErrorDialog.create();
						alertDialog.show();

					}
					loadingDialog.dismiss();

				}
				else {
					textInputLayout.setError("Incorrect Password");

				}
			}
		});

		loadingDialog= new Dialog(this);
		loadingDialog.setContentView(R.layout.exporting_vault_dialog);
		loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_bg));
		loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		loadingDialog.setCancelable(false);
		textInputLayout= findViewById(R.id.textInputLayout);
		Button exportButton= findViewById(R.id.exportButton);
		exportButton.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			confirmPassword.show();
		}
		});

		Button importButton= findViewById(R.id.importButton);
		importButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CSVFiles", "Clicked ");
				Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				startActivityForResult(intent,10);
              	Toast.makeText(ImportExportCSVActivity.this,"Select a Import File",Toast.LENGTH_SHORT).show();

			}
		});


	
    }
    private void exportCSV(String sprf) throws Exception {
		Log.d("mylife", "exportCSV: insidefuncetion");
		File Folder = new File(Environment.getExternalStorageDirectory(), "Unipass");
		File CSVFile = new File(Folder.getAbsolutePath(), "unipass_export.csv");
		if (!Folder.exists()) {
			Folder.mkdirs();
		}

		FileWriter csvWriter= new FileWriter(CSVFile);
		Cursor cursor = db.displayItem();
		cursor.moveToNext();
		csvWriter.append("Category");
		csvWriter.append(",");
		csvWriter.append("Title");
		csvWriter.append(",");
		csvWriter.append("Username/Email");
		csvWriter.append(",");
		csvWriter.append("Password");
		csvWriter.append(",");
		csvWriter.append("URL");
		csvWriter.append(",");
		csvWriter.append("Created");
		csvWriter.append(",");
		csvWriter.append("Modified");
		csvWriter.append(",");
		csvWriter.append("Notes");
		csvWriter.append("\n");
		while (cursor.moveToNext()) {
			String Category= cursor.getString(8);
			String title = cursor.getString(1);
			String username = cursor.getString(2);
			String password= cursor.getString(3);
			String url= cursor.getString(4);
			String created= cursor.getString(6);
			String modified= cursor.getString(7);
			String notes=cursor.getString(5);
			try {
				Log.d("mylife", "PASSWORD: "+password+"sprf: "+sprf);
				password= Crypter.decrypt(password,sprf);
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("mylife", "exportCSV: "+e.getMessage());
				password= "ERROR";
			}
			String appendString= '"'+Category+'"'+","+'"'+title+'"'+","+'"'+username+'"'+","+'"'+password+'"'+","+'"'+url+'"'+","+'"'+created+'"'+","+'"'+modified+'"'+","+'"'+notes+'"';
			Log.d("mylife", "exportCSV: "+ appendString);

			csvWriter.append(appendString);
			csvWriter.append("\n");
		}
		csvWriter.flush();
		csvWriter.close();


	}


	public String getFilePath(Intent data) {
		Uri uri= data.getData();
		String fullPath= uri.getPath();
		Log.d("path","full path "+fullPath);
		int dotIndex= fullPath.lastIndexOf(":");
		String folderName="";
		if(dotIndex>0) {
			folderName = fullPath.substring(dotIndex + 1);
		}
		return Environment.getExternalStorageDirectory()+"/" + folderName;



	}
	private void readCSV(File file){
		try {
			try (CsvReader csvReader = CsvReader.builder().build(Paths.get(file.getPath()), UTF_8)) {
				int count=1;
				for (CsvRow row : csvReader) {
					if(count>1) {
						String category = row.getField(0);
						String title = row.getField(1);
						String username = row.getField(2);
						String password = row.getField(3);
						String url = row.getField(4);
						String created = row.getField(5);
						String modified = row.getField(6);
						String notes = row.getField(7);
						try {
							password = Crypter.encrypt(myApplication.getSPRF(), password);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(ImportExportCSVActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
						}
						ModelClass modelClass = new ModelClass(title, username, password, url, notes, created, modified, category, "false");
						db.addLogin(modelClass);
					}
					count++;
				}


			}
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==10){
			String path = getFilePath(data);
			String[] arr= path.split("\\.");
			int index = (arr.length)-1;
			if(arr[index].equals("csv")) {
				File CSVFile = new File(path);
				readCSV(CSVFile);
				Toast.makeText(ImportExportCSVActivity.this, "Data Imported Successfully", Toast.LENGTH_SHORT).show();
			}
			else {
				Log.d("dialog","Invalid Database file!");
				Toast.makeText(ImportExportCSVActivity.this, "Invalid file!", Toast.LENGTH_SHORT).show();
			}
		}
	}






}