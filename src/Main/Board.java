package Main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

import Objects.Ball;
import Objects.Brick;
import Objects.BrickFactory;
import Objects.BrickNotBreakable;
import Objects.Missile;
import Objects.Player;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timer timer;
    private String saved_message = "Hi. Click NEW GAME or load a SAVED GAME.";
    private ArrayList<Ball> balls;
    private Player paddle;
    private boolean paused = false;
    private Brick[] bricks;
    private boolean inGame = false;
    private JWindow menuWindow = new JWindow();
    private instance[] instances;
    //Remove these two when testing is done
	private int TestingCount=0;
	private int powerCount =0;
    BrickFactory factory = new BrickFactory();
    private boolean messageVisble =true;
    public Board() {

        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));
        setBackground(Color.WHITE);
        gameInit();
        
    }

    private void gameInit() {
        paddle = Player.getPaddleInstance();

        instances = new instance[2];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new instance();
            }
        makeNewInstance();

        makingTheMenu();
        pauseGame();
        timer = new Timer(Commons.PERIOD, new GameCycle());
        timer.start();
    }
    //Don't use call this function. use makeNewInstance(); or makeNextLevel();
    private void makeGameInstance() {
        instances[0] = new instance();
        instances[0].setisEmpty(false);

        balls = instances[0].getBalls();
        bricks = instances[0].getbricks();


        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        maketheBricks();
        System.out.println("Current Life: " +paddle.getLife());

        paddle.setX(instances[0].getPlayerX());
        paddle.setY(instances[0].getPlayerY());
     }
    
    private void makeNewInstance() {
        makeGameInstance();
        paddle.initState();

    }
    private void makeNextLevel() {
        makeGameInstance();
        paddle.setBallStuckToPaddle(true);
    }
    private void getsavedInstance() throws CloneNotSupportedException {
        instances[0] = new instance();
        instances[0].setBallsCloneOf(instances[1].getBalls());
        instances[0].setBricksCloneOf(instances[1].getbricks());

        balls = instances[0].getBalls();
        bricks = instances[0].getbricks();

        paddle.setX(instances[1].getPlayerX());
        paddle.setY(instances[1].getPlayerY());
        paddle.setLife(instances[1].getLife());
        paddle.setScore(instances[1].getScore());
        paddle.setLevel(instances[1].getLevel());
        paddle.setPowerUp(instances[1].getplayerPowerUp());
        paddle.setBallStuckToPaddle(instances[1].isBallStuckToPaddle());

    }
    private void saveTheGame() throws CloneNotSupportedException{
        instances[1] = new instance();
        instances[1].setisEmpty(false);
        instances[1].setBallsCloneOf(instances[0].getBalls());
        instances[1].setBricksCloneOf(instances[0].getbricks());

        instances[1].setPlayerX(paddle.getX());
        instances[1].setPlayerY(paddle.getY());
        instances[1].setLife(paddle.getLife());
        instances[1].setScore(paddle.getScore());
        instances[1].setLevel(paddle.getLevel());
        instances[1].setPlayerPowerUp(paddle.getPlayerPowerUp());
        instances[1].setBallStuckToPaddle(paddle.isBallStuckToPaddle());

    }
    public void maketheBricks(){
        int k = 0;
        //Make the Bricks
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                int rand = ((int)(Math.random()*7)) + 1;
                bricks[k] = factory.getBrick(j * 145 + 120, i *50 + 50, rand);
                k++;
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (inGame) {
            drawObjects(g2d);
            drawScore(g2d);
            drawLevel(g2d);
            drawLife(g2d);
        }
        DrawSavedMessage(g2d);
        Toolkit.getDefaultToolkit().sync();
    }


   private void drawLife(Graphics2D g2d) {
    String message = "Life: ";
    message+= paddle.getLife();
    var font = new Font("Verdana", Font.BOLD, 15);
    FontMetrics fontMetrics = this.getFontMetrics(font);
    g2d.setColor(Color.RED);
    g2d.setFont(font);
    g2d.drawString(message, Commons.WIDTH - fontMetrics.stringWidth(message) - 2, 56);
    }

 private void drawLevel(Graphics2D g2d) {
        String message = "Level: ";
        message+= paddle.getLevel();
        var font = new Font("Verdana", Font.BOLD, 15);
        FontMetrics fontMetrics = this.getFontMetrics(font);
        g2d.setColor(Color.RED);
        g2d.setFont(font);
        g2d.drawString(message, Commons.WIDTH - fontMetrics.stringWidth(message) - 2, 18);

        }

    private void drawScore(Graphics2D g2d) {
        String message = "Score: "; 
        message+= paddle.getScore();
        var font = new Font("Verdana", Font.BOLD, 15);
        FontMetrics fontMetrics = this.getFontMetrics(font);
        g2d.setColor(Color.RED);
        g2d.setFont(font);
        g2d.drawString(message,Commons.WIDTH - fontMetrics.stringWidth(message) - 2, 36);

    }

    private void drawObjects(Graphics2D g2d) {
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),paddle.getImageWidth(), paddle.getImageHeight(), this);

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);    
            g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(), ball.getImageWidth(), ball.getImageHeight(), this);
        }
        for (int i = 0; i < Commons.N_OF_BRICKS; i++) {
            if (!bricks[i].isDestroyed()) {
                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(), bricks[i].getY(), bricks[i].getImageWidth(), bricks[i].getImageHeight(), this);
            }

        }
        for (int i = 0; i < paddle.getMissiles().size(); i++) {
        	 Missile missile = paddle.getMissiles().get(i);

        	if (!missile.isOutOfBounds() && !missile.isDestroyed()) {
                g2d.drawImage(missile.getImage(), missile.getX(),missile.getY(), missile.getImageWidth(), missile.getImageHeight(), this);
            }
        	else {
        		paddle.getMissiles().remove(i);
        	}
        }
    }
    private void DrawSavedMessage(Graphics2D g2d) {

        var font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        if(messageVisble){
            g2d.drawString(saved_message,(Commons.WIDTH - fontMetrics.stringWidth(saved_message)) / 2, 20);
        }
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!paused) {
                doGameCycle();
            }
            menuWindow.setLocationRelativeTo(Application.board);
            repaint();
        }
    }
    public void moveGameObjects(){
        paddle.loadImage();
        paddle.move();
        for(int i=0;i<bricks.length;i++){
            bricks[i].move();
        }
        for(int i=0;i<balls.size();i++){
            balls.get(i).move();
        }
        for(int i = 0; i < paddle.getMissiles().size(); i++) {
       	 	Missile missile = paddle.getMissiles().get(i);
       	 	missile.move();	
        }
    }
    private void doGameCycle() {
        moveGameObjects();
        checkCollision();
        checkifOnlyUnbreakbleBricksLeft();      
        //This is for testing powerups until the powerups are made.
        /*
        if(TestingCount>300) {
            String[] testingPowerArray = {"fire", "long", "small", "slow", "fast", "default"};
        	powerCount++;
            String testPowerValue = testingPowerArray[testingPowerArray.length - powerCount];
        	paddle.setPowerUp(testPowerValue);
            if(powerCount == testingPowerArray.length) {
                powerCount = 0;
            }
            paddle.setBallStuckToPaddle(true);
        	TestingCount = 0;
            
        }
        TestingCount++;
        */
    }
    private void stopGame() {
        saved_message = "Game Over. Choose NEW GAME or load a SAVED GAME";
        inGame = false;
        menuWindow.setVisible(true);
    }
    
  private void checkCollision() {
	  	checkCollisionBallsDropped();
	  	checkCollisionMissileBricks();
        checkCollisionPaddleBall();
        checkCollisionBallBricks();
        checkCollisionBrickMovement();
    }
  
    private void checkCollisionBrickMovement(){
        for(int i=0;i<bricks.length;i++){
            for(int j=0;j<bricks.length;j++){
                if(i==j) {
                    continue;
                }
                else{
                    if(bricks[i].getRect().intersects(bricks[j].getRect()) && !bricks[i].isDestroyed() && !bricks[j].isDestroyed()){  
                      if(bricks[j].getRect().getMaxX()>bricks[i].getRect().getMaxX()){
                            bricks[i].ChangeDirection("left");
                            bricks[j].ChangeDirection("right");
                        }
                        else {
                            bricks[i].ChangeDirection("right");
                            bricks[j].ChangeDirection("left");
                        }
                    }
                    if(bricks[i].getX()<=0)
                        bricks[i].ChangeDirection("right");
                    if(bricks[i].getX()>= Commons.WIDTH - bricks[i].getImageWidth())
                        bricks[i].ChangeDirection("left");
                }
            }
        }
    }

private void checkCollisionPaddleBall() {
    for(int i = 0; i < balls.size();i++) {
        Ball ball = balls.get(i);
        if ((ball.getRect()).intersects(paddle.getRect())) {
            Random rand = new Random();
            int max=0;
            int min=0;
            int paddleLPos = (int) paddle.getRect().getMinX();
            int ballLPos = (int) ball.getRect().getMinX();
  
            int first = paddleLPos +paddle.getImageWidth()/4;
            int second = paddleLPos +paddle.getImageWidth()/2;
            int third = paddleLPos + +3*paddle.getImageWidth()/4;
            int fourth = paddleLPos + 4*paddle.getImageWidth();
  
            if (ballLPos < first) {
              min = 135;
              max = 155;
            }
  
            if (ballLPos >= first && ballLPos < second) {
              min = 110;
              max = 130;  
              }
  
            if (ballLPos >= second && ballLPos < third) {
              if(rand.nextInt(2) == 0) {
                  min = 70;
                  max = 80;
              }
              else {
                  min = 100;
                  max = 110;
              }
            }

            if (ballLPos >= third && ballLPos < fourth) {
              min = 50;
              max = 70;
              }
  
            if (ballLPos > fourth) {
              min = 25;
              max = 45;
              }
            double radianValue = Math.toRadians(rand.nextInt((max - min) + 1) + min);
            System.out.println("Angle of Launch after collision: " + Math.toDegrees(radianValue));
            ball.setXDir((int)(ball.getSpeed()*Math.cos(radianValue)));
            ball.setYDir((int) (-Math.abs( ball.getSpeed()*Math.sin(radianValue))));
            System.out.println("YDIR: " + ball.getYDir() + "/ XDIR: " + ball.getXDir());
        }
    }
  
  }
  private void checkCollisionBallsDropped() {
    for(int i = 0; i < balls.size();i++) {
        Ball ball = balls.get(i);
        if (ball.getRect().getMaxY() > Commons.BOTTOM_EDGE) {
            balls.remove(i);
            if(balls.size() < 1){
                paddle.loseALife();
                balls.add(new Ball());
                paddle.setBallStuckToPaddle(true);
                ball.ballLaunchRandom();
            }
            System.out.println("Current Life: " + paddle.getLife());
            
            if(paddle.getLife()<1) {
                stopGame();
            }
        }
    }
  

      
  }
  private void checkifOnlyUnbreakbleBricksLeft(){
    boolean NoBricksLeft = true;
    boolean BreakableBricksLeft = false;  

    for(int i = 0; i < bricks.length;i++) {
        if(!bricks[i].isDestroyed()){
            NoBricksLeft = false;

            if(!(bricks[i] instanceof BrickNotBreakable)) {
                BreakableBricksLeft = true;
                break;
            }
        }

      }
      //if no breakable bricks left then:
        if(!BreakableBricksLeft){
            for(int r=0; r < balls.size();r++) {
                balls.get(r).ChangeToRedBall();
            }
            for(int k=0;k<Commons.N_OF_BRICKS; k++)
              bricks[k].setBreakable();
        }
        if (NoBricksLeft) {
            paddle.setLevel(paddle.getLevel()+1);
            saved_message = "Victory! Get Ready for Level " + (paddle.getLevel());
            makeNextLevel();
        }
  }
  private void checkCollisionBallBricks() {
  	   for (int i = 0; i < Commons.N_OF_BRICKS; i++) {
        for(int j = 0; j< balls.size();j++) {
            Ball ball = balls.get(j);
            if ((ball.getRect()).intersects(bricks[i].getRect())) {

                int ballLeft = (int) ball.getRect().getMinX();
                int ballHeight = (int) ball.getRect().getHeight();
                int ballWidth = (int) ball.getRect().getWidth();
                int ballTop = (int) ball.getRect().getMinY();

                var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                var pointLeft = new Point(ballLeft - 1, ballTop);
                var pointTop = new Point(ballLeft, ballTop - 1);
                var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].isDestroyed()) {
                    if (bricks[i].getRect().contains(pointRight)) {
                        ball.setXDir(-1*Math.abs(ball.getXDir()));
                    } 
                    else if (bricks[i].getRect().contains(pointLeft)) {
                        ball.setXDir(1*Math.abs(ball.getXDir()));
                    }
                    if (bricks[i].getRect().contains(pointTop)) {
                        ball.setYDir(1*Math.abs(ball.getYDir()));
                    } 
                    else if (bricks[i].getRect().contains(pointBottom)) {
                        ball.setYDir(-1*Math.abs(ball.getYDir()));
                    }
                    bricks[i].DecreaseHP();
                    bricks[i].updateImage();
                }
            }
            }
        }
  	}

  private void checkCollisionMissileBricks() {
	  for(int i = 0; i < paddle.getMissiles().size();i++) {
		  for (int b = 0; b < Commons.N_OF_BRICKS; b++) {
              if (!bricks[b].isDestroyed()) {
		          if ((paddle.getMissiles().get(i).getRect()).intersects(bricks[b].getRect())) {
	                  bricks[b].DecreaseHP();
                      bricks[b].updateImage();
	                  paddle.getMissiles().get(i).setDestroyed(true);
		          }
              }
		  }
	  }	

  }

    private void unpauseGame() {
        paused = false;
        messageVisble = false;
    }

    private void pauseGame() {
        paused = true;
        messageVisble = true;

    }

  private class TAdapter extends KeyAdapter {

      @Override
      public void keyReleased(KeyEvent e) {

          paddle.keyReleased(e);
      }
      
      @Override
      public void keyPressed(KeyEvent e) {
    	  //4
    	  int key = e.getKeyCode(); 
          paddle.keyPressed(e);

          if (key == KeyEvent.VK_ESCAPE) { 
              if(!menuWindow.isVisible()){
                menuWindow.setVisible(true);
                pauseGame();
              }
              else {
                menuWindow.setVisible(false);
                unpauseGame();
              }
        }
      }

  }

public void makingTheMenu(){
    JLabel nameLabel = new JLabel("MENU");
    nameLabel.setFont(new Font("Ariel", Font.BOLD, 30));
    
    JPanel topPanel = new JPanel();
    JPanel middlePanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    
    topPanel.add(nameLabel);
    
    //ingame buttons
    JButton resumeButton = new JButton("RESUME [ECS]");
    resumeButton.setFont(new Font("RESUME [ECS]", Font.BOLD, 14));
    JButton saveButton = new JButton("SAVE GAME");
    saveButton.setFont(new Font("Ariel", Font.BOLD, 14));
    JButton loadSaveButton = new JButton("LOAD SAVE");
    loadSaveButton.setFont(new Font("Ariel", Font.BOLD, 14));
    JButton newgameButton = new JButton("NEW GAME");
    newgameButton.setFont(new Font("Ariel", Font.BOLD, 14));
    JButton exitButton = new JButton("EXIT");
    exitButton.setFont(new Font("Ariel", Font.BOLD, 14));

    middlePanel.add(BorderLayout.CENTER, resumeButton);
    middlePanel.add(BorderLayout.CENTER, loadSaveButton);
    middlePanel.add(BorderLayout.CENTER, saveButton);

    middlePanel.add(BorderLayout.CENTER, newgameButton);
    middlePanel.add(BorderLayout.CENTER, exitButton);

    resumeButton.setVisible(false);
    saveButton.setVisible(false);

    menuWindow.add(BorderLayout.NORTH, topPanel);
    menuWindow.add(BorderLayout.CENTER, middlePanel);
    menuWindow.add(BorderLayout.SOUTH, bottomPanel); 


    newgameButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!inGame) {
                resumeButton.setVisible(true);
                saveButton.setVisible(true);
                inGame = true;
            }
            saved_message = "Press NEW GAME to start over, or LOAD SAVE to load a saved game";
            menuWindow.dispose();
            makeNewInstance();
            unpauseGame();
        }
    });
    resumeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            unpauseGame();
            saved_message = "Press NEW GAME to start over, or LOAD SAVE to load a saved game";
            menuWindow.dispose();
        }
    });
    loadSaveButton.addActionListener(new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            if(!instances[1].isEmpty()) {
                try {
                    getsavedInstance();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
                unpauseGame();
                saved_message = "Press NEW GAME to start over, or LOAD SAVE to load a saved game";
                menuWindow.dispose();
            }
            else {
                saved_message ="You have no Saved Data! Try NEW GAME.";
            }
        }
    });
    exitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);

        }
    });
    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveTheGame();
                saved_message = "Game File Saved successfully. Press RESUME to continue, or LOAD SAVE to open the saved File.";
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();
            }
        }
    });
    //These are the settings for the frame to have
    menuWindow.setPreferredSize(new Dimension(400, 350));
    menuWindow.pack();
    menuWindow.setAlwaysOnTop(true);
    menuWindow.setLocationRelativeTo(null);
    menuWindow.setVisible(true);
    menuWindow.repaint();
    }
}