package com.wayyeasy.wayyeasy.Activities.Physicians;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static org.reactnative.maskedview.RNCMaskedView.getBitmapFromView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wayyeasy.wayyeasy.ComponentFiles.Constants.ConstantIDs;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.user.physicians_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityViewPrescriptionBinding;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewPrescriptionActivity extends AppCompatActivity {

    ActivityViewPrescriptionBinding prescriptionBinding;

    SharedPreferenceManager preferenceManager;
    physicians_response_model prescriptionData;

    private PdfDocument document;
    private int pdfHeight = 1080, pdfWidth = 720;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prescriptionBinding = ActivityViewPrescriptionBinding.inflate(getLayoutInflater());
        setContentView(prescriptionBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());

        prescriptionData = getIntent().getParcelableExtra(Constants.prescription);

        if (prescriptionData.getPrescription() != null && prescriptionData.getPrescription().size() > 0)
            for (int i = 0; i < prescriptionData.getPrescription().size(); i++) {
                addMedView(i + 1, prescriptionData.getPrescription().get(i).getMedType(), prescriptionData.getPrescription().get(i).getMedName(), prescriptionData.getPrescription().get(i).getMedDesc());
            }

        prescriptionBinding.doctorName.setText(getIntent().getStringExtra(Constants.physicianName) + "\n( " + getIntent().getStringExtra(Constants.speciality) + " )");
        prescriptionBinding.doctorId.setText(prescriptionData.getPhysicianId());
        prescriptionBinding.patientName.setText(preferenceManager.getString(Constants.name));
        prescriptionBinding.patientAge.setText(preferenceManager.getString(Constants.age));
        prescriptionBinding.patientGender.setText(preferenceManager.getString(Constants.sex));
        prescriptionBinding.consultationDate.setText(prescriptionData.getUpdatedAt());

        prescriptionBinding.downloadPrescription.setOnClickListener(v -> {
            if (checkPermissionForPDF()) {
                generatePDFFromView(prescriptionBinding.linearLayout);
            } else requestPermission();
        });
    }

    private void generatePDFFromView(View view) {
        Bitmap bitmap = getBitmapFromView(view);
        document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page myPage = document.startPage(pageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(myPage);
        CreateFile();
    }

    private void CreateFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "prescription.pdf");
        startActivityForResult(intent, ConstantIDs.CREATE_PRESCRIPTION_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantIDs.CREATE_PRESCRIPTION_PDF && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                if (document != null) {
                    ParcelFileDescriptor pfd = null;
                    try {
                        pfd = getContentResolver().openFileDescriptor(uri, "w");
                        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                        document.writeTo(fileOutputStream);
                        document.close();
                        Toast.makeText(this, "Pdf downloaded successfully.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        try {
                            DocumentsContract.deleteDocument(getContentResolver(), uri);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, ConstantIDs.PERMISSION_REQUEST_FOR_READ_WRITE);
    }

    private boolean checkPermissionForPDF() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void addMedView(int count, String medType, String medName, String medDesc) {
        View medicineView = getLayoutInflater().inflate(R.layout.single_medicine_layout, null, false);
        TextView slNo = medicineView.findViewById(R.id.sl_no);
        TextView medicineType = medicineView.findViewById(R.id.med_type);
        TextView medicineName = medicineView.findViewById(R.id.med_name);
        TextView medicineDesc = medicineView.findViewById(R.id.med_desc);

        slNo.setText(String.valueOf(count));
        medicineType.setText(medType);
        medicineName.setText(medName);
        medicineDesc.setText(medDesc);

        prescriptionBinding.medicineListLayout.addView(medicineView);
    }

    public void clearActivity(View view) {
        finish();
    }
}