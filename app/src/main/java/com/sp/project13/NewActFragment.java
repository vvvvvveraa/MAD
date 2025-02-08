package com.sp.project13;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class NewActFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView text;
    private MaterialButton dialog_button;
    private ImageView imagePicker;
    private EditText activityNameEditText, activityCodeEditText, newDescriptionEditText;
    private MaterialAutoCompleteTextView interestAutoCompleteTextView;
    private TextInputLayout interestLayout;
    private Uri imageUri;
    private DatabaseReference rootDatabaseref;

    // New variable to store the selected date
    private String selectedDate;

    public NewActFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_act, container, false);

        // Initialize views
        text = view.findViewById(R.id.showText);
        dialog_button = view.findViewById(R.id.dialog_button);
        imagePicker = view.findViewById(R.id.image_picker);
        activityNameEditText = view.findViewById(R.id.activity_name);
        activityCodeEditText = view.findViewById(R.id.activity_code);
        newDescriptionEditText = view.findViewById(R.id.newDescription);
        interestAutoCompleteTextView = view.findViewById(R.id.interestTV);
        interestLayout = view.findViewById(R.id.interest_layout);
        MaterialButton submitButton = view.findViewById(R.id.submit_button);
        TextView errMsg = view.findViewById(R.id.errMsg);

        rootDatabaseref = FirebaseDatabase.getInstance().getReference("events");

        // Set up image picker
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // Set up date picker dialog
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        // Set up submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm(errMsg)) {
                    // Gather input data
                    String activityName = activityNameEditText.getText().toString().trim();
                    String activityCode = activityCodeEditText.getText().toString().trim();
                    String interest = interestAutoCompleteTextView.getText().toString().trim();
                    String newDescription = newDescriptionEditText.getText().toString().trim();

                    // Upload the image and save the event
                    uploadImage(imageUri, activityName, activityCode, interest, newDescription);
                } else {
                    errMsg.setText("Please fill all up");
                }
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imagePicker.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateForm(TextView errMsg) {
        boolean isValid = true;

        if (activityNameEditText.getText().toString().isEmpty()) {
            activityNameEditText.setError("Activity name is required");
            isValid = false;
        }

        if (activityCodeEditText.getText().toString().isEmpty()) {
            activityCodeEditText.setError("Activity code is required");
            isValid = false;
        }

        if (interestAutoCompleteTextView.getText().toString().isEmpty()) {
            interestLayout.setError("Please select an option");
            isValid = false;
        }

        if (newDescriptionEditText.getText().toString().isEmpty()) {
            newDescriptionEditText.setError("New description is required");
            isValid = false;
        }

        if (imageUri == null) {
            Toast.makeText(getActivity(), "Please upload an image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void openDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.Theme_Project13, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Format the date and store it in the selectedDate variable
                selectedDate = String.format(Locale.getDefault(), "%02d %s %d", day, getMonthString(month), year);
                text.setText(selectedDate);
            }
        }, year, month, day);

        dialog.show();
    }

    private String getMonthString(int month) {
        // Array of month names
        String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    // New method to upload the image and save the event
    private void uploadImage(Uri imageUri, String activityName, String activityCode, String interest, String newDescription) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique ID for the image
        String imageId = UUID.randomUUID().toString(); // Generate a unique ID

        // Create a reference for the image file
        StorageReference imageRef = storageRef.child("images/" + imageId + ".jpg"); // Use the imageId as part of the filename

        // Upload the image
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Create a unique key for the new event
                        String eventId = rootDatabaseref.push().getKey();

                        // Create an Event object with the selected date
                        Event newEvent = new Event(activityName, activityCode, selectedDate, interest, "Full description here", imageUrl, "organizer1", newDescription, imageId);

                        // Set the event in the database
                        rootDatabaseref.child(eventId).setValue(newEvent).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Event created successfully", Toast.LENGTH_SHORT).show();

                                // Create an Intent to start Submit_newAct
                                Intent intent = new Intent(getActivity(), Submit_newAct.class);
                                intent.putExtra("activityName", activityName); // Pass the activity name
                                intent.putExtra("eventId", eventId); // Optionally pass the event ID if needed
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Failed to create event", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(getActivity(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}