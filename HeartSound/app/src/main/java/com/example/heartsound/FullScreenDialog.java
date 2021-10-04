package com.example.heartsound;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class FullScreenDialog extends Dialog {

    private Context context;
    private ImageButton close;

    public FullScreenDialog(Context context) {
        super(context, R.style.MyFullScreenDialog);
        setContentView(R.layout.full_screen_dialog);
        this.context = context;

        init();
    }
    private void init() {
        close = (ImageButton)findViewById(R.id.close);
        close.setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}

