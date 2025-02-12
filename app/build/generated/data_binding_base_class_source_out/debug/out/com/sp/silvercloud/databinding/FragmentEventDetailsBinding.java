// Generated by view binder compiler. Do not edit!
package com.sp.silvercloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sp.silvercloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentEventDetailsBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final TextView attendanceStatus;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final TextView dateLabelTextView;

  @NonNull
  public final TextView dateTextView;

  @NonNull
  public final TextView descriptionTextView;

  @NonNull
  public final EditText eventCodeEditText;

  @NonNull
  public final TextView eventCodeTextView;

  @NonNull
  public final ImageView eventImageView;

  @NonNull
  public final TextView interestLabelTextView;

  @NonNull
  public final TextView interestTextView;

  @NonNull
  public final Button joinEventBtn;

  @NonNull
  public final Button submitAttendanceBtn;

  @NonNull
  public final TextView titleTextView;

  private FragmentEventDetailsBinding(@NonNull NestedScrollView rootView,
      @NonNull TextView attendanceStatus, @NonNull ImageButton backButton,
      @NonNull TextView dateLabelTextView, @NonNull TextView dateTextView,
      @NonNull TextView descriptionTextView, @NonNull EditText eventCodeEditText,
      @NonNull TextView eventCodeTextView, @NonNull ImageView eventImageView,
      @NonNull TextView interestLabelTextView, @NonNull TextView interestTextView,
      @NonNull Button joinEventBtn, @NonNull Button submitAttendanceBtn,
      @NonNull TextView titleTextView) {
    this.rootView = rootView;
    this.attendanceStatus = attendanceStatus;
    this.backButton = backButton;
    this.dateLabelTextView = dateLabelTextView;
    this.dateTextView = dateTextView;
    this.descriptionTextView = descriptionTextView;
    this.eventCodeEditText = eventCodeEditText;
    this.eventCodeTextView = eventCodeTextView;
    this.eventImageView = eventImageView;
    this.interestLabelTextView = interestLabelTextView;
    this.interestTextView = interestTextView;
    this.joinEventBtn = joinEventBtn;
    this.submitAttendanceBtn = submitAttendanceBtn;
    this.titleTextView = titleTextView;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentEventDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentEventDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_event_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentEventDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.attendanceStatus;
      TextView attendanceStatus = ViewBindings.findChildViewById(rootView, id);
      if (attendanceStatus == null) {
        break missingId;
      }

      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.dateLabelTextView;
      TextView dateLabelTextView = ViewBindings.findChildViewById(rootView, id);
      if (dateLabelTextView == null) {
        break missingId;
      }

      id = R.id.dateTextView;
      TextView dateTextView = ViewBindings.findChildViewById(rootView, id);
      if (dateTextView == null) {
        break missingId;
      }

      id = R.id.descriptionTextView;
      TextView descriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (descriptionTextView == null) {
        break missingId;
      }

      id = R.id.eventCodeEditText;
      EditText eventCodeEditText = ViewBindings.findChildViewById(rootView, id);
      if (eventCodeEditText == null) {
        break missingId;
      }

      id = R.id.eventCodeTextView;
      TextView eventCodeTextView = ViewBindings.findChildViewById(rootView, id);
      if (eventCodeTextView == null) {
        break missingId;
      }

      id = R.id.eventImageView;
      ImageView eventImageView = ViewBindings.findChildViewById(rootView, id);
      if (eventImageView == null) {
        break missingId;
      }

      id = R.id.interestLabelTextView;
      TextView interestLabelTextView = ViewBindings.findChildViewById(rootView, id);
      if (interestLabelTextView == null) {
        break missingId;
      }

      id = R.id.interestTextView;
      TextView interestTextView = ViewBindings.findChildViewById(rootView, id);
      if (interestTextView == null) {
        break missingId;
      }

      id = R.id.joinEventBtn;
      Button joinEventBtn = ViewBindings.findChildViewById(rootView, id);
      if (joinEventBtn == null) {
        break missingId;
      }

      id = R.id.submitAttendanceBtn;
      Button submitAttendanceBtn = ViewBindings.findChildViewById(rootView, id);
      if (submitAttendanceBtn == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      return new FragmentEventDetailsBinding((NestedScrollView) rootView, attendanceStatus,
          backButton, dateLabelTextView, dateTextView, descriptionTextView, eventCodeEditText,
          eventCodeTextView, eventImageView, interestLabelTextView, interestTextView, joinEventBtn,
          submitAttendanceBtn, titleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
