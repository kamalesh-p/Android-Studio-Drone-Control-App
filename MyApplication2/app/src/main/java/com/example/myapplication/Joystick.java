package com.example.myapplication;

import static android.view.MotionEvent.ACTION_POINTER_UP;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

class Joystick {
    JoystickControl joystickControl = null;
    Potentiometer potentiometer = null;
    Button layout;
    UDP udp;
    boolean isJoystick;

    int getAngle(double x, double y) {
        int angle = (int) Math.toDegrees(Math.atan2(y, x));
        if (angle < 0) {
            return angle + 360;
        }
        return angle;
    }

    View findViewById(int value) {
        if (joystickControl != null)
            return joystickControl.findViewById(value);
        else if (potentiometer != null)
            return potentiometer.findViewById(value);
        else
            return null;
    }

    public void init(JoystickControl joystickControl) {
        this.joystickControl = joystickControl;
        this.layout = joystickControl.layout;
        this.udp = joystickControl.udp;
        isJoystick = false;
    }

    public void init(Potentiometer potentiometer) {
        this.potentiometer = potentiometer;
        this.layout = potentiometer.layout;
        this.udp = potentiometer.udp;
        isJoystick = true;
    }

    public class Move {
        int old_value;
        ImageView color;
        ImageView white;
        RelativeLayout ball;
        TextView text;

        void init(int color, int white, int ball, int text) {
            this.color = (ImageView) findViewById(color);
            this.white = (ImageView) findViewById(white);
            this.ball = (RelativeLayout) findViewById(ball);
            this.text = (TextView) findViewById(text);
        }

        void origin() {
            float centerX = (float) (color.getWidth() / 2) + color.getX();
            float centerY = (float) (color.getHeight() / 2) + color.getY();

            ball.setX(centerX - (float) (ball.getWidth() / 2));
            ball.setY(centerY - (float) (ball.getHeight() / 2));

            analyse();
        }

        void setXY(int x, int y) {
            int xPos, yPos;
            int xMax = move.color.getWidth() + move.color.getLeft();
            int xMin = move.color.getLeft();
            int yMax = move.color.getHeight() + move.color.getHeight() / 4;
            int yMin = move.color.getTop() - move.color.getHeight() / 4;
            if (x > xMax) {
                // right most
                xPos = xMax;
                ball.setX(xMax - ball.getWidth() / 2);
            } else if (x < xMin) {
                // left most
                xPos = xMin;
                ball.setX(xMin - ball.getWidth() / 2);
            } else {
                // center
                xPos = x;
                ball.setX(x - ball.getWidth() / 2);
            }
            if (y > yMax) {
                // bottom most
                yPos = yMax;
                ball.setY(yMax);
            } else if (y < yMin) {
                // top most
                yPos = yMin;
                ball.setY(yMin);
            } else {
                // center
                yPos = y;
                ball.setY(y);
            }
            analyse(xPos, yPos);
        }

        void analyse(int x, int y) {
            int centerX = (int) ((color.getWidth() / 2) + color.getX());
            int centerY = (int) ((color.getHeight() / 2) + color.getY());
            int tempX = x - centerX;
            int tempY = (int) (centerY - y - layout.getY());
            int radius = (int) Math.sqrt(tempX * tempX + tempY * tempY);
            int angle = getAngle(tempX, tempY);
            if (radius < 40) {
                analyse();
            } else {
                int new_value;
                //45, 135, 225, 315
                if (45 <= angle && angle < 135) {
                    new_value = 1;
                    if (old_value != new_value)
                        text.setText("Front(1)");
                } else if (135 <= angle && angle < 225) {
                    new_value = 2;
                    if (old_value != new_value)
                        text.setText("Left(2)");
                } else if (225 <= angle && angle < 315) {
                    new_value = 3;
                    if (old_value != new_value)
                        text.setText("Back(3)");
                } else {
                    new_value = 4;
                    if (old_value != new_value)
                        text.setText("Right(4)");
                }
                if (old_value != new_value) {
                    udp.send(String.valueOf(new_value));
                    old_value = new_value;
                }
            }
        }

        void analyse() {
            if (old_value != 0) {
                text.setText("None(0)");
                udp.send("0");
                old_value = 0;
            }
        }
    }

    public class Raise {
        int old_value;
        ImageView color;
        ImageView white;
        RelativeLayout ball;
        TextView text;

        void init(int color, int white, int ball, int text) {
            this.color = (ImageView) findViewById(color);
            this.white = (ImageView) findViewById(white);
            this.ball = (RelativeLayout) findViewById(ball);
            this.text = (TextView) findViewById(text);
        }

        void origin() {
            ball.setX((float) (color.getWidth() / 2) + color.getX() - (float) (ball.getWidth() / 2));
            if (!isJoystick) {
                originY();
            } else {
                int yMax = (int) white.getY();
                ball.setY(yMax);
                text.setText("50");
            }
        }

        void originY() {
            ball.setY((float) (color.getHeight() / 2) + color.getY() - (float) (ball.getHeight() / 2));
            analyse();
        }

        void setY(int y) {
            y += ball.getHeight() / 4;
            int yPos;
            int yMax = white.getBottom() - ball.getHeight();
            int yMin = (int) white.getY();
            if (y > yMax) {
                // bottom most
                yPos = yMax;
                ball.setY(yMax);
            } else if (y < yMin) {
                // top most
                yPos = yMin;
                ball.setY(yMin);
            } else {
                // center
                yPos = y;
                ball.setY(y);
            }
            analyse(yPos, yMax, yMin);
        }

        void analyse(int y, int yMax, int yMin) {
            if (!isJoystick) {
                if (y < (color.getHeight() / 2) + color.getY() - layout.getY()) {
                    if (old_value != 6) {
                        text.setText("Top(6)");
                        udp.send("6");
                        old_value = 6;
                    }
                } else if (y > (color.getHeight() / 2) + color.getY() - layout.getY() + 60) {
                    if (old_value != 7) {
                        text.setText("Down(7)");
                        udp.send("7");
                        old_value = 7;
                    }
                } else {
                    analyse();
                }
            } else {
                int percentage = (y - white.getTop()) * 100 / (yMax - yMin);
                percentage = 100 - percentage;
                if (percentage != old_value) {
                    text.setText(String.valueOf(percentage));
                    udp.send(String.valueOf(percentage));
                    old_value = percentage;
                }
            }
        }

        void analyse() {
            if (old_value != 5) {
                text.setText("None(5)");
                udp.send("5");
                old_value = 5;
            }
        }
    }

    public Move move = new Move();
    public Raise raise = new Raise();

    public void myTouchAction(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        int x, y;
        boolean bool_move = false, bool_raise = false;

        for (int i = 0; i < event.getPointerCount(); i++) {
            x = (int) MotionEventCompat.getX(event, i);
            y = (int) MotionEventCompat.getY(event, i);
            if (x < layout.getWidth() / 2) {
                if (!isJoystick) {
                    if (action != MotionEvent.ACTION_UP || (action != ACTION_POINTER_UP && event.getX() == x && event.getY() == y))
                        bool_move = true;
                    move.setXY(x, y);
                }
            } else if (x >= raise.color.getLeft() && x <= raise.color.getRight()) {
                if (action != MotionEvent.ACTION_UP || (action != ACTION_POINTER_UP && event.getX() == x && event.getY() == y))
                    bool_raise = true;
                raise.setY(y);
            }
        }
        if (!isJoystick) {
            if (bool_move == false) {
                move.origin();
            }
        }
        if (bool_raise == false) {
            if (!isJoystick)
                raise.originY();
        }
    }

    public void origin() {
        if (!isJoystick)
            move.origin();
        raise.origin();
    }
}

