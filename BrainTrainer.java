package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton,button0,button1,button2,button3,playAgain;
    TextView sumTextView;
    TextView timerTextView,resultTextView,scoreTextView;
    ArrayList<Integer> answers=new ArrayList <Integer>();
    int locationOfCorrectAnswer;
    int score=0;
    int numberOfQuestions=0;

    public void chooseAnswer(View view){
        if(Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())){
            resultTextView.setText("Correct!");
            score++;
        }
        else{
            resultTextView.setText("Wrong :(");
        }
        numberOfQuestions++;
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        newQuestion();
    }

    public void start(View view){
        goButton.setVisibility(view.INVISIBLE);
    }

    public void newQuestion(){
        Random random=new Random();

        int a=random.nextInt(29);
        int b=random.nextInt(29);
        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));

        locationOfCorrectAnswer=random.nextInt(4);

        answers.clear();
        for(int i=0;i<4;i++){
            if(i==locationOfCorrectAnswer){
                answers.add(a+b);
            }else{
                int wrongAnswer=random.nextInt(57);
                while(wrongAnswer==(a+b)){
                    wrongAnswer=random.nextInt(57);
                }
                answers.add(wrongAnswer);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));

    }

    public void startTimer(){
        CountDownTimer countDownTimer=new CountDownTimer(3100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                resultTextView.setText("Done!");
                playAgain.setVisibility(View.VISIBLE);
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                Toast toast=Toast.makeText(MainActivity.this, "You scored "+score+" out of "+numberOfQuestions, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();



            }
        }.start();
    }

    public void playAgain(View view){
        goButton.setVisibility(View.VISIBLE);
        score=0;
        numberOfQuestions=0;
        timerTextView.setText("30s");
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        playAgain.setVisibility(view.INVISIBLE);
        resultTextView.setText("");
        startTimer();
        newQuestion();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //goButton=findViewById(R.id.goButton);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        playAgain=findViewById(R.id.playAgain);
        sumTextView=findViewById(R.id.sumTextView);
        timerTextView=findViewById(R.id.timerTextView);
        resultTextView=findViewById(R.id.resultTextView);
        scoreTextView=findViewById(R.id.scoreTextView);

        goButton.setVisibility(View.VISIBLE);
        playAgain(findViewById(R.id.timerTextView));
        newQuestion();


    }
}
