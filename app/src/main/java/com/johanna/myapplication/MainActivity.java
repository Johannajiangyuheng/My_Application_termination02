package com.johanna.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "idnex";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private boolean mIsCheater;
    Button backButton;

    private TrueFalse[] mQustionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_text,true),
            new TrueFalse(R.string.question_text1,true),
            new TrueFalse(R.string.question_text2,false),
            new TrueFalse(R.string.question_text3,false),
            new TrueFalse(R.string.question_text4,true)

    };

    private int mCurrentIndex = 0;

    private void updateQuestion(){
        int question = mQustionBank[mCurrentIndex].getmQuestion();
        mQuestionTextView.setText(question);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        mTrueButton = (Button)findViewById(R.id.true_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex =(mCurrentIndex+1)%mQustionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CheatActivity.class);

                boolean answerIsTrue = mQustionBank[mCurrentIndex].ismTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,answerIsTrue);
                startActivityForResult(i,0);
            }
        });

        updateQuestion();


    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQustionBank[mCurrentIndex].ismTrueQuestion();

        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onsavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //return true; 直接返回true 能达到屏蔽改按键的功能，即在该Activity中该按键无法使用（在高版本中无法对Home键进行屏蔽）
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, "您点击了返回键", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
