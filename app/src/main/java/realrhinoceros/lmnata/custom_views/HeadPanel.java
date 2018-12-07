package realrhinoceros.lmnata.custom_views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import realrhinoceros.lmnata.MainActivity;
import realrhinoceros.lmnata.R;

public class HeadPanel extends RelativeLayout {
    private Context context;
    protected TextView sectorName;
    protected TextView sectorDescription;
    protected ImageButton optionsBtn;
    protected ImageButton homeBtn;
    protected PopupMenu popupMenu;

    Activity hpActivity;

    public HeadPanel(Context context, String name, String description, Activity activity) {
        super(context);
        this.context = context;
        initComponent();
        sectorName.setText(name);
        sectorDescription.setText(description);
        hpActivity = activity;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_head_panel, this);
        }
        sectorName = (TextView) findViewById(R.id.sector_name);
        sectorDescription = (TextView) findViewById(R.id.sector_descr);
        optionsBtn = (ImageButton) findViewById(R.id.option_btn);
        homeBtn = (ImageButton) findViewById(R.id.home_btn);

        initPopup(optionsBtn);

        optionsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        homeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
    }

    protected void goHome(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.back_message);

        builder.setPositiveButton(R.string.app_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(hpActivity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                hpActivity.startActivity(intent);
            }
        });

        builder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public String getNameSectorText(){
        return (String) sectorName.getText();
    }

    protected void initPopup(View v){
        popupMenu = new PopupMenu(this.context, optionsBtn);
        MenuInflater inflaterPopup = popupMenu.getMenuInflater();
        inflaterPopup.inflate(R.menu.options, popupMenu.getMenu());
    }

    public void setNameSectorText(String name){
        sectorName.setText(name);
    }

    public void setDescriptionSectorText(String description){
        sectorDescription.setText(description);
    }

    public void setOptionsBtnVisibility(int visibility){
        optionsBtn.setVisibility(visibility);
    }

    public void setOptionsListener(PopupMenu.OnMenuItemClickListener listener){
        popupMenu.setOnMenuItemClickListener(listener);
    }
}
