package com.marco.socketadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tags extends AppCompatActivity {

    private EditText etApplianceName, edTimeout;
    private ImageView ivLock;
    private ImageButton ibAddTag;
    private TagAdapter tagAdapter;
    private RecyclerView tagRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Model> tagList = new ArrayList<>();
    private Model model;
    private Integer lockImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        // View object instance
        etApplianceName = findViewById(R.id.et_appliance_name);
        edTimeout = findViewById(R.id.et_timeout);
        ivLock = findViewById(R.id.iv_lock);
        //set tag green button as default
        ivLock.setTag(R.mipmap.green_button);
        ibAddTag = findViewById(R.id.image_button_add_tag);
        tagRecyclerView = findViewById(R.id.rv_tag);
        tagAdapter = new TagAdapter(tagList);



        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        tagRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        tagRecyclerView.setLayoutManager(mLayoutManager);

        tagRecyclerView.setAdapter(tagAdapter);

        ibAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fieldNameAppliance = String.valueOf(etApplianceName.getText());
                String fieldTimeout = String.valueOf(edTimeout.getText());
                if(fieldNameAppliance.isEmpty() || fieldTimeout.isEmpty()){
                    Toast.makeText(Tags.this, "Field APPLIANCE or field TIMEOUT can not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    //instance of Model class - gettins/Settings
                    model=new Model();
                    model.setTagID("7");
                    model.setTagApplianceName(fieldNameAppliance);
                    model.setTagTimeout(fieldTimeout);

                    lockImageID =getImageID(ivLock);
                    switch(lockImageID) {
                        case R.mipmap.green_button:
                            model.setTagLock("No");
                            //model.setTagTR_On(R.mipmap.green_button);
                            break;
                        case R.mipmap.red_button:
                            model.setTagLock("Yes");
                            //model.setTagTR_On(R.mipmap.red_button);
                            break;
                    }

                    tagList.add(model);
                    tagAdapter.notifyDataSetChanged();
                    Toast.makeText(Tags.this, "Tag Add Successful!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ivLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                lockImageID=getImageID(v);

                switch(lockImageID) {
                    case R.mipmap.green_button:
                        imageView.setImageResource(R.mipmap.red_button);
                        imageView.setTag(R.mipmap.red_button);
                        break;
                    case R.mipmap.red_button:
                        imageView.setImageResource(R.mipmap.green_button);
                        imageView.setTag(R.mipmap.green_button);
                        break;
                }

            }
        });

        loadTagInfo();
    }

    private Integer getImageID(View v) {
        lockImageID = (Integer) v.getTag();
        lockImageID = lockImageID == null ? 0 : lockImageID;

        return lockImageID;
    }

    private void loadTagInfo() {

        model = new Model();
        model.setTagID("2");
        model.setTagApplianceName("Heater");
        model.setTagLock("No");
        model.setTagTimeout("25");
        tagList.add(model);

        model.setTagID("5");
        model.setTagApplianceName("Iron");
        model.setTagLock("Yes");
        model.setTagTimeout("20");
        tagList.add(model);

        tagAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
