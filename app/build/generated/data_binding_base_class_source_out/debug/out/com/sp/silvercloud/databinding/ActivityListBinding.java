// Generated by view binder compiler. Do not edit!
package com.sp.silvercloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sp.silvercloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityListBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView activityRecyclerView;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final EditText searchBar;

  private ActivityListBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView activityRecyclerView, @NonNull ConstraintLayout main,
      @NonNull EditText searchBar) {
    this.rootView = rootView;
    this.activityRecyclerView = activityRecyclerView;
    this.main = main;
    this.searchBar = searchBar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.activityRecyclerView;
      RecyclerView activityRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (activityRecyclerView == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.searchBar;
      EditText searchBar = ViewBindings.findChildViewById(rootView, id);
      if (searchBar == null) {
        break missingId;
      }

      return new ActivityListBinding((ConstraintLayout) rootView, activityRecyclerView, main,
          searchBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
