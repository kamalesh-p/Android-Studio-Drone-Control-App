
            public boolean onTouch(View view, MotionEvent event) {
                boolean bool = false;
                int motionaction = event.getAction();
                Log.e("kamal 1", String.valueOf(motionaction));
                Log.e("kamal 2", String.valueOf(MotionEvent.ACTION_MASK));
                switch (motionaction & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("kamal", "First");
                        bool = true;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.e("kamal", "Second");
                        bool = true;
                        break;
                }
                if (bool) {
                    bool = false;
                    int x, y;
                    try {
                        x = (int) event.getX(motionaction & MotionEvent.ACTION_MASK);
                        y = (int) event.getY(motionaction & MotionEvent.ACTION_MASK);
                    }
                    catch(Exception ex){
                        return false;
                    }
                    String msg = "x = " + String.valueOf(x) + ", y = " + String.valueOf(y);
                    for (int i = 0; i < imageViewArray.length; i++) {
                        if (isPointWithin(x, y, imageViewArray[i].getLeft(), imageViewArray[i].getRight(), imageViewArray[i].getTop() - layout.getTop(), imageViewArray[i].getBottom() - layout.getTop())) {
                            bool = true;
                            if (joystick_move.equals(imageViewArray[i])) {
                                textView.setText("(1) " + msg);
                            } else if (joystick_raise.equals(imageViewArray[i])) {
                                textView.setText("(2) " + msg);
                            }
                        }
                    }
                    if (!bool) {
                        textView.setText(msg);
                    }
                }
                return false;
            }
