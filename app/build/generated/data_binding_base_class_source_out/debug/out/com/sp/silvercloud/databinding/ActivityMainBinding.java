// Generated by view binder compiler. Do not edit!
package com.sp.silvercloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sp.silvercloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigationView;

  @NonNull
  public final DrawerLayout drawerLayoutUser;

  @NonNull
  public final FrameLayout frameLayout;

  @NonNull
  public final NavigationView navViewUser;

  @NonNull
  public final Toolbar toolbarUser;

  private ActivityMainBinding(@NonNull DrawerLayout rootView,
      @NonNull BottomNavigationView bottomNavigationView, @NonNull DrawerLayout drawerLayoutUser,
      @NonNull FrameLayout frameLayout, @NonNull NavigationView navViewUser,
      @NonNull Toolbar toolbarUser) {
    this.rootView = rootView;
    this.bottomNavigationView = bottomNavigationView;
    this.drawerLayoutUser = drawerLayoutUser;
    this.frameLayout = frameLayout;
    this.navViewUser = navViewUser;
    this.toolbarUser = toolbarUser;
  }

  @Override
  @NonNull
  public DrawerLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottomNavigationView;
      BottomNavigationView bottomNavigationView = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigationView == null) {
        break missingId;
      }

      DrawerLayout drawerLayoutUser = (DrawerLayout) rootView;

      id = R.id.frame_layout;
      FrameLayout frameLayout = ViewBindings.findChildViewById(rootView, id);
      if (frameLayout == null) {
        break missingId;
      }

      id = R.id.nav_view_user;
      NavigationView navViewUser = ViewBindings.findChildViewById(rootView, id);
      if (navViewUser == null) {
        break missingId;
      }

      id = R.id.toolbar_user;
      Toolbar toolbarUser = ViewBindings.findChildViewById(rootView, id);
      if (toolbarUser == null) {
        break missingId;
      }

      return new ActivityMainBinding((DrawerLayout) rootView, bottomNavigationView,
          drawerLayoutUser, frameLayout, navViewUser, toolbarUser);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
