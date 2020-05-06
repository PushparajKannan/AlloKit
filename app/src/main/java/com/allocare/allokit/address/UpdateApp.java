package com.allocare.allokit.address;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.allocare.allokit.R;

public class UpdateApp extends Dialog{



    public Context mContext;

    public CardView updateLay;

    public UpdateClick listner;

    public UpdateApp(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UpdateApp(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected UpdateApp(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    public void init(Context context) {
       this.mContext = context;
     //  this.dialog = new Dialog(this.mContext);
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       this.setContentView(R.layout.update_dialog);
        this.setCancelable(false);
       this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
       this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        updateLay = this.findViewById(R.id.updateLay);

        updateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listner!=null) {
                    listner.onClicked();
                }
               // dismiss();
            }
        });
   }


   public void setUpdateLayClick(UpdateClick lis) {
      this.listner = lis;
   }

   public interface UpdateClick {
       void onClicked();
   }



}
