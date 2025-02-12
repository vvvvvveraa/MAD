// Generated by view binder compiler. Do not edit!
package com.sp.silvercloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sp.silvercloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityOrganizerSignUpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final EditText emailInput;

  @NonNull
  public final TextView emailLabel;

  @NonNull
  public final LinearLayout formCard;

  @NonNull
  public final TextView loginRedirect;

  @NonNull
  public final EditText nameInput;

  @NonNull
  public final TextView nameLabel;

  @NonNull
  public final EditText passwordInput;

  @NonNull
  public final TextView passwordLabel;

  @NonNull
  public final Button signUpButton;

  @NonNull
  public final TextView signUpTitle;

  private ActivityOrganizerSignUpBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageButton backButton, @NonNull EditText emailInput, @NonNull TextView emailLabel,
      @NonNull LinearLayout formCard, @NonNull TextView loginRedirect, @NonNull EditText nameInput,
      @NonNull TextView nameLabel, @NonNull EditText passwordInput, @NonNull TextView passwordLabel,
      @NonNull Button signUpButton, @NonNull TextView signUpTitle) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.emailInput = emailInput;
    this.emailLabel = emailLabel;
    this.formCard = formCard;
    this.loginRedirect = loginRedirect;
    this.nameInput = nameInput;
    this.nameLabel = nameLabel;
    this.passwordInput = passwordInput;
    this.passwordLabel = passwordLabel;
    this.signUpButton = signUpButton;
    this.signUpTitle = signUpTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityOrganizerSignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityOrganizerSignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_organizer_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityOrganizerSignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.emailInput;
      EditText emailInput = ViewBindings.findChildViewById(rootView, id);
      if (emailInput == null) {
        break missingId;
      }

      id = R.id.emailLabel;
      TextView emailLabel = ViewBindings.findChildViewById(rootView, id);
      if (emailLabel == null) {
        break missingId;
      }

      id = R.id.formCard;
      LinearLayout formCard = ViewBindings.findChildViewById(rootView, id);
      if (formCard == null) {
        break missingId;
      }

      id = R.id.loginRedirect;
      TextView loginRedirect = ViewBindings.findChildViewById(rootView, id);
      if (loginRedirect == null) {
        break missingId;
      }

      id = R.id.nameInput;
      EditText nameInput = ViewBindings.findChildViewById(rootView, id);
      if (nameInput == null) {
        break missingId;
      }

      id = R.id.nameLabel;
      TextView nameLabel = ViewBindings.findChildViewById(rootView, id);
      if (nameLabel == null) {
        break missingId;
      }

      id = R.id.passwordInput;
      EditText passwordInput = ViewBindings.findChildViewById(rootView, id);
      if (passwordInput == null) {
        break missingId;
      }

      id = R.id.passwordLabel;
      TextView passwordLabel = ViewBindings.findChildViewById(rootView, id);
      if (passwordLabel == null) {
        break missingId;
      }

      id = R.id.signUpButton;
      Button signUpButton = ViewBindings.findChildViewById(rootView, id);
      if (signUpButton == null) {
        break missingId;
      }

      id = R.id.signUpTitle;
      TextView signUpTitle = ViewBindings.findChildViewById(rootView, id);
      if (signUpTitle == null) {
        break missingId;
      }

      return new ActivityOrganizerSignUpBinding((ConstraintLayout) rootView, backButton, emailInput,
          emailLabel, formCard, loginRedirect, nameInput, nameLabel, passwordInput, passwordLabel,
          signUpButton, signUpTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
