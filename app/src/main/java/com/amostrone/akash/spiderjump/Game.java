package com.amostrone.akash.spiderjump;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class Game extends View {

    Rect enemy;
    Rect player;

    Paint paint_enemy;
    Paint paint_player;
    Paint paint_score;

    static int score_val = -2;
    static int high_score_val = 0;

    int speed_enemy=2;
    int enemy_X=0;
    int player_Y=0;

    boolean clicked=false;
    int[] drawable_enemy_ship = {R.drawable.ship1, R.drawable.ship2, R.drawable.ship3, R.drawable.ship4, R.drawable.ship5};
    int random_enemy_drawable = ThreadLocalRandom.current().nextInt(0, 4 + 1);

    MediaPlayer ring_background= MediaPlayer.create(getContext(),R.raw.background);

    public Game(Context context) {
        super(context);
        player = new Rect();
        enemy = new Rect();
        paint_enemy = new Paint();
        paint_player = new Paint();
        paint_score = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int middle=getWidth()/2;
        int width=getWidth();
        int height=getHeight();

        //Background Music
        //TODO start the music
        ring_background.start();

        if(player_Y==0)player_Y=height/2;
        else if(player_Y!=height/2)player_Y+=2;
        if(player_Y<height/2-160){
            player_Y=height/2-160;
            clicked=false;
        }
        if(clicked){
            player_Y-=4;
        }

        if(enemy_X<0){
            enemy_X=getWidth();
            random_enemy_drawable= ThreadLocalRandom.current().nextInt(0, 4 + 1);
            score_val+=2;
            speed_enemy++;
        }
        else{
            enemy_X-=speed_enemy;
        }
        paint_enemy.setColor(Color.RED);
        enemy.bottom=height/2-15;
        enemy.top=height/2-85;
        enemy.left=enemy_X-110;
        enemy.right=enemy_X-40;
        Drawable enmy = getResources().getDrawable(drawable_enemy_ship[random_enemy_drawable], null);
        enmy.setBounds(enemy.left, enemy.top, enemy.right, enemy.bottom);
        enmy.draw(canvas);
        //canvas.drawRect(enemy,paint_enemy);

        paint_player.setColor(Color.GREEN);
        player.bottom=player_Y-15;
        player.top=player_Y-85;
        player.left=30;
        player.right=100;
        Drawable plyr = getResources().getDrawable(R.drawable.player, null);
        plyr.setBounds(20, player_Y-80, 150, player_Y);
        plyr.draw(canvas);
        //canvas.drawCircle(50, player_Y-40, 40, paint_player);
        //canvas.drawRect(player,paint_player);

        paint_score.setColor(Color.RED);
        paint_score.setTextSize(45);
        high_score_val=gethigh_score();
        paint_score.setAntiAlias(true);
        paint_score.setUnderlineText(true);
        paint_score.setFakeBoldText(true);
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
        paint_score.setTypeface(typeface);
        canvas.drawText("High Score : "+high_score_val,middle-400,75,paint_score);
        canvas.drawText("Score : "+score_val,middle+200,75,paint_score);

        //When enemy and player collide, Game Over
        if(Rect.intersects(enemy,player)) {
            Toast.makeText(getContext(), "Game Over, Your Score is "+score_val, Toast.LENGTH_SHORT).show();
            sethigh_score(score_val);
            speed_enemy=2;
            score_val=0;

            enemy_X=width;

            //Collision music
            //ring_hit.start();
        }

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
                clicked=true;
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

