package com.example.mispubs.ui.Pubs;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mispubs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MFabButtons {

    private FloatingActionButton fab_button_1, fab_button_2, fab_button_3;
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    private int fabTranslationY = 100;
    private boolean isFabMenuOpen = false;
    private int speed = 250;
    private Context context;
    private FragmentManager fm;
    private FragmentPubs fragmentPubs;


    public MFabButtons(Context context, FloatingActionButton fab_button_1, FloatingActionButton fab_button_2, FloatingActionButton fab_button_3, FragmentManager fm, FragmentPubs fragmentPubs) {
        this.fab_button_1 = fab_button_1;
        this.fab_button_2 = fab_button_2;
        this.fab_button_3 = fab_button_3;
        this.context = context;
        this.fm = fm;
        this.fragmentPubs = fragmentPubs;

        setFabTranslationY();
        setFabClicks();
    }

    private void setFabTranslationY() {
        fab_button_2.setAlpha(0f);
        fab_button_3.setAlpha(0f);

        fab_button_2.setTranslationY(fabTranslationY);
        fab_button_3.setTranslationY(fabTranslationY);

    }

    private void setFabClicks() {
        fab_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
            }
        });

        fab_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                FragmentDetallePubs detallePubs = new FragmentDetallePubs();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.nav_host_fragment,detallePubs );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fab_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                fragmentPubs.activarControlVoz();

            }
        });

    }

    private void fabMenuOpen() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(true);
        fabOpenAnimation(fab_button_2);
        fabOpenAnimation(fab_button_3);

    }

    private void fabMenuClose() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(false);
        fabCloseAnimation(fab_button_2);
        fabCloseAnimation(fab_button_3);

    }

    private void fabCloseAnimation(FloatingActionButton fab) {
        fab.animate().translationY(fabTranslationY).alpha(0f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabOpenAnimation(FloatingActionButton fab) {
        fab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabMainAnimation(boolean isOpen) {
        if (isOpen) {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(45f).setDuration(speed).start();
        } else {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(0f).setDuration(speed).start();
        }
    }

    private void menuCheck() {
        if (isFabMenuOpen) {
            fabMenuClose();
        } else {
            fabMenuOpen();
        }
    }






}