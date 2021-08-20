package com.amostrone.akash.spiderjump;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Game extends View {

    Rect enemy;
    Rect player;
    Rect top_bar;

    Paint paint_enemy;
    Paint paint_player;
    Paint paint_score;
    Paint paint_top_bar;

    static int score_val = 0;
    static int high_score_val = 0;

    static int enemy_X=0;
    static int player_Y=0;

    //MediaPlayer ring_hit= MediaPlayer.create(getContext(),R.raw.hit);
    MediaPlayer ring_background= MediaPlayer.create(getContext(),R.raw.background);
    //MediaPlayer ring_gameover= MediaPlayer.create(getContext(),R.raw.gameover);

    public Game(Context context) {
        super(context);
        player = new Rect();
        enemy = new Rect();
        top_bar = new Rect();
        paint_enemy = new Paint();
        paint_player = new Paint();
        paint_score = new Paint();
        paint_top_bar = new Paint();

        paint_top_bar.setColor(Color.GRAY);
        top_bar.bottom=150;
        top_bar.top=100;
        top_bar.left=0;
        top_bar.right=getWidth();

        paint_score.setColor(Color.GRAY);
        paint_score.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Background Music
        ring_background.start();

        int middle=getWidth()/2;
        int width=getWidth();
        int height=getHeight();

        enemy.bottom=height-25;
        enemy.top=height-75;
        enemy.left=width+25;
        enemy.right=width-25;
        canvas.drawRect(enemy,paint_enemy);

        paint_player.setColor(Color.GREEN);
        player.bottom=height-25;
        player.top=height-75;
        player.left=10;
        player.right=50;
        canvas.drawRect(player,paint_player);

        canvas.drawRect(top_bar,paint_top_bar);

        high_score_val=gethigh_score();
        canvas.drawText("High Score : "+high_score_val,middle-400,75,paint_score);

        canvas.drawText("Score : "+score_val,middle+200,75,paint_score);

        //When enemy and player collide, invert x direction motion
        //Increase Score
        //Increase speed
        if(Rect.intersects(enemy,player)) {
            score_val+=2;
            //dirY*=-1;

            //Collision music
            //ring_hit.start();

            //Increase Speed
            //enemy_move_x++;
            //enemy_move_y++;
        }

        //When enemy and top_bar collide, invert y direction motion
        if(Rect.intersects(enemy,top_bar)) {
            //dirY*=-1;
        }

        //enemy_movement();

        postInvalidate();
    }

    public void sethigh_score(int h){

        SharedPreferences sharedPref = getContext().getSharedPreferences("high_score",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int old_high_score = gethigh_score();

        if(h>old_high_score) {
            editor.putInt("high_score", h);
            editor.apply();
        }
    }

    public int gethigh_score(){

        SharedPreferences sharedPref = getContext().getSharedPreferences("high_score",MODE_PRIVATE);

        int defaultValue = 0;
        return sharedPref.getInt("high_score", defaultValue);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                //player_pos=(int) event.getX();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                //player_pos=(int) event.getX();
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        postInvalidate();

        return true;
    }

}

