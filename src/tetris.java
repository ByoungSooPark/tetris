import java.awt.Color;

import java.awt.Graphics;

import java.awt.Image;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;



import javax.swing.JFrame;



public class tetris {

 public static void main(String[] args){

  new gameMain();

 }

}



class gameMain extends JFrame implements KeyListener{

 int key, move, score=0;

 boolean up, down, left, right, rot, end=false;

 int[][] stopBox=new int[10][30];//0null 1red 2blue 3green 4yellow

 int[][] movingBox=new int[10][30];//0null 1red 2blue 3green 4yellow

 int[][] tempBox=new int[10][30];//0null 1red 2blue 3green 4yellow

 float sin=(float)Math.sin(Math.PI/2), cos=(float)Math.cos(Math.PI/2);

 Graphics bufferGraphics;

 

 Image offscreen;

 int time=0;

    int[][][] figure={

       {{0,0,0, 0,  1, 1, 1, 1}, {0,0,0, 0,  0, 0, 0, 0}}, 

       {{0,0,0, 0, 0, 2, 2, 2}, {0,0,0, 0, 0, 2, 0, 0}}, 

       {{0,0,0, 0, 0, 3, 3,0}, {0,0,0, 0, 0, 3, 3,0}},

       {{0,0,0, 0, 4, 4, 0,0}, {0,0,0, 0, 0, 4, 4,0}}

      };

 gameMain(){

  init();

  while(true){

   process();

   drawing();

   if(end){

    break;

   }
 
   sleep();

   if(time<60){

    time++;

   }

   else{

    time=0;

   }

  }

  drawing();

 }

 private void sleep(){

  try{

   if(down==false){

    Thread.sleep(20);

   }

   else{

    Thread.sleep(5);

   }

  }catch(Exception e){

   

  }

 }

 private void process(){

  rotation();

  falling();

  sideMoving();

  trans();

  score();

  end();

 }

 

 private void making(){

  int sort=(int)(Math.random()*4);

  for(int a=0; a<figure[sort].length;a++){

   for(int b=0; b<figure[sort][a].length;b++){

    movingBox[a+5][b]=figure[sort][a][b];

   }

  }

 }

 private void trans(){

  if(downJudge()==true){

   for(int a=0; a<10; a++){

    for(int b=0; b<30; b++){

     if(movingBox[a][b]!=0){

      stopBox[a][b]=movingBox[a][b];

      movingBox[a][b]=0;

      

     }

    }

   }

   making();

  }

 }

 private void drawing(){

  repaint();

 }

 public void init()  

    {  

  

  setTitle("TETRIS");

  setSize(200, 600);

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  setResizable(false);

  setLocation(300, 100);

  setVisible(true);

  offscreen = createImage(200,600); 

        bufferGraphics = offscreen.getGraphics(); 

        addKeyListener(this);

        making();

    }



 public void paint(Graphics g)  

    { 

  if(bufferGraphics==null){

   return;

  }

  bufferGraphics.clearRect(0,0,200,600); 

        bufferGraphics.setColor(Color.BLACK);

  if(end==true){

   bufferGraphics.drawString("game over", 100, 100);

   return;

  }

         

         bufferGraphics.drawString("Score "+score, 100, 100);

         for(int a=0; a<10;a++){

          for(int b=0; b<30;b++){

              if(stopBox[a][b]!=0){

               if(stopBox[a][b]==1){

                bufferGraphics.setColor(Color.RED);

               }

               else if(stopBox[a][b]==2){

                bufferGraphics.setColor(Color.BLUE);

               }

               else if(stopBox[a][b]==3){

                bufferGraphics.setColor(Color.GREEN);

               }

               else if(stopBox[a][b]==4){

                bufferGraphics.setColor(Color.YELLOW);

               }

               bufferGraphics.fill3DRect(a*20, b*20, 20, 20, true);

              }

              else if(movingBox[a][b]!=0){

               if(movingBox[a][b]==1){

                bufferGraphics.setColor(Color.RED);

               }

               else if(movingBox[a][b]==2){

                bufferGraphics.setColor(Color.BLUE);

               }

               else if(movingBox[a][b]==3){

                bufferGraphics.setColor(Color.GREEN);

               }

               else if(movingBox[a][b]==4){

                bufferGraphics.setColor(Color.YELLOW);

               }

               bufferGraphics.fill3DRect(a*20, b*20, 20, 20, true);

              }

             }

         }

         

         

         g.drawImage(offscreen,0,0,this); 

    }





    public void update(Graphics g) 

    { 

         paint(g); 

    } 

    private void end(){

     

      for(int a=0; a<5;a++){

    

    for(int b=0; b<10;b++){

     if(stopBox[b][a]!=0){

      end=true;

     }

    }

      }

     

    }

 private void score(){

  if(time==0){

   int count;

   

   for(int a=0; a<30;a++){

    count=0;

    for(int b=0; b<10;b++){

     if(stopBox[b][a]!=0){

      count++;

     }

    }

    if(count==10){

     score++;

     lineDelete(a);

    }

   }

  }

 }

 private void lineDelete(int line){

  for(int b=0; b<10;b++){

   stopBox[b][line]=0;

  }

  for(int a=line-1; a>=0;a--){

   for(int b=0; b<10;b++){

    if(stopBox[b][a]!=0){

     tempBox[b][a+1]=stopBox[b][a];

     stopBox[b][a]=0;

    }

   }

  }

  for(int a=line; a>=0;a--){

   for(int b=0; b<10;b++){

    if(tempBox[b][a]!=0){

     stopBox[b][a]=tempBox[b][a];

     tempBox[b][a]=0;

    }

   }

  }

 }

 private boolean out(int x, int y){

  if(x>=0 && x<10 && y>=0 && y<30){

   return false;

  }

  return true;

 }

 private void rotation(){

  int centx=0, centy=0;

  for(int a=0; a<10;a++){

   for(int b=0; b<30;b++){

    if(movingBox[a][b]!=0){

     centx+=a;

     centy+=b;

    }

   }

  }

  centx/=4;

  centy/=4;

  

  if(up==true){

   rot=true;

   for(int a=0; a<10;a++){

    for(int b=0; b<30;b++){

     if(movingBox[a][b]!=0){

      if(out((int)(cos*(a-centx)-sin*(b-centy))+centx,(int)(sin*(a-centx)+cos*(b-centy))+centy)==false){

       

       tempBox[(int)(cos*(a-centx)-sin*(b-centy))+centx][(int)(sin*(a-centx)+cos*(b-centy))+centy]=movingBox[a][b];

      }

      else{

       rot=false;

      }

      

     }

    }

   }

   if(rot!=false){

    rot=rotJudge();

   }

   if(rot==false){

    for(int a=0; a<10;a++){

     for(int b=0; b<30;b++){

      if(tempBox[a][b]!=0){

       tempBox[a][b]=0;

      }

     }

    }

   }

   else{

    for(int a=0; a<10;a++){

     for(int b=0; b<30;b++){

      if(movingBox[a][b]!=0){

       movingBox[a][b]=0;

      }

     }

    }

    for(int a=0; a<10;a++){

     for(int b=0; b<30;b++){

      if(tempBox[a][b]!=0){

       movingBox[a][b]=tempBox[a][b];

       tempBox[a][b]=0;

       

      }

     }

    }

   }

   up=false;

  }

 }

 private void falling(){

  if(time==0){

   for(int a=0; a<10;a++){

    for(int b=0; b<30;b++){

     if(movingBox[a][b]!=0){

      tempBox[a][b+1]=movingBox[a][b];

      movingBox[a][b]=0;

      

     }

    }

   }

   for(int a=0; a<10;a++){

    for(int b=0; b<30;b++){

     if(tempBox[a][b]!=0){

      movingBox[a][b]=tempBox[a][b];

      tempBox[a][b]=0;

      

     }

    }

   }

  }

 }

 private void sideMoving(){

  if(right==true){

   move=1;right=false;

  }

  else if(left==true){

   move=-1;left=false;

  }

  if(sideJudge(move)==move){

   for(int a=0; a<10;a++){

    for(int b=0; b<30;b++){

     if(movingBox[a][b]!=0){

      tempBox[a+move][b]=movingBox[a][b];

      movingBox[a][b]=0;

      

     }

    }

   }

   for(int a=0; a<10;a++){

    for(int b=0; b<30;b++){

     if(tempBox[a][b]!=0){

      movingBox[a][b]=tempBox[a][b];

      tempBox[a][b]=0;

      

     }

    }

   }

   move=0;

  }

   

 }

 private boolean rotJudge(){

  for(int a=0; a<10;a++){

   for(int b=0; b<30;b++){

    if(tempBox[a][b]!=0 && stopBox[a][b]!=0){

     return false;

    }

   }

  }

  return true;

 }

 private int sideJudge(int mode){//-1 left 1right

  

  for(int a=0; a<10;a++){

   for(int b=0; b<30;b++){

    if(movingBox[a][b]!=0 && out(a+mode, b)==true){

     return 0;

    }

    else if(out(a+mode, b)==false && movingBox[a][b]!=0 && stopBox[a+mode][b]!=0){

     return 0;

    }

    

   }

  }

  return mode;

 }

 private boolean downJudge(){

  for(int a=0; a<10;a++){

   for(int b=1; b<30;b++){

    if(movingBox[a][b-1]!=0 && stopBox[a][b]!=0){

     return true;

    }

    else if(b==29 && movingBox[a][b]!=0){

     return true;

    }

   }

  }

  return false;

 }

 

 @Override

 public void keyPressed(KeyEvent arg0) {

  // TODO Auto-generated method stub

  key=arg0.getKeyCode();

  switch(key){

   case 37:

    left=true;

   break;

   case 38:

    up=true;

   break;

   case 39:

    right=true;

   break;

   case 40:

    down=true;

   break;

   

  }

  

  

 }

 @Override

 public void keyReleased(KeyEvent arg0) {

  // TODO Auto-generated method stub

  key=arg0.getKeyCode();

  switch(key){

   case 37:

    left=false;

   break;

   case 38:

    up=false;

   break;

   case 39:

    right=false;

   break;

   case 40:

    down=false;

   break;

   

  }

 }

 @Override

 public void keyTyped(KeyEvent arg0) {

  // TODO Auto-generated method stub

  

 }

}

