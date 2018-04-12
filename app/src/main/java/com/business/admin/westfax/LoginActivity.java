package com.business.admin.westfax;





import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.VerticalTextView;

import java.util.Map;
/**
 * Created by SONY on 20-01-2018.
 */

public class LoginActivity extends AppCompatActivity {
//        implements View.OnClickListener{
    VerticalTextView txtSignIn, txtRegister;
    LinearLayout llSignIn, llRegister;
//    Button btnRegister, btnSignIn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.log_regiser);
//        llSignIn = (LinearLayout) findViewById(R.id.llSign_in);
//        llRegister = (LinearLayout) findViewById(R.id.llRegister);
//
//        txtRegister = (VerticalTextView) findViewById(R.id.txtRegister);
//        txtSignIn = (VerticalTextView) findViewById(R.id.txtSignIn);
//
//
//        txtSignIn.setOnClickListener(this);
//        txtRegister.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.txtSignIn:
//                hideSoftKeyBoard();
//                showSignInForm();
//                break;
//            case R.id.txtRegister:
//                hideSoftKeyBoard();
//                showRegisterForm();
//                break;
//        }
//    }
//
//    private void showSignInForm() {
//        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llRegister.getLayoutParams();
//        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
//        infoLogin.widthPercent = 0.15f;
//        llRegister.requestLayout();
//
//
//        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignIn.getLayoutParams();
//        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
//        infoSignup.widthPercent = 0.85f;
//        llSignIn.requestLayout();
//
//        txtRegister.setVisibility(View.VISIBLE);
//        txtSignIn.setVisibility(View.GONE);
//        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
//        llSignIn.startAnimation(translate);
//
//
//    }
//
//    private void showRegisterForm() {
//        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignIn.getLayoutParams();
//        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
//        infoLogin.widthPercent = 0.15f;
//        llSignIn.requestLayout();
//
//
//        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llRegister.getLayoutParams();
//        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
//        infoSignup.widthPercent = 0.85f;
//        llRegister.requestLayout();
//
//        txtRegister.setVisibility(View.GONE);
//        txtSignIn.setVisibility(View.VISIBLE);
//        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
//        llRegister.startAnimation(translate);
//    }
//
//    private void hideSoftKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//
//        if (imm.isAcceptingText()) {
//            // verify if the soft keyboard is open
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
    }

}
